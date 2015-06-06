package org.pluuno.core.customize;

import org.pluuno.core.play.Randomizer;


public class DefaultEngineConfiguration implements EngineConfiguration {
	private ShapeColors shapeColors;
	private StartingPositions startingPositions;
	private RotationSystem rotationSystem;
	private Ghosting ghosting;
	private Delays shiftDelays;
	private Randomizer randomizer;
	
	public DefaultEngineConfiguration() {
		PropertiesCustomization pc = new PropertiesCustomization(getClass().getResource("default.properties"));
		shapeColors = pc;
		startingPositions = pc;
		rotationSystem = pc;
		ghosting = pc;
		shiftDelays = pc;
		randomizer = pc;
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

	@Override
	public Delays getDelays() {
		return shiftDelays;
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
	
	public void setShiftDelays(Delays shiftDelays) {
		this.shiftDelays = shiftDelays;
	}
	
	@Override
	public Randomizer getRandomizer() {
		return randomizer;
	}
	
	public void setRandomizer(Randomizer randomizer) {
		this.randomizer = randomizer;
	}
}
