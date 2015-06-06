package org.pluuno.swing;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import org.pluuno.core.play.Command;
import org.pluuno.core.play.Engine;
import org.pluuno.core.play.EngineEvent;
import org.pluuno.core.play.EngineListener;

public class InputListener implements KeyListener, EngineListener {

	private Engine engine;
	private volatile Command command = Command.NO_ACTION;
	private volatile long dasCount;
	private boolean[] held = new boolean[KeyEvent.VK_CONTEXT_MENU];
	
	public InputListener(Engine engine) {
		this.engine = engine;
	}
	
	@Override
	public void commandPerformed(EngineEvent e) {
	}

	@Override
	public void commandNotPerformed(EngineEvent e) {
	}

	@Override
	public void shapeLocked(EngineEvent e) {
	}

	private synchronized void perform(boolean tick, boolean dasOnly) {
		switch(command) {
		case NO_ACTION:
			break;
		case SHIFT_UP:
			if(dasCount >= engine.getConfig().getDelays().getDASUp())
				engine.perform(Command.SOFT_SHIFT_UP);
			else if(dasCount == 0 && !dasOnly)
				engine.perform(command);
			if(tick)
				dasCount++;
			break;
		case SHIFT_RIGHT:
			if(dasCount >= engine.getConfig().getDelays().getDASRight())
				engine.perform(Command.SOFT_SHIFT_RIGHT);
			else if(dasCount == 0 && !dasOnly)
				engine.perform(command);
			if(tick)
				dasCount++;
			break;
		case SHIFT_DOWN:
			if(dasCount >= engine.getConfig().getDelays().getDASDown())
				engine.perform(Command.SOFT_SHIFT_DOWN);
			else if(dasCount == 0 && !dasOnly)
				engine.perform(command);
			if(tick)
				dasCount++;
			break;
		case SHIFT_LEFT:
			if(dasCount >= engine.getConfig().getDelays().getDASLeft())
				engine.perform(Command.SOFT_SHIFT_LEFT);
			else if(dasCount == 0 && !dasOnly)
				engine.perform(command);
			if(tick)
				dasCount++;
			break;
		case HARD_SHIFT_UP:
		case HARD_SHIFT_RIGHT:
		case HARD_SHIFT_DOWN:
		case HARD_SHIFT_LEFT:
		case HOLD:
			if(dasOnly)
				return;
			engine.perform(command);
			command = Command.NO_ACTION;
			break;
		case ROTATE_CLOCKWISE:
		case ROTATE_COUNTERCLOCKWISE:
			engine.perform(command);
			command = Command.NO_ACTION;
			break;
		}
	}
	
	@Override
	public void shapeSpawned(EngineEvent e) {
		perform(false, true);
	}

	@Override
	public void gameOver(EngineEvent e) {
	}

	@Override
	public void gameReset(EngineEvent e) {
	}

	@Override
	public void clockTicked(EngineEvent e) {
		perform(true, false);
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public synchronized void keyPressed(KeyEvent e) {
		int kc = e.getKeyCode();
		if(kc < 0 || kc > held.length || held[kc])
			return;
		held[kc] = true;
		dasCount = 0;
		switch(e.getKeyCode()) {
		case KeyEvent.VK_UP:
			command = Command.SHIFT_UP;
			break;
		case KeyEvent.VK_RIGHT:
			command = Command.SHIFT_RIGHT;
			break;
		case KeyEvent.VK_DOWN:
			command = Command.SHIFT_DOWN;
			break;
		case KeyEvent.VK_LEFT:
			command = Command.SHIFT_LEFT;
			break;
		case KeyEvent.VK_Z:
			command = Command.ROTATE_COUNTERCLOCKWISE;
			break;
		case KeyEvent.VK_X:
			command = Command.ROTATE_CLOCKWISE;
			break;
		case KeyEvent.VK_SPACE:
			command = Command.HOLD;
			break;
		}
	}

	@Override
	public synchronized void keyReleased(KeyEvent e) {
		int kc = e.getKeyCode();
		if(kc < 0 || kc > held.length || !held[kc])
			return;
		held[kc] = false;
		command = Command.NO_ACTION;
		dasCount = 0;
	}

}
