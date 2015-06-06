package org.pluuno.core;

import java.awt.Color;

/**
 * {@link Blocks} are packed {@code long}s.<p>
 * 
 * The format is:<br>
 * {@code [0-7: flags][8-15: red][16-23: green][24-31: blue][32-39: alpha][40-55: shape id]}
 * @author robin
 *
 */
public class Blocks {
	public static final int FLAG_SOLID =   0b00000001;
	public static final int FLAG_GARBAGE = 0b00000010;
	public static final int FLAG_ACTIVE =  0b00000100;
	public static final int FLAG_GHOST =   0b00001000;
	public static final int FLAG_WALL =    0b00010000;
	
	public static long of(int flags, int red, int green, int blue, int alpha, short shapeId) {
		long b = 0;
		b |= (0xFFL & flags) << 0;
		b |= (0xFFL & red) << 8;
		b |= (0xFFL & green) << 16;
		b |= (0xFFL & blue) << 24;
		b |= (0xFFL & alpha) << 32;
		b |= (0xFFFFL & shapeId) << 40;
		return b;
	}
	
	public static long of(int flags, Color c, short shapeId) {
		return of(flags, c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha(), shapeId);
	}
	
	public static long of(int flags, short shapeId) {
		return of(flags, 0, 0, 0, 0, shapeId);
	}
	
	public static int flags(long block) {
		return (int) (0xFFL & block);
	}
	
	public static int red(long block) {
		return (int)((0xFF00L & block) >>> 8);
	}
	
	public static int green(long block) {
		return (int)((0xFF0000L & block) >>> 16);
	}
	
	public static int blue(long block) {
		return (int)((0xFF000000L & block) >>> 24);
	}
	
	public static int alpha(long block) {
		return (int)((0xFF00000000L & block) >>> 32);
	}
	
	public static Color color(long block) {
		return new Color(red(block), green(block), blue(block), alpha(block));
	}
	
	public static short shapeId(long block) {
		return (short)((0xFFFF0000000000L & block) >>> 40);
	}
}
