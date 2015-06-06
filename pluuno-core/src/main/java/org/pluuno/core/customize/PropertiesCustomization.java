package org.pluuno.core.customize;

import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

import org.pluuno.core.Blocks;
import org.pluuno.core.Orientation;
import org.pluuno.core.Shape;
import org.pluuno.core.ShapeType;
import org.pluuno.core.XYShapes;
import org.pluuno.core.play.Engine;

public class PropertiesCustomization 
implements 
ShapeColors,
StartingPositions,
RotationSystem,
Ghosting,
Delays
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
	public static final String GHOSTING_ENABLED = ".ghosting.enabled";
	public static final String BACKGROUND_COLOR = "color.background";
	public static final String WALL_COLOR = "color.wall";
	public static final String GARBAGE_COLOR = "color.garbage";
	public static final String BUFFER_COLOR = "color.buffer";
	public static final String DELAY_DAS_UP = "delay.das.up";
	public static final String DELAY_DAS_RIGHT = "delay.das.right";
	public static final String DELAY_DAS_DOWN = "delay.das.down";
	public static final String DELAY_DAS_LEFT = "delay.das.left";
	public static final String DELAY_ARE = "delay.are";
	
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
	private Map<String, Object> cache = new HashMap<>();
	
	public PropertiesCustomization(URL url) {
		this(load(Objects.requireNonNull(url)));
	}
	
	public PropertiesCustomization(Properties props) {
		this.props = Objects.requireNonNull(props);
	}

	@SuppressWarnings("unchecked")
	public <T> T getCache(String key) {
		return (T) cache.get(key);
	}
	
	public <T> T setCache(String key, T value) {
		cache.put(key, value);
		return value;
	}
	
	protected String pv(ShapeType type, String suffix) {
		String v = props.getProperty(type + suffix);
		if(v == null)
			v = props.getProperty(type.getId() + suffix);
		if(v == null)
			v = props.getProperty("*" + suffix);
		return v;
	}
	
	protected String pv(Shape shape, String suffix) {
		String v = props.getProperty(shape.getType() + "." + shape.getOrientation().toString().toLowerCase() + suffix);
		if(v == null)
			v = props.getProperty(shape.getType().getId() + "." + shape.getOrientation().toString().toLowerCase() + suffix);
		if(v == null)
			v = props.getProperty(shape.getType() + ".*" + suffix);
		if(v == null)
			v = props.getProperty("*." + shape.getOrientation().toString().toLowerCase() + suffix);
		if(v == null)
			v = props.getProperty("*.*" + suffix);
		return v;
	}
	
	@Override
	public int startingX(ShapeType type, Engine engine) {
		Integer c = getCache(type + STARTING_X_OFFSET);
		if(c != null)
			return c;
		int xoff = Integer.parseInt(pv(type, STARTING_X_OFFSET));
		Shape shape = type.getShape(startingOrientation(type, engine));
		return setCache(type + STARTING_X_OFFSET, engine.getField().getWidth() / 2 - (shape.getWidth()+1) / 2 + xoff);
	}

	@Override
	public int startingY(ShapeType type, Engine engine) {
		Integer c = getCache(type + STARTING_Y_OFFSET);
		if(c != null)
			return c;
		int yoff = Integer.parseInt(pv(type, STARTING_Y_OFFSET));
		Shape shape = type.getShape(startingOrientation(type, engine));
		return setCache(type + STARTING_Y_OFFSET, -shape.getHeight() + yoff);
	}

	@Override
	public Orientation startingOrientation(ShapeType type, Engine engine) {
		Orientation c = getCache(type + STARTING_ORIENTATION);
		if(c != null)
			return c;
		return setCache(type + STARTING_ORIENTATION, Orientation.valueOf(pv(type, STARTING_ORIENTATION).toUpperCase()));
	}

	@Override
	public Color getColor(int blockFlags, int shapeId, Engine engine) {
		Color c = getCache("color." + blockFlags + "." + shapeId);
		if(c != null)
			return c;
		if((blockFlags & Blocks.FLAG_GHOST) != 0)
			c = getGhostColor(Shape.of(shapeId), engine);
		else if((blockFlags & Blocks.FLAG_ACTIVE) != 0)
			c = getActiveColor(Shape.of(shapeId), engine);
		else if((blockFlags & Blocks.FLAG_GARBAGE) != 0)
			c = getGarbageColor();
		else if((blockFlags & Blocks.FLAG_WALL) != 0)
			c = getWallColor();
		else if((blockFlags & Blocks.FLAG_SOLID) != 0)
			c = getInactiveColor(Shape.of(shapeId), engine);
		else if((blockFlags & Blocks.FLAG_BUFFER) != 0)
			c = getBufferColor();
		else
			c = getBackgroundColor();
		return setCache("color." + blockFlags + "." + shapeId, c);
	}

	public Color getInactiveColor(Shape shape, Engine engine) {
		String[] rgba = pv(shape, INACTIVE_COLOR).split(",");
		return new Color(
				Integer.parseInt(rgba[0].trim()),
				Integer.parseInt(rgba[1].trim()),
				Integer.parseInt(rgba[2].trim()),
				Integer.parseInt(rgba[3].trim()));
	}

	public Color getActiveColor(Shape shape, Engine engine) {
		String[] rgba = pv(shape, ACTIVE_COLOR).split(",");
		return new Color(
				Integer.parseInt(rgba[0].trim()),
				Integer.parseInt(rgba[1].trim()),
				Integer.parseInt(rgba[2].trim()),
				Integer.parseInt(rgba[3].trim()));
	}

	public Color getGhostColor(Shape shape, Engine engine) {
		String[] rgba = pv(shape, GHOST_COLOR).split(",");
		return new Color(
				Integer.parseInt(rgba[0].trim()),
				Integer.parseInt(rgba[1].trim()),
				Integer.parseInt(rgba[2].trim()),
				Integer.parseInt(rgba[3].trim()));
	}
	
	public Color getBackgroundColor() {
		String[] rgba = props.getProperty(BACKGROUND_COLOR).split(",");
		return new Color(
				Integer.parseInt(rgba[0].trim()),
				Integer.parseInt(rgba[1].trim()),
				Integer.parseInt(rgba[2].trim()),
				Integer.parseInt(rgba[3].trim()));
	}
	
	public Color getBufferColor() {
		String[] rgba = props.getProperty(BUFFER_COLOR).split(",");
		return new Color(
				Integer.parseInt(rgba[0].trim()),
				Integer.parseInt(rgba[1].trim()),
				Integer.parseInt(rgba[2].trim()),
				Integer.parseInt(rgba[3].trim()));
	}
	
	public Color getWallColor() {
		String[] rgba = props.getProperty(WALL_COLOR).split(",");
		return new Color(
				Integer.parseInt(rgba[0].trim()),
				Integer.parseInt(rgba[1].trim()),
				Integer.parseInt(rgba[2].trim()),
				Integer.parseInt(rgba[3].trim()));
	}
	
	public Color getGarbageColor() {
		String[] rgba = props.getProperty(GARBAGE_COLOR).split(",");
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
		String[] xs = xks.split(",");
		String[] ys = yks.split(",");
		
		int[] xo = new int[xs.length];
		int[] yo = new int[ys.length];
		
		for(int i = 0; i < xs.length; i++)
			xo[i] = Integer.parseInt(xs[i].trim());
		for(int i = 0; i < ys.length; i++)
			yo[i] = Integer.parseInt(ys[i].trim());
		
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
		String[] xs = xks.split(",");
		String[] ys = yks.split(",");
		
		int[] xo = new int[xs.length];
		int[] yo = new int[ys.length];
		
		for(int i = 0; i < xs.length; i++)
			xo[i] = Integer.parseInt(xs[i].trim());
		for(int i = 0; i < ys.length; i++)
			yo[i] = Integer.parseInt(ys[i].trim());
		
		long rotated = XYShapes.counterclockwise(xyshape);
		for(int i = 0; i < xo.length; i++) {
			long t = XYShapes.shifted(rotated, xo[i], yo[i]);
			if(!engine.getField().intersects(t))
				return t;
		}
		return xyshape;
	}

	@Override
	public Long computeGhost(Long xyshape, Engine engine) {
		if(xyshape == null)
			return null;
		ShapeType type = XYShapes.shape(xyshape).getType();
		String v = pv(type, GHOSTING_ENABLED);
		if(!Boolean.parseBoolean(v))
			return null;
		long nxy = XYShapes.shifted(xyshape, 0, 1);
		while(!engine.getField().intersects(nxy)) {
			xyshape = nxy;
			nxy = XYShapes.shifted(nxy, 0, 1);
		}
		return xyshape;
	}

	@Override
	public long getDASUp() {
		Long das = getCache(DELAY_DAS_UP);
		if(das != null)
			return das;
		return setCache(DELAY_DAS_UP, Long.parseLong(props.getProperty(DELAY_DAS_UP)));
	}

	@Override
	public long getDASRight() {
		Long das = getCache(DELAY_DAS_RIGHT);
		if(das != null)
			return das;
		return setCache(DELAY_DAS_RIGHT, Long.parseLong(props.getProperty(DELAY_DAS_RIGHT)));
	}

	@Override
	public long getDASDown() {
		Long das = getCache(DELAY_DAS_DOWN);
		if(das != null)
			return das;
		return setCache(DELAY_DAS_DOWN, Long.parseLong(props.getProperty(DELAY_DAS_DOWN)));
	}

	@Override
	public long getDASLeft() {
		Long das = getCache(DELAY_DAS_LEFT);
		if(das != null)
			return das;
		return setCache(DELAY_DAS_LEFT, Long.parseLong(props.getProperty(DELAY_DAS_LEFT)));
	}

	@Override
	public long getARE() {
		Long are = getCache(DELAY_ARE);
		if(are != null)
			return are;
		return setCache(DELAY_ARE, Long.parseLong(props.getProperty(DELAY_ARE)));
	}
}
