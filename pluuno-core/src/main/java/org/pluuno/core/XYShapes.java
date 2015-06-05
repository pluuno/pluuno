package org.pluuno.core;

/**
 * Represents a {@link Shape} as a {@code long}.<p>
 * 
 * The format is:<br>
 * {@code [0-15: Shape id][16-23: X coordinate][24-31: Y coordinate][32-63: XYShape id]}
 * @author Robin
 *
 */
public class XYShapes {
	
	public static long of(Shape shape, int x, int y, int id) {
		return of(shape.getId(), x, y, id);
	}
	
	public static long of(int shapeId, int x, int y, int id) {
		long l = 0;
		l |= 0xFFFFL & shapeId;
		l |= (0xFFL & x) << 16;
		l |= (0xFFL & y) << 24;
		l |= (0xFFFFFFFFL & id) << 32;
		return l;
	}
	
	public static short shapeId(long l) {
		return (short) (0xFFFFL & l);
	}
	
	public static int x(long l) {
		return (int) ((0xFF0000L & l) >>> 16);
	}
	
	public static int y(long l) {
		return (int) ((0xFF000000L & l) >>> 24);
	}
	
	public static int id(long l) {
		return (int) ((0xFFFFFFFF00000000L & l) >>> 32);
	}
}
