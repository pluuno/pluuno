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
	
	public static class ShapeTypeSerializer extends Serializer<ShapeType> {

		@Override
		public void write(Kryo kryo, Output output, ShapeType object) {
			output.writeInt(object.getId(), true);
			if(object.getId() >= VALUES.length) {
				output.writeInt(object.getDim(), true);
				output.writeLong(object.getUp().getMask());
			}
		}

		@Override
		public ShapeType read(Kryo kryo, Input input, Class<ShapeType> type) {
			int id = input.readInt(true);
			if(id < VALUES.length)
				return VALUES[id];
			int dim = input.readInt(true);
			long mask = input.readLong();
			return new ShapeType(id, dim, mask);
		}
		
	}
	
	private static final ShapeType[] VALUES = new ShapeType[] {S, Z, J, L, T, I, O};
	
	private static long rotate8x8Left(long mask) {
		long rotated = 0;
		for(int i = 0; i < 8; i++) {
			long slice = (mask & (0xFFL << (i * 8))) >>> (i * 8);
			rotated |= inflate(slice) >>> i;
		}
		return rotated;
	}
	
	private static long inflate(long l) {
		long inflated = 0;
		long m = 1;
		for(int i = 0; i < 8; i++) {
			inflated |= (l & m) << (i * 7);
		}
		return inflated;
	}

	private static long join(long[] m) {
		long mask = 0;
		for(int i = 0; i < m.length; i++) {
			mask |= 0xFF & (m[i] << (i * 8));
		}
		return mask;
	}
	
	private int id;
	private int dim;
	
	private Shape up;
	private Shape right;
	private Shape down;
	private Shape left;

	public ShapeType(int id, int dim, long... m) {
		this(id, dim, join(m) >>> (8 - dim));
	}
	
	public ShapeType(int id, int dim, long mask) {
		this.id = id;
		this.dim = dim;
		up = new Shape(this, Orientation.UP, mask);
		mask = rotate8x8Left(mask) >>> (8 - dim);
		left = new Shape(this, Orientation.LEFT, mask);
		mask = rotate8x8Left(mask) >>> (8 - dim);
		down = new Shape(this, Orientation.DOWN, mask);
		mask = rotate8x8Left(mask) >>> (8 - dim);
		right = new Shape(this, Orientation.RIGHT, mask);
	}
	
	public int getId() {
		return id;
	}
	private int getDim() {
		return dim;
	}
	public Shape getUp() {
		return up;
	}
	public Shape getRight() {
		return right;
	}
	public Shape getDown() {
		return down;
	}
	public Shape getLeft() {
		return left;
	}
	
}
