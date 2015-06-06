package org.pluuno.core.customize;

import java.awt.Color;

import org.pluuno.core.Shape;
import org.pluuno.core.play.Engine;

public interface ShapeColors {
	public Color getColor(int blockFlags, int shapeId, Engine engine);
}
