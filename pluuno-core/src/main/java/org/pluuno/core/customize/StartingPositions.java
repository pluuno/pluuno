package org.pluuno.core.customize;

import org.pluuno.core.Orientation;
import org.pluuno.core.ShapeType;
import org.pluuno.core.play.Engine;

public interface StartingPositions {
	public int startingX(ShapeType type, Engine engine);
	public int startingY(ShapeType type, Engine engine);
	public Orientation startingOrientation(ShapeType type, Engine engine);
}
