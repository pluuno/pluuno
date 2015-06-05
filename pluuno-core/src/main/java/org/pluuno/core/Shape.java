package org.pluuno.core;

import java.util.Objects;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class Shape {
	
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
	
	public Shape(ShapeType type, Orientation orientation, long mask) {
		this.type = Objects.requireNonNull(type);
		this.orientation = Objects.requireNonNull(orientation);
		this.mask = mask;
		
		id = (short)((type.getId() << 2) | orientation.toInt());
	}

	public Shape(ShapeType type, Orientation orientation, short id, long mask) {
		this.type = type;
		this.orientation = orientation;
		this.id = id;
		this.mask = mask;
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
		sb.append("\u2554\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2557\n");
		for(int i = 0; i < 64; i++) {
			if((i % 8) == 0)
				sb.append('\u2551');
			if((mask & (1L << i)) != 0)
				sb.append("[]");
			else
				sb.append("  ");
			if(((i + 1) % 8) == 0)
				sb.append("\u2551\n");
		}
		sb.append("\u255a\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u255d\n");
		return sb.toString();
	}
}
