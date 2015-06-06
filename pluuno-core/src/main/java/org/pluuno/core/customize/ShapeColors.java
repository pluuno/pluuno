package org.pluuno.core.customize;

import java.awt.Color;

import org.pluuno.core.Shape;
import org.pluuno.core.play.Engine;

public interface ShapeColors {
	public Color getInactiveColor(Shape shape, Engine engine);
	public Color getActiveColor(Shape shape, Engine engine);
	public Color getGhostColor(Shape shape, Engine engine);
}
