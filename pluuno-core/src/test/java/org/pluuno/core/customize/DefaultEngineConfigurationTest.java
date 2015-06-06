package org.pluuno.core.customize;

import org.junit.Before;
import org.junit.Test;
import org.pluuno.core.Field;
import org.pluuno.core.ShapeType;
import org.pluuno.core.XYShapes;
import org.pluuno.core.play.Engine;

public class DefaultEngineConfigurationTest {
	private DefaultEngineConfiguration config = new DefaultEngineConfiguration();
	
	private Engine engine;
	
	@Before
	public void before() {
		engine = new Engine(new Field());
	}
	
	@Test
	public void testRotateClockwise() {
		config.getRotationSystem().rotateClockwise(XYShapes.of(ShapeType.S.getRight(), -1, 0, 0), engine);
	}

	@Test
	public void testRotateCounterclockwise() {
		config.getRotationSystem().rotateCounterclockwise(XYShapes.of(ShapeType.S.getRight(), -1, 0, 0), engine);
	}
}
