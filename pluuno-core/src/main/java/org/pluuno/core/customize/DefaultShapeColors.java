package org.pluuno.core.customize;

import java.awt.Color;

import org.pluuno.core.Shape;
import org.pluuno.core.play.Engine;

public class DefaultShapeColors implements ShapeColors {

	@Override
	public Color getInactiveColor(Shape shape, Engine engine) {
		return shape.getType().getDefaultColor();
	}

	@Override
	public Color getActiveColor(Shape shape, Engine engine) {
		return shape.getType().getDefaultColor().brighter();
	}

	@Override
	public Color getGhostColor(Shape shape, Engine engine) {
		return new Color(255, 255, 255, 127);
	}

}
