package org.pluuno.core;

import java.util.Arrays;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class Field {
	public static final int PAD = 7;
	public static final long PAD_MASK = (1L << PAD) - 1;
	
	public static final int DEFAULT_WIDTH = 10;
	public static final int DEFAULT_HEIGHT = 20;
	
	public static final int MAX_WIDTH = 64 - 2 * PAD;
	
	public static class FieldSerializer extends Serializer<Field> {

		@Override
		public void write(Kryo kryo, Output output, Field object) {
			output.writeInt(object.getWidth(), true);
			output.writeInt(object.getFieldHeight(), true);
			output.writeInt(object.getBufferHeight(), true);
			for(long m : object.getMask()) {
				output.writeLong(m);
			}
		}

		@Override
		public Field read(Kryo kryo, Input input, Class<Field> type) {
			int width = input.readInt(true);
			int fieldHeight = input.readInt(true);
			int bufferHeight = input.readInt(true);
			long[] mask = new long[fieldHeight + bufferHeight + PAD * 2];
			for(int i = 0; i < mask.length; i++) {
				mask[i] = input.readLong();
			}
			return new Field(width, fieldHeight, bufferHeight, mask);
		}
		
	}
	
	private int width;
	private int fieldHeight;
	private int bufferHeight;
	private int top;
	private long[] mask;
	private long[] blocks;
	
	private long wall;
	private long[] wallBlocks;
	
	private long cleared;
	
	public Field() {
		this(DEFAULT_WIDTH, DEFAULT_HEIGHT);
	}
	
	public Field(int width, int height) {
		this(width, height, height);
	}
	
	public Field(int width, int fieldHeight, int bufferHeight) {
		if(width < 1 || width > 64 - (2 * PAD) || fieldHeight < 1 || bufferHeight < 1)
			throw new IllegalArgumentException();
		this.width = width;
		this.fieldHeight = fieldHeight;
		this.bufferHeight = bufferHeight;
		
		mask = new long[fieldHeight + bufferHeight + 2 * PAD];
		blocks = new long[mask.length * 64];
		wall = PAD_MASK | (PAD_MASK << (PAD + width));
		wallBlocks = new long[64];
		long wallBlock = Blocks.of(Blocks.FLAG_SOLID | Blocks.FLAG_WALL, 0, (short) 0);
		for(int i = 0; i < PAD; i++) {
			wallBlocks[i] = wallBlock;
			wallBlocks[width + 2 * PAD - i - 1] = wallBlock;
		}
		cleared = -1L >>> (64 - 2 * PAD - width);
		
		top = PAD + bufferHeight;
		
		reset();
	}

	private Field(int width, int fieldHeight, int bufferHeight, long[] mask) {
		this(width, fieldHeight, bufferHeight);
		this.mask = mask;
	}
	
	public void reset() {
		Arrays.fill(mask, wall);
		for(int i = 0; i < PAD; i++) {
			mask[i] = -1L;
			mask[mask.length - 1 - i] = -1L;
		}
		Arrays.fill(blocks, 0L);
		long wallBlock = Blocks.of(Blocks.FLAG_SOLID | Blocks.FLAG_WALL, -1, (short) 0);
		long[] tbp = new long[64 * PAD];
		Arrays.fill(tbp, wallBlock);
		System.arraycopy(tbp, 0, blocks, 0, tbp.length);
		System.arraycopy(tbp, 0, blocks, blocks.length - tbp.length, tbp.length);
		for(int y = -bufferHeight; y < fieldHeight; y++) {
			for(int x = 0; x < PAD; x++) {
				blocks[64 * (top + y) + x] = wallBlock;
				blocks[64 * (top + y) + PAD + width + x] = wallBlock;
			}
		}
	}
	
	public boolean intersects(long xyshape) {
		int x = XYShapes.x(xyshape);
		int y = XYShapes.y(xyshape);
		Shape shape = XYShapes.shape(xyshape);
		long[] smask = shape.getSplitMask();
		for(int i = 0; i < smask.length; i++) {
			long fmask;
			if(y + i < fieldHeight && y + i >= -bufferHeight)
				fmask = mask[top + y + i];
			else
				fmask = -1L;
			if((fmask & (smask[i] << (PAD + x))) != 0)
				return true;
		}
		return false;
	}
	
	public void shift(int fromY, int toY, int delta, long replaceWith, long[] replaceBlocksWith) {
		if(toY < fromY)
			throw new IllegalArgumentException();
		if(replaceBlocksWith.length != 64)
			throw new IllegalArgumentException();
		long[] replace = new long[Math.abs(delta)];
		Arrays.fill(replace, replaceWith);
		if(delta > 0) {
			if(fromY < -bufferHeight || fromY > fieldHeight || toY + delta < -bufferHeight || toY + delta > fieldHeight)
				throw new IllegalArgumentException();
			System.arraycopy(mask, top + fromY, mask, top + fromY + delta, toY - fromY);
			System.arraycopy(blocks, 64 * (top + fromY), blocks, 64 * (top + fromY + delta), 64 * (toY - fromY));
			System.arraycopy(replace, 0, mask, top + fromY, replace.length);
			for(int i = 0; i < delta; i++) {
				System.arraycopy(replaceBlocksWith, 0, blocks, 64 * (top + fromY + i), 64);
			}
		} else if(delta < 0) {
			if(fromY + delta < -bufferHeight || fromY + delta > fieldHeight || toY < -bufferHeight || toY > fieldHeight)
				throw new IllegalArgumentException();
			System.arraycopy(mask, top + fromY, mask, top + fromY + delta, toY - fromY);
			System.arraycopy(blocks, 64 * (top + fromY), blocks, 64 * (top + fromY + delta), 64 * (toY - fromY));
			System.arraycopy(replace, 0, mask, top + toY + delta, replace.length);
			for(int i = 0; i < -delta; i++) {
				System.arraycopy(replaceBlocksWith, 0, blocks, 64 * (top + toY - i), 64);
			}
		}
	}
	
	public void removeRow(int y) {
		shift(-bufferHeight, y, 1, wall, wallBlocks);
	}
	
	public boolean isCleared(int y) {
		return mask[top + y] == cleared;
	}
	
	public void blit(long xyshape, long block) {
		int x = XYShapes.x(xyshape);
		int y = XYShapes.y(xyshape);
		Shape shape = XYShapes.shape(xyshape);
		long[] smask = shape.getSplitMask();
		for(int i = 0; i < smask.length; i++) {
			mask[top + y + i] |= (smask[i] << (PAD + x));
		}
		for(int by = 0; by < Shape.MAX_DIM; by++) {
			for(int bx = 0; bx < Shape.MAX_DIM; bx++) {
				if((shape.getMask() & (1L << (by * Shape.MAX_DIM + bx))) != 0)
					blocks[64 * (top + y + by) + PAD + x + bx] = block;
			}
		}
	}
	
	public long getBlock(int x, int y) {
		if(x < -PAD || x >= width + PAD || y < -bufferHeight - PAD || y >= fieldHeight + PAD)
			return 0;
		long b = blocks[64 * (top + y) + PAD + x];
		if(y < 0)
			b |= Blocks.FLAG_BUFFER;
		return b;
	}
	
	public int getWidth() {
		return width;
	}

	public int getFieldHeight() {
		return fieldHeight;
	}

	public int getBufferHeight() {
		return bufferHeight;
	}

	public long[] getMask() {
		return mask;
	}
	
	public long[] getBlocks() {
		return blocks;
	}
	
	@Override
	public String toString() {
		return toMaskString();
	}
	
	public String toMaskString() {
		StringBuilder sb = new StringBuilder();
		sb.append('\u2554');
		for(int i = 0; i < width; i++)
			sb.append("\u2550\u2550");
		sb.append("\u2557\n");
		for(int y = 0; y < bufferHeight + fieldHeight; y++) {
			sb.append('\u2551');
			for(int x = 0; x < width; x++) {
				if((mask[PAD + y] & (1L << (PAD + x))) != 0)
					sb.append("[]");
				else if(y == bufferHeight - 1)
					sb.append("__");
				else
					sb.append("  ");
			}
			sb.append("\u2551\n");
		}
		sb.append('\u255a');
		for(int i = 0; i < width; i++)
			sb.append("\u2550\u2550");
		sb.append("\u255d\n");
		return sb.toString();
	}
}
