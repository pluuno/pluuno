package org.pluuno.core.customize;

import org.pluuno.core.Field;

public interface RotationSystem {
	public long rotateClockwise(long xyshape, Field field);
	public long rotateCounterclockwise(long xyshape, Field field);
}
