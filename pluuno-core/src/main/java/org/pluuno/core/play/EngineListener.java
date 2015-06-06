package org.pluuno.core.play;

import java.util.EventListener;

public interface EngineListener extends EventListener {
	public void commandPerformed(EngineEvent e);
	public void commandNotPerformed(EngineEvent e);
	public void shapeLocked(EngineEvent e);
	public void shapeSpawned(EngineEvent e);
	public void gameOver(EngineEvent e);
	public void gameReset(EngineEvent e);
}
