package org.pluuno.core.randomizer;

import java.util.Random;

import org.pluuno.core.ShapeType;
import org.pluuno.core.play.Randomizer;

public class MemorylessRandomizer implements Randomizer {
	private Random rnd = new Random(); 
	
	@Override
	public ShapeType next(int nextShapeID) {
		return ShapeType.TYPE_VALUES[rnd.nextInt(ShapeType.TYPE_VALUES.length)];
	}

}
