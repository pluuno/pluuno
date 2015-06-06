package org.pluuno.core;

import java.util.Objects;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class Shape {
	public static final int MAX_DIM = 8;
	
	public static Shape of(short id) {
		return VALUES[id];
	}
	
	public static class ShapeSerializer extends Serializer<Shape> {
		@Override
		public void write(Kryo kryo, Output output, Shape object) {
			output.writeShort(object.getId());
			if(object.getId() >= VALUES.length) {
				kryo.writeObject(output, object.getType());
				output.writeInt(object.getOrientation().toInt(), true);
				output.writeLong(object.getMask());
			}
		}

		@Override
		public Shape read(Kryo kryo, Input input, Class<Shape> t) {
			short id = input.readShort();
			if(id < VALUES.length)
				return VALUES[id];
			ShapeType type = kryo.readObject(input, ShapeType.class);
			Orientation orientation = Orientation.fromInt(input.readInt(true));
			long mask = input.readLong();
			return new Shape(type, orientation, id, mask);
		}
	}
	
	static final Shape[] VALUES = new Shape[28];
	
	private ShapeType type;
	private Orientation orientation;
	private short id;
	private long mask;
	private long[] splitMask;
	
	public Shape(ShapeType type, Orientation orientation, long mask) {
		this(type, orientation, (short)((type.getId() << 2) | orientation.toInt()), mask);
	}

	public Shape(ShapeType type, Orientation orientation, short id, long mask) {
		this.type = Objects.requireNonNull(type);
		this.orientation = Objects.requireNonNull(orientation);
		this.id = id;
		this.mask = mask;
		splitMask = new long[MAX_DIM];
		for(int i = 0; i < MAX_DIM; i++) {
			splitMask[i] = (mask & (0xFFL << (i * MAX_DIM))) >>> (i * MAX_DIM);
		}
	}

	public ShapeType getType() {
		return type;
	}
	
	public Orientation getOrientation() {
		return orientation;
	}

	public long getMask() {
		return mask;
	}
	
	public long[] getSplitMask() {
		return splitMask;
	}

	public short getId() {
		return id;
	}
	
	@Override
	public String toString() {
		if(id < VALUES.length)
			return String.format("%s_%s", type, orientation);
		return String.format("<Shape %s %s>", type, orientation);
	}
	
	public String toMaskString() {
		StringBuilder sb = new StringBuilder();
		sb.append('\u2554');
		for(int i = 0; i < type.getDim(); i++)
			sb.append("\u2550\u2550");
		sb.append("\u2557\n");
		for(int i = 0; i < 8 * type.getDim(); i += MAX_DIM) {
			sb.append('\u2551');
			for(int j = 0; j < type.getDim(); j++) {
				if((mask & (1L << (i + j))) != 0)
					sb.append("[]");
				else
					sb.append("  ");
			}
			sb.append("\u2551\n");
		}
		sb.append('\u255a');
		for(int i = 0; i < type.getDim(); i++)
			sb.append("\u2550\u2550");
		sb.append("\u255d\n");
		return sb.toString();
	}
}
