package org.pluuno.core.customize;

import org.pluuno.core.Orientation;
import org.pluuno.core.ShapeType;

public interface StartingPositions {
	public int startingX(ShapeType type);
	public int startingY(ShapeType type);
	public Orientation startingOrientation(ShapeType type);
}
