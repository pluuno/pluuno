package org.pluuno.core.play;

import java.util.EventListener;

public interface FieldListener extends EventListener {
	public void blockModified(int x, int y);
}
