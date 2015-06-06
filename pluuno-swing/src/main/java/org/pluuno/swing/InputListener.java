package org.pluuno.swing;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import org.pluuno.core.play.Command;
import org.pluuno.core.play.Engine;
import org.pluuno.core.play.EngineEvent;
import org.pluuno.core.play.EngineListener;

public class InputListener implements KeyListener, EngineListener {
	private static final Command[] COMMANDS = Command.values();

	private Engine engine;
	private List<Command> lateral = new ArrayList<>();
	private List<Command> nonlateral = new ArrayList<>();
	private long[] das = new long[COMMANDS.length];

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

	@Override
	public void shapeSpawned(EngineEvent e) {
		perform();
	}

	@Override
	public void gameOver(EngineEvent e) {
	}

	@Override
	public void gameReset(EngineEvent e) {
	}
	
	public void perform() {
		for(Command c : nonlateral) {
			if(das[c.ordinal()] == 0)
				engine.perform(c);
		}
		if(lateral.size() == 1) {
			Command c = lateral.get(0);
			switch(c) {
			case NO_ACTION:
				break;
			case SHIFT_UP:
				if(das[c.ordinal()] >= engine.getConfig().getDelays().getDASUp())
					engine.perform(Command.SOFT_SHIFT_UP);
				else if(das[c.ordinal()] == 0)
					engine.perform(c);
				break;
			case SHIFT_RIGHT:
				if(das[c.ordinal()] >= engine.getConfig().getDelays().getDASRight())
					engine.perform(Command.SOFT_SHIFT_RIGHT);
				else if(das[c.ordinal()] == 0)
					engine.perform(c);
				break;
			case SHIFT_DOWN:
				if(das[c.ordinal()] >= engine.getConfig().getDelays().getDASDown())
					engine.perform(Command.SOFT_SHIFT_DOWN);
				else if(das[c.ordinal()] == 0)
					engine.perform(c);
				break;
			case SHIFT_LEFT:
				if(das[c.ordinal()] >= engine.getConfig().getDelays().getDASLeft())
					engine.perform(Command.SOFT_SHIFT_LEFT);
				else if(das[c.ordinal()] == 0)
					engine.perform(c);
				break;
			case HARD_SHIFT_UP:
			case HARD_SHIFT_RIGHT:
			case HARD_SHIFT_DOWN:
			case HARD_SHIFT_LEFT:
				if(das[c.ordinal()] == 0)
					engine.perform(c);
				break;
			}
		}
	}

	@Override
	public void clockTicked(EngineEvent e) {
		perform();
		for(Command c : lateral)
			das[c.ordinal()]++;
		for(Command c : nonlateral)
			das[c.ordinal()]++;
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	public Command commandOf(KeyEvent e) {
		switch(e.getKeyCode()) {
		case KeyEvent.VK_UP:
			return Command.SHIFT_UP;
		case KeyEvent.VK_RIGHT:
			return Command.SHIFT_RIGHT;
		case KeyEvent.VK_DOWN:
			return Command.SHIFT_DOWN;
		case KeyEvent.VK_LEFT:
			return Command.SHIFT_LEFT;
		case KeyEvent.VK_Z:
			return Command.ROTATE_COUNTERCLOCKWISE;
		case KeyEvent.VK_X:
			return Command.ROTATE_CLOCKWISE;
		case KeyEvent.VK_SPACE:
			return Command.HOLD;
		}
		return null;
	}

	@Override
	public synchronized void keyPressed(KeyEvent e) {
		Command c = commandOf(e);

		if(c == null || lateral.contains(c) || nonlateral.contains(c))
			return;

		switch(c) {
		case SHIFT_UP:
		case SHIFT_RIGHT:
		case SHIFT_DOWN:
		case SHIFT_LEFT:
		case SOFT_SHIFT_UP:
		case SOFT_SHIFT_RIGHT:
		case SOFT_SHIFT_DOWN:
		case SOFT_SHIFT_LEFT:
		case HARD_SHIFT_UP:
		case HARD_SHIFT_RIGHT:
		case HARD_SHIFT_DOWN:
		case HARD_SHIFT_LEFT:
			lateral.add(c);
			break;
		case HOLD:
		case ROTATE_CLOCKWISE:
		case ROTATE_COUNTERCLOCKWISE:
			nonlateral.add(c);
			break;
		}
		das[c.ordinal()] = 0;
	}

	@Override
	public synchronized void keyReleased(KeyEvent e) {
		Command c = commandOf(e);
		if(c == null)
			return;
		lateral.remove(c);
		nonlateral.remove(c);
	}

}
