package org.pluuno.core;

import java.awt.Color;
import java.util.Arrays;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class Field {
	public static final int PAD = 7;
	public static final long PAD_MASK = (1L << PAD) - 1;
	
	public static final int DEFAULT_WIDTH = 10;
	public static final int DEFAULT_FIELD_HEIGHT = 20;
	public static final int DEFAULT_BUFFER_HEIGHT = 20;
	
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
	
	public Field() {
		this(DEFAULT_WIDTH, DEFAULT_FIELD_HEIGHT, DEFAULT_BUFFER_HEIGHT);
	}
	
	public Field(int width, int fieldHeight, int bufferHeight) {
		if(width < 1 || width > 64 - (2 * PAD) || fieldHeight < 1 || bufferHeight < 1)
			throw new IllegalArgumentException();
		this.width = width;
		this.fieldHeight = fieldHeight;
		this.bufferHeight = bufferHeight;
		
		mask = new long[fieldHeight + bufferHeight + 2 * PAD];
		blocks = new long[width * (fieldHeight + bufferHeight)];
		wall = PAD_MASK | (PAD_MASK << (PAD + width));
		top = PAD + bufferHeight;
		
		clear();
	}

	private Field(int width, int fieldHeight, int bufferHeight, long[] mask) {
		this(width, fieldHeight, bufferHeight);
		this.mask = mask;
	}
	
	public void clear() {
		Arrays.fill(mask, wall);
		for(int i = 0; i < PAD; i++) {
			mask[i] = -1L;
			mask[mask.length - 1 - i] = -1L;
		}
		Arrays.fill(blocks, 0L);
	}
	
	public boolean intersects(long xyshape) {
		int x = XYShapes.x(xyshape);
		int y = XYShapes.y(xyshape);
		Shape shape = XYShapes.shape(xyshape);
		long[] smask = shape.getSplitMask();
		for(int i = 0; i < smask.length; i++) {
			long fmask = mask[top + y + i] & ~wall;
			if((fmask & (smask[i] << (PAD + x))) != 0)
				return true;
		}
		return false;
	}
	
	public void blit(long xyshape) {
		Shape shape = XYShapes.shape(xyshape);
		blit(xyshape, shape.getType().getDefaultColor());
	}
	
	public void blit(long xyshape, Color c) {
		int x = XYShapes.x(xyshape);
		int y = XYShapes.y(xyshape);
		Shape shape = XYShapes.shape(xyshape);
		long[] smask = shape.getSplitMask();
		for(int i = 0; i < smask.length; i++) {
			mask[top + y + i] |= (smask[i] << (PAD + x));
		}
		long block = Blocks.of(Blocks.FLAG_SOLID, c, shape.getId());
		for(int by = 0; by < Shape.MAX_DIM; by++) {
			for(int bx = 0; bx < Shape.MAX_DIM; bx++) {
				if((shape.getMask() & (1L << (by * Shape.MAX_DIM + bx))) != 0)
					blocks[width * y + width * by + x + bx] = block;
			}
		}
	}
	
	public long getBlock(int x, int y) {
		return blocks[width * y + x];
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
