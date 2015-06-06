package org.pluuno.core.play;

import org.pluuno.core.customize.RotationSystem;
import org.pluuno.core.customize.ShapeColors;
import org.pluuno.core.customize.StartingPositions;

public class EngineConfiguration {
	private ShapeColors shapeColors;
	private RotationSystem rotationSystem;
	private StartingPositions startingPositions;
	
	public ShapeColors getShapeColors() {
		return shapeColors;
	}
	public void setShapeColors(ShapeColors shapeColors) {
		this.shapeColors = shapeColors;
	}
	public RotationSystem getRotationSystem() {
		return rotationSystem;
	}
	public void setRotationSystem(RotationSystem rotationSystem) {
		this.rotationSystem = rotationSystem;
	}
	public StartingPositions getStartingPositions() {
		return startingPositions;
	}
	public void setStartingPositions(StartingPositions startingPositions) {
		this.startingPositions = startingPositions;
	}
}
