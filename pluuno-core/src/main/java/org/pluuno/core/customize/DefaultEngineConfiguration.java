package org.pluuno.core.customize;


public class DefaultEngineConfiguration implements EngineConfiguration {
	private ShapeColors shapeColors;
	private StartingPositions startingPositions;
	private RotationSystem rotationSystem;
	private Ghosting ghosting;
	
	public DefaultEngineConfiguration() {
		PropertiesCustomization pc = new PropertiesCustomization(getClass().getResource("default.properties"));
		shapeColors = pc;
		startingPositions = pc;
		rotationSystem = pc;
		ghosting = pc;
	}
	
	@Override
	public ShapeColors getShapeColors() {
		return shapeColors;
	}

	@Override
	public RotationSystem getRotationSystem() {
		return rotationSystem;
	}

	@Override
	public StartingPositions getStartingPositions() {
		return startingPositions;
	}

	@Override
	public Ghosting getGhosting() {
		return ghosting;
	}

	public void setShapeColors(ShapeColors shapeColors) {
		this.shapeColors = shapeColors;
	}

	public void setStartingPositions(StartingPositions startingPositions) {
		this.startingPositions = startingPositions;
	}

	public void setRotationSystem(RotationSystem rotationSystem) {
		this.rotationSystem = rotationSystem;
	}

	public void setGhosting(Ghosting ghosting) {
		this.ghosting = ghosting;
	}
}
