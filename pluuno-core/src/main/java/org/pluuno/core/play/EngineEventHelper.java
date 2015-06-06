package org.pluuno.core.play;

import javax.swing.event.EventListenerList;

public class EngineEventHelper {
	private Engine engine;
	private EventListenerList listeners = new EventListenerList();
	
	public EngineEventHelper(Engine engine) {
		this.engine = engine;
	}
	
	public void addEngineListener(EngineListener l) {
		listeners.add(EngineListener.class, l);
	}
	
	public void removeEngineListener(EngineListener l) {
		listeners.remove(EngineListener.class, l);
	}

	public void addFieldListener(FieldListener l) {
		listeners.add(FieldListener.class, l);
	}
	
	public void removeFieldListener(FieldListener l) {
		listeners.remove(FieldListener.class, l);
	}
	
	public void fireCommandPerformed(Command c) {
		Object[] ll = listeners.getListenerList();
		EngineEvent e = null;
		for(int i = ll.length - 2; i >= 0; i -= 2) {
			if(ll[i] == EngineListener.class) {
				if(e == null)
					e = new EngineEvent(engine, EngineEvent.COMMAND_PERFORMED, c, null);
				((EngineListener) ll[i+1]).commandPerformed(e);
			}
		}
	}

	public void fireCommandNotPerformed(Command c) {
		Object[] ll = listeners.getListenerList();
		EngineEvent e = null;
		for(int i = ll.length - 2; i >= 0; i -= 2) {
			if(ll[i] == EngineListener.class) {
				if(e == null)
					e = new EngineEvent(engine, EngineEvent.COMMAND_NOT_PERFORMED, c, null);
				((EngineListener) ll[i+1]).commandNotPerformed(e);
			}
		}
	}

	public void fireShapeLocked(Long xyshape) {
		Object[] ll = listeners.getListenerList();
		EngineEvent e = null;
		for(int i = ll.length - 2; i >= 0; i -= 2) {
			if(ll[i] == EngineListener.class) {
				if(e == null)
					e = new EngineEvent(engine, EngineEvent.SHAPE_LOCKED, null, xyshape);
				((EngineListener) ll[i+1]).shapeLocked(e);
			}
		}
	}

	public void fireShapeSpawned(Long xyshape) {
		Object[] ll = listeners.getListenerList();
		EngineEvent e = null;
		for(int i = ll.length - 2; i >= 0; i -= 2) {
			if(ll[i] == EngineListener.class) {
				if(e == null)
					e = new EngineEvent(engine, EngineEvent.SHAPE_SPAWNED, null, xyshape);
				((EngineListener) ll[i+1]).shapeSpawned(e);
			}
		}
	}

	public void fireGameOver() {
		Object[] ll = listeners.getListenerList();
		EngineEvent e = null;
		for(int i = ll.length - 2; i >= 0; i -= 2) {
			if(ll[i] == EngineListener.class) {
				if(e == null)
					e = new EngineEvent(engine, EngineEvent.GAME_OVER, null, null);
				((EngineListener) ll[i+1]).gameOver(e);
			}
		}
	}

	public void fireGameReset() {
		Object[] ll = listeners.getListenerList();
		EngineEvent e = null;
		for(int i = ll.length - 2; i >= 0; i -= 2) {
			if(ll[i] == EngineListener.class) {
				if(e == null)
					e = new EngineEvent(engine, EngineEvent.GAME_RESET, null, null);
				((EngineListener) ll[i+1]).gameReset(e);
			}
		}
	}

	public void fireClockTicked() {
		Object[] ll = listeners.getListenerList();
		EngineEvent e = null;
		for(int i = ll.length - 2; i >= 0; i -= 2) {
			if(ll[i] == EngineListener.class) {
				if(e == null)
					e = new EngineEvent(engine, EngineEvent.CLOCK_TICKED, null, null);
				((EngineListener) ll[i+1]).clockTicked(e);
			}
		}
	}
	
	public void fireBlockModified(int x, int y) {
		Object[] ll = listeners.getListenerList();
		for(int i = ll.length - 2; i >= 0; i -= 2) {
			if(ll[i] == FieldListener.class)
				((FieldListener) ll[i+1]).blockModified(x, y);
		}
	}
}
