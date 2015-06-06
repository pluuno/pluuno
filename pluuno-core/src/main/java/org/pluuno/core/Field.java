package org.pluuno.core;

import java.util.Arrays;

public class Field {
	public static final int PAD = 7;
	public static final long PAD_MASK = (1L << PAD) - 1;
	
	private int width;
	private int fieldHeight;
	private int bufferHeight;
	private int top;
	private long[] mask;
	
	private long wall;
	
	public Field(int width, int fieldHeight, int bufferHeight) {
		if(width < 1 || width > 64 - (2 * PAD) || fieldHeight < 1 || bufferHeight < 1)
			throw new IllegalArgumentException();
		this.width = width;
		this.fieldHeight = fieldHeight;
		this.bufferHeight = bufferHeight;
		
		mask = new long[fieldHeight + bufferHeight + 2 * PAD];
		wall = PAD_MASK | (PAD_MASK << (PAD + width));
		top = PAD + bufferHeight;
		
		clear();
	}

	public void clear() {
		Arrays.fill(mask, wall);
		for(int i = 0; i < PAD; i++) {
			mask[i] = -1L;
			mask[mask.length - 1 - i] = -1L;
		}
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
		int x = XYShapes.x(xyshape);
		int y = XYShapes.y(xyshape);
		Shape shape = XYShapes.shape(xyshape);
		long[] smask = shape.getSplitMask();
		for(int i = 0; i < smask.length; i++) {
			mask[top + y + i] |= (smask[i] << (PAD + x));
		}
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
}
