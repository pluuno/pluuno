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
		long xyshape = 0;
		xyshape |= 0xFFFFL & shapeId;
		xyshape |= (0xFFL & x) << 16;
		xyshape |= (0xFFL & y) << 24;
		xyshape |= (0xFFFFFFFFL & id) << 32;
		return xyshape;
	}
	
	public static short shapeId(long xyshape) {
		return (short) (0xFFFFL & xyshape);
	}
	
	public static Shape shape(long xyshape) {
		return Shape.of(shapeId(xyshape));
	}
	
	public static int x(long xyshape) {
		return (int) (byte) ((0xFF0000L & xyshape) >>> 16);
	}
	
	public static int y(long xyshape) {
		return (int) (byte) ((0xFF000000L & xyshape) >>> 24);
	}
	
	public static int id(long xyshape) {
		return (int) ((0xFFFFFFFF00000000L & xyshape) >>> 32);
	}
}
