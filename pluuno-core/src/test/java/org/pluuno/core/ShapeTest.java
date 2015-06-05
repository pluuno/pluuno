package org.pluuno.core;

import org.junit.Test;

public class ShapeTest {
	@Test
	public void testMaskStrings() {
		System.out.println(ShapeType.J.getUp().toMaskString());
		System.out.println(ShapeType.J.getRight().toMaskString());
		System.out.println(ShapeType.J.getDown().toMaskString());
		System.out.println(ShapeType.J.getLeft().toMaskString());
	}
}
