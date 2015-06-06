package org.pluuno.core.customize;

import org.pluuno.core.Orientation;
import org.pluuno.core.Shape;
import org.pluuno.core.ShapeType;
import org.pluuno.core.play.Engine;

public class DefaultStartingPositions implements StartingPositions {

	@Override
	public int startingX(ShapeType type, Engine engine) {
		Shape shape = type.getShape(startingOrientation(type, engine));
		return engine.getField().getWidth() / 2 - shape.getWidth() / 2;
	}

	@Override
	public int startingY(ShapeType type, Engine engine) {
		Shape shape = type.getShape(startingOrientation(type, engine));
		return -shape.getHeight();
	}

	@Override
	public Orientation startingOrientation(ShapeType type, Engine engine) {
		return Orientation.UP;
	}

}
