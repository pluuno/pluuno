package org.pluuno.core.customize;

import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import org.pluuno.core.Orientation;
import org.pluuno.core.Shape;
import org.pluuno.core.ShapeType;
import org.pluuno.core.XYShapes;
import org.pluuno.core.play.Engine;

public class PropertiesCustomization 
implements 
ShapeColors,
StartingPositions,
RotationSystem
{
	public static final String STARTING_X_OFFSET = ".starting.x-offset";
	public static final String STARTING_Y_OFFSET = ".starting.y-offset";
	public static final String STARTING_ORIENTATION = ".starting.orientation";
	public static final String INACTIVE_COLOR = ".color.inactive";
	public static final String ACTIVE_COLOR = ".color.active";
	public static final String GHOST_COLOR = ".color.ghost";
	public static final String CLOCKWISE_X_KICKS = ".rotate.clockwise.x-kicks";
	public static final String CLOCKWISE_Y_KICKS = ".rotate.clockwise.y-kicks";
	public static final String COUNTERCLOCKWISE_X_KICKS = ".rotate.counterclockwise.x-kicks";
	public static final String COUNTERCLOCKWISE_Y_KICKS = ".rotate.counterclockwise.y-kicks";
	
	private static final Properties load(URL url) {
		try {
			InputStream in = url.openStream();
			try {
				Properties props = new Properties();
				props.load(in);
				return props;
			} finally {
				in.close();
			}
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	private Properties props;
	
	public PropertiesCustomization(URL url) {
		this(load(url));
	}
	
	public PropertiesCustomization(Properties props) {
		this.props = props;
	}

	private String pv(ShapeType type, String suffix) {
		String v = props.getProperty(type + suffix);
		if(v == null)
			v = props.getProperty(type.getId() + suffix);
		if(v == null)
			v = props.getProperty("*" + suffix);
		return v;
	}
	
	private String pv(Shape shape, String suffix) {
		String v = props.getProperty(shape.getType() + "." + shape.getOrientation().toString().toLowerCase() + "." + suffix);
		if(v == null)
			v = props.getProperty(shape.getType().getId() + "." + shape.getOrientation().toString().toLowerCase() + "." + suffix);
		if(v == null)
			v = props.getProperty(shape.getType() + ".*" + suffix);
		if(v == null)
			v = props.getProperty("*.*" + suffix);
		return v;
	}
	
	@Override
	public int startingX(ShapeType type, Engine engine) {
		int xoff = Integer.parseInt(pv(type, STARTING_X_OFFSET));
		Shape shape = type.getShape(startingOrientation(type, engine));
		return engine.getField().getWidth() / 2 - shape.getWidth() / 2 + xoff;
	}

	@Override
	public int startingY(ShapeType type, Engine engine) {
		int yoff = Integer.parseInt(pv(type, STARTING_Y_OFFSET));
		Shape shape = type.getShape(startingOrientation(type, engine));
		return -shape.getHeight() + yoff;
	}

	@Override
	public Orientation startingOrientation(ShapeType type, Engine engine) {
		return Orientation.valueOf(pv(type, STARTING_ORIENTATION).toUpperCase());
	}

	@Override
	public Color getInactiveColor(Shape shape, Engine engine) {
		String[] rgba = pv(shape, INACTIVE_COLOR).split(",");
		return new Color(
				Integer.parseInt(rgba[0].trim()),
				Integer.parseInt(rgba[1].trim()),
				Integer.parseInt(rgba[2].trim()),
				Integer.parseInt(rgba[3].trim()));
	}

	@Override
	public Color getActiveColor(Shape shape, Engine engine) {
		String[] rgba = pv(shape, ACTIVE_COLOR).split(",");
		return new Color(
				Integer.parseInt(rgba[0].trim()),
				Integer.parseInt(rgba[1].trim()),
				Integer.parseInt(rgba[2].trim()),
				Integer.parseInt(rgba[3].trim()));
	}

	@Override
	public Color getGhostColor(Shape shape, Engine engine) {
		String[] rgba = pv(shape, GHOST_COLOR).split(",");
		return new Color(
				Integer.parseInt(rgba[0].trim()),
				Integer.parseInt(rgba[1].trim()),
				Integer.parseInt(rgba[2].trim()),
				Integer.parseInt(rgba[3].trim()));
	}
	
	@Override
	public long rotateClockwise(long xyshape, Engine engine) {
		Shape shape = XYShapes.shape(xyshape);
		String xks = pv(shape, CLOCKWISE_X_KICKS);
		String yks = pv(shape, CLOCKWISE_Y_KICKS);
		if(xks == null)
			xks = pv(shape.getType(), CLOCKWISE_X_KICKS);
		if(yks == null)
			yks = pv(shape.getType(), CLOCKWISE_Y_KICKS);
		String[] xs = xks.split(",");
		String[] ys = yks.split(",");
		
		int[] xo = new int[xs.length];
		int[] yo = new int[ys.length];
		
		for(int i = 0; i < xs.length; i++)
			xo[i] = Integer.parseInt(xs[i]);
		for(int i = 0; i < ys.length; i++)
			yo[i] = Integer.parseInt(ys[i]);
		
		long rotated = XYShapes.clockwise(xyshape);
		for(int i = 0; i < xo.length; i++) {
			long t = XYShapes.shifted(rotated, xo[i], yo[i]);
			if(!engine.getField().intersects(t))
				return t;
		}
		return xyshape;
	}

	@Override
	public long rotateCounterclockwise(long xyshape, Engine engine) {
		Shape shape = XYShapes.shape(xyshape);
		String xks = pv(shape, COUNTERCLOCKWISE_X_KICKS);
		String yks = pv(shape, COUNTERCLOCKWISE_Y_KICKS);
		if(xks == null)
			xks = pv(shape.getType(), COUNTERCLOCKWISE_X_KICKS);
		if(yks == null)
			yks = pv(shape.getType(), COUNTERCLOCKWISE_Y_KICKS);
		String[] xs = xks.split(",");
		String[] ys = yks.split(",");
		
		int[] xo = new int[xs.length];
		int[] yo = new int[ys.length];
		
		for(int i = 0; i < xs.length; i++)
			xo[i] = Integer.parseInt(xs[i]);
		for(int i = 0; i < ys.length; i++)
			yo[i] = Integer.parseInt(ys[i]);
		
		long rotated = XYShapes.counterclockwise(xyshape);
		for(int i = 0; i < xo.length; i++) {
			long t = XYShapes.shifted(rotated, xo[i], yo[i]);
			if(!engine.getField().intersects(t))
				return t;
		}
		return xyshape;
	}

}
