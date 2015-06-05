package org.pluuno.core;

import static org.pluuno.core.ShapeType.I;
import static org.pluuno.core.ShapeType.J;
import static org.pluuno.core.ShapeType.L;
import static org.pluuno.core.ShapeType.O;
import static org.pluuno.core.ShapeType.S;
import static org.pluuno.core.ShapeType.T;
import static org.pluuno.core.ShapeType.Z;

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
	
	private static final Shape[] VALUES = new Shape[] {
		S.getUp(), S.getRight(), S.getDown(), S.getLeft(),
		Z.getUp(), Z.getRight(), Z.getDown(), Z.getLeft(),
		J.getUp(), J.getRight(), J.getDown(), J.getLeft(),
		L.getUp(), L.getRight(), L.getDown(), L.getLeft(),
		T.getUp(), T.getRight(), T.getDown(), T.getLeft(),
		I.getUp(), I.getRight(), I.getDown(), I.getLeft(),
		O.getUp(), O.getRight(), O.getDown(), O.getLeft(),
	};
	
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
}
