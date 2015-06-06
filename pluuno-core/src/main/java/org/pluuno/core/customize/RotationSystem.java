package org.pluuno.core.customize;

import org.pluuno.core.play.Engine;

public interface RotationSystem {
	public long rotateClockwise(long xyshape, Engine engine);
	public long rotateCounterclockwise(long xyshape, Engine engine);
}
