package org.pluuno.core;

public enum Orientation {
	UP,
	RIGHT,
	DOWN,
	LEFT,
	;
	
	private static final Orientation[] VALUES = Orientation.values();
	
	public int toInt() {
		return ordinal();
	}
	
	public static Orientation fromInt(int i) {
		return VALUES[i];
	}
}
