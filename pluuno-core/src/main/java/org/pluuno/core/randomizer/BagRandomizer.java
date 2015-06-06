package org.pluuno.core.randomizer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.pluuno.core.ShapeType;
import org.pluuno.core.play.Randomizer;

public class BagRandomizer implements Randomizer {
	private Random rnd;
	private List<ShapeType> bag = new ArrayList<>();
	
	public BagRandomizer(long seed) {
		rnd = new Random(seed);
		
		List<ShapeType> nbag = new ArrayList<>();
		nbag.add(ShapeType.I);
		nbag.add(ShapeType.J);
		nbag.add(ShapeType.L);
		nbag.add(ShapeType.T);
		
		while(nbag.size() > 0)
			bag.add(nbag.remove(rnd.nextInt(nbag.size())));
	}

	@Override
	public ShapeType next(int nextShapeID) {
		ShapeType type = bag.remove(0);
		if(bag.size() == 0) {
			List<ShapeType> nbag = new ArrayList<>(Arrays.asList(ShapeType.TYPE_VALUES));
			while(nbag.size() > 0)
				bag.add(nbag.remove(rnd.nextInt(nbag.size())));
		}
		return type;
	}

}
