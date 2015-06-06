package org.pluuno.core.customize;

import java.awt.Color;

import org.pluuno.core.Shape;

public class DefaultShapeColors implements ShapeColors {

	@Override
	public Color getColor(Shape shape) {
		return shape.getType().getDefaultColor();
	}

}
