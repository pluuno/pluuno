package org.pluuno.core;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class ShapeType {
	
	public static final ShapeType S = new ShapeType(0, 3, 
			0b011,
			0b110,
			0b000);
	public static final ShapeType Z = new ShapeType(1, 3, 
			0b110,
			0b011,
			0b000);
	public static final ShapeType J = new ShapeType(2, 3, 
			0b100,
			0b111,
			0b000);
	public static final ShapeType L = new ShapeType(3, 3, 
			0b001,
			0b111,
			0b000);
	public static final ShapeType T = new ShapeType(4, 3,
			0b010,
			0b111,
			0b000);
	public static final ShapeType I = new ShapeType(5, 4, 
			0b0000,
			0b1111,
			0b0000,
			0b0000);
	public static final ShapeType O = new ShapeType(6, 2, 
			0b11,
			0b11);
	
	public static ShapeType of(int id) {
		return TYPE_VALUES[id];
	}
	
	public static class ShapeTypeSerializer extends Serializer<ShapeType> {

		@Override
		public void write(Kryo kryo, Output output, ShapeType object) {
			output.writeInt(object.getId(), true);
			if(object.getId() >= TYPE_VALUES.length) {
				output.writeInt(object.getDim(), true);
				output.writeLong(object.getUp().getMask());
			}
		}

		@Override
		public ShapeType read(Kryo kryo, Input input, Class<ShapeType> type) {
			int id = input.readInt(true);
			if(id < TYPE_VALUES.length)
				return TYPE_VALUES[id];
			int dim = input.readInt(true);
			long mask = input.readLong();
			return new ShapeType(id, dim, mask);
		}
		
	}
	
	public static final ShapeType[] TYPE_VALUES = new ShapeType[] {S, Z, J, L, T, I, O};
	public static final Shape[] SHAPE_VALUES = new Shape[TYPE_VALUES.length * 4];
	static {
		for(int i = 0; i < TYPE_VALUES.length; i++) {
			SHAPE_VALUES[4*i + 0] = TYPE_VALUES[i].getUp();
			SHAPE_VALUES[4*i + 1] = TYPE_VALUES[i].getRight();
			SHAPE_VALUES[4*i + 2] = TYPE_VALUES[i].getDown();
			SHAPE_VALUES[4*i + 3] = TYPE_VALUES[i].getLeft();
		}
	}
	
	private static long rotate8x8Right(long mask) {
		long rotated = 0;
		for(int i = 0; i < 8; i++) {
			long slice = (mask & (0xFFL << (i * 8))) >>> (i * 8);
			rotated |= inflate(slice) << (7 - i);
		}
		return rotated;
	}
	
	private static long inflate(long l) {
		long inflated = 0;
		long m = 1;
		for(int i = 0; i < 8; i++) {
			inflated |= (l & m) << (i * 7);
			m = m << 1;
		}
		return inflated;
	}

	private static long join(long[] m) {
		long mask = 0;
		for(int i = 0; i < m.length; i++) {
			long b = 0xFFL & m[i];
			b = Long.reverse(b);
			b = b >>> 56;
			mask |= b << (i * 8);
		}
		return mask;
	}
	
	private short id;
	private int dim;
	
	private Shape[] shapes;

	public ShapeType(int id, int dim, long... m) {
		this(id, dim, join(m) >>> (8 - dim));
	}
	
	public ShapeType(int id, int dim, long mask) {
		this.id = (short) id;
		this.dim = dim;
		shapes = new Shape[4];
		shapes[0] = new Shape(this, Orientation.UP, mask);
		mask = rotate8x8Right(mask) >>> (8 - dim);
		shapes[1] = new Shape(this, Orientation.RIGHT, mask);
		mask = rotate8x8Right(mask) >>> (8 - dim);
		shapes[2] = new Shape(this, Orientation.DOWN, mask);
		mask = rotate8x8Right(mask) >>> (8 - dim);
		shapes[3] = new Shape(this, Orientation.LEFT, mask);
	}
	
	public short getId() {
		return id;
	}
	public int getDim() {
		return dim;
	}
	public Shape getShape(Orientation orientation) {
		return shapes[orientation.toInt()];
	}
	public Shape getUp() {
		return getShape(Orientation.UP);
	}
	public Shape getRight() {
		return getShape(Orientation.RIGHT);
	}
	public Shape getDown() {
		return getShape(Orientation.DOWN);
	}
	public Shape getLeft() {
		return getShape(Orientation.LEFT);
	}
	
	@Override
	public String toString() {
		if(id < TYPE_VALUES.length) {
			return Character.toString("SZJLTIO".charAt(id));
		}
		return String.format("<ShapeType: %d>", id);
	}
}
