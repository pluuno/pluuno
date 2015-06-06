package org.pluuno.core.customize;


public class DefaultEngineConfiguration implements EngineConfiguration {
	private static final DefaultEngineConfiguration instance = new DefaultEngineConfiguration();
	public static DefaultEngineConfiguration get() {
		return instance;
	}
	
	private ShapeColors shapeColors;
	private StartingPositions startingPositions;
	private RotationSystem rotationSystem;
	
	private DefaultEngineConfiguration() {
		PropertiesCustomization pc = new PropertiesCustomization(getClass().getResource("default.properties"));
		shapeColors = pc;
		startingPositions = pc;
		rotationSystem = pc;
	}
	
	@Override
	public ShapeColors getShapeColors() {
		return shapeColors;
	}
	public void setShapeColors(ShapeColors shapeColors) {
		this.shapeColors = shapeColors;
	}

	@Override
	public RotationSystem getRotationSystem() {
		return rotationSystem;
	}
	public void setRotationSystem(RotationSystem rotationSystem) {
		this.rotationSystem = rotationSystem;
	}

	@Override
	public StartingPositions getStartingPositions() {
		return startingPositions;
	}
	public void setStartingPositions(StartingPositions startingPositions) {
		this.startingPositions = startingPositions;
	}
}
