package org.pluuno.core;

import org.junit.Test;

public class FieldTest {
	@Test
	public void testString() {
		Field f = new Field(10, 5, 2);
		f.blit(XYShapes.of(ShapeType.T.getUp(), 0, 0, 0), 0);
		System.out.println(f);
	}
}
