package org.pluuno.core;

/**
 * {@link Blocks} are packed {@code long}s.<p>
 * 
 * The format is:<br>
 * {@code [0-7: flags][8-39: xyshape id][40-55: shape id]}
 * @author robin
 *
 */
public class Blocks {
	public static final int FLAG_SOLID =   0b00000001;
	public static final int FLAG_GARBAGE = 0b00000010;
	public static final int FLAG_ACTIVE =  0b00000100;
	public static final int FLAG_GHOST =   0b00001000;
	public static final int FLAG_WALL =    0b00010000;
	public static final int FLAG_BUFFER =  0b00100000;
	
	public static long of(int flags, int xyshapeId, short shapeId) {
		long b = 0;
		b |= 0xFFL & flags;
		b |= (0xFFFFFFFFL & xyshapeId) << 8;
		b |= (0xFFFFL & shapeId) << 40;
		return b;
	}
	
	public static int flags(long block) {
		return (int) (0xFFL & block);
	}

	public static int xyshapeId(long block) {
		return (int) ((0xFFFFFFFF00L & block) >>> 8);
	}
	
	public static short shapeId(long block) {
		return (short)((0xFFFF0000000000L & block) >>> 40);
	}
}
