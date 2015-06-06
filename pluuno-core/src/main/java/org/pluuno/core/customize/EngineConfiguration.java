package org.pluuno.core.customize;

import org.pluuno.core.play.Randomizer;

public interface EngineConfiguration {

	public ShapeColors getShapeColors();
	public RotationSystem getRotationSystem();
	public StartingPositions getStartingPositions();
	public Ghosting getGhosting();
	public Delays getDelays();
	public Randomizer getRandomizer();
}