package org.pluuno.core.play;

import java.util.Objects;

import org.pluuno.core.Blocks;
import org.pluuno.core.Field;
import org.pluuno.core.Orientation;
import org.pluuno.core.Shape;
import org.pluuno.core.ShapeType;
import org.pluuno.core.XYShapes;
import org.pluuno.core.customize.DefaultEngineConfiguration;
import org.pluuno.core.customize.EngineConfiguration;

public class Engine {
	public class Counts {
		private int currentXYShapeID;
		private long frameCount;
		private long moveCount;
		private long holdCount;
		private long gravityFrameOffset;
		private int level;
		
		private Counts() {}
		
		public int getCurrentXYShapeID() {
			return currentXYShapeID;
		}
		public long getFrameCount() {
			return frameCount;
		}
		public long getMoveCount() {
			return moveCount;
		}
		public long getHoldCount() {
			return holdCount;
		}
		public long getGravityFrameOffset() {
			return gravityFrameOffset;
		}
		public int getLevel() {
			return level;
		}
		public void setCurrentXYShapeID(int currentXYShapeID) {
			this.currentXYShapeID = currentXYShapeID;
		}
		public void setFrameCount(long frameCount) {
			this.frameCount = frameCount;
		}
		public void setMoveCount(long moveCount) {
			this.moveCount = moveCount;
		}
		public void setHoldCount(long holdCount) {
			this.holdCount = holdCount;
		}
		public void setGravityFrameOffset(long gravityFrameOffset) {
			this.gravityFrameOffset = gravityFrameOffset;
		}
		public void setLevel(int level) {
			this.level = level;
		}
	}
	
	private Field field;
	private Long xyshape;
	private long block;
	private Long ghost;
	private long ghostBlock;
	private Long held;
	
	private EngineConfiguration config = DefaultEngineConfiguration.get();
	private Counts counts = new Counts();
	private EngineEventHelper events = new EngineEventHelper();
	
	private boolean over;
	
	public Engine(Field field) {
		this.field = Objects.requireNonNull(field);
	}
	
	public void addEngineListener(EngineListener l) {
		events.addEngineListener(l);
	}
	
	public void removeEngineListener(EngineListener l) {
		events.removeEngineListener(l);
	}
	
	public void perform(Command command) {
		if(xyshape == null || over)
			return;
		long oldMoveCount = counts.moveCount;
		switch(command) {
		case NO_ACTION: break;
		case SHIFT_UP: 
			long nxy = XYShapes.shifted(xyshape, 0, -1);
			if(!field.intersects(nxy)) {
				xyshape = nxy;
				counts.moveCount++;
			}
			break;
		case SHIFT_RIGHT: 
			nxy = XYShapes.shifted(xyshape, 1, 0);
			if(!field.intersects(nxy)) {
				xyshape = nxy;
				counts.moveCount++;
			}
			break;
		case SHIFT_DOWN: 
			nxy = XYShapes.shifted(xyshape, 0, 1);
			if(!field.intersects(nxy)) {
				xyshape = nxy;
				counts.moveCount++;
			}
			break;
		case SHIFT_LEFT: 
			nxy = XYShapes.shifted(xyshape, -1, 0);
			if(!field.intersects(nxy)) {
				xyshape = nxy;
				counts.moveCount++;
			}
			break;
		case SOFT_SHIFT_UP:
			long was = xyshape;
			nxy = XYShapes.shifted(xyshape, 0, -1);
			while(!field.intersects(nxy)) {
				xyshape = nxy;
				nxy = XYShapes.shifted(nxy, 0, -1);
			}
			if(was != xyshape)
				counts.moveCount++;
			break;
		case SOFT_SHIFT_RIGHT:
			was = xyshape;
			nxy = XYShapes.shifted(xyshape, 1, 0);
			while(!field.intersects(nxy)) {
				xyshape = nxy;
				nxy = XYShapes.shifted(nxy, 1, 0);
			}
			if(was != xyshape)
				counts.moveCount++;
			break;
		case SOFT_SHIFT_DOWN:
			was = xyshape;
			nxy = XYShapes.shifted(xyshape, 0, 1);
			while(!field.intersects(nxy)) {
				xyshape = nxy;
				nxy = XYShapes.shifted(nxy, 0, 1);
			}
			if(was != xyshape)
				counts.moveCount++;
			break;
		case SOFT_SHIFT_LEFT:
			was = xyshape;
			nxy = XYShapes.shifted(xyshape, -1, 0);
			while(!field.intersects(nxy)) {
				xyshape = nxy;
				nxy = XYShapes.shifted(nxy, -1, 0);
			}
			if(was != xyshape)
				counts.moveCount++;
			break;
		case HARD_SHIFT_UP:
			nxy = XYShapes.shifted(xyshape, 0, -1);
			while(!field.intersects(nxy)) {
				xyshape = nxy;
				nxy = XYShapes.shifted(nxy, 0, -1);
			}
			counts.moveCount++;
			lock();
			break;
		case HARD_SHIFT_RIGHT:
			nxy = XYShapes.shifted(xyshape, 1, 0);
			while(!field.intersects(nxy)) {
				xyshape = nxy;
				nxy = XYShapes.shifted(nxy, 1, 0);
			}
			counts.moveCount++;
			lock();
			break;
		case HARD_SHIFT_DOWN:
			nxy = XYShapes.shifted(xyshape, 0, 1);
			while(!field.intersects(nxy)) {
				xyshape = nxy;
				nxy = XYShapes.shifted(nxy, 0, 1);
			}
			counts.moveCount++;
			lock();
			break;
		case HARD_SHIFT_LEFT:
			nxy = XYShapes.shifted(xyshape, -1, 0);
			while(!field.intersects(nxy)) {
				xyshape = nxy;
				nxy = XYShapes.shifted(nxy, -1, 0);
			}
			counts.moveCount++;
			lock();
			break;
		case ROTATE_CLOCKWISE:
			was = xyshape;
			xyshape = config.getRotationSystem().rotateClockwise(xyshape, this);
			if(was != xyshape)
				counts.moveCount++;
			break;
		case ROTATE_COUNTERCLOCKWISE:
			was = xyshape;
			xyshape = config.getRotationSystem().rotateCounterclockwise(xyshape, this);
			if(was != xyshape)
				counts.moveCount++;
			break;
		case HOLD:
			Long wasHeld = held;
			held = xyshape;
			xyshape = null;
			if(wasHeld != null)
				spawn(XYShapes.shape(wasHeld).getType());
			counts.moveCount++;
			break;
		}
		if(oldMoveCount != counts.moveCount) {
			if(xyshape != null) {
				Shape shape = XYShapes.shape(xyshape);
				ghost = config.getGhosting().computeGhost(xyshape, this);
				ghostBlock = Blocks.of(
						Blocks.FLAG_ACTIVE | Blocks.FLAG_GHOST, 
						config.getShapeColors().getColor(Blocks.FLAG_ACTIVE | Blocks.FLAG_GHOST, shape.getId(), this), 
						shape.getId());
			} else {
				ghost = null;
				ghostBlock = 0;
			}
			events.fireCommandPerformed(command);
		}
		else if(command != Command.NO_ACTION)
			events.fireCommandNotPerformed(command);
	}
	
	public void spawn(ShapeType type) {
		if(over)
			return;
		ghost = null;
		ghostBlock = 0;
		
		counts.gravityFrameOffset = counts.frameCount;
		int x = config.getStartingPositions().startingX(type, this);
		int y = config.getStartingPositions().startingY(type, this);
		Orientation orientation = config.getStartingPositions().startingOrientation(type, this);
		Shape shape = type.getShape(orientation);
		setXYShape(XYShapes.of(shape, x, y, ++counts.currentXYShapeID));
		events.fireShapeSpawned(xyshape);
		if(field.intersects(xyshape)) {
			over = true;
			events.fireGameOver();
			return;
		}
		
		ghost = config.getGhosting().computeGhost(xyshape, this);
		ghostBlock = Blocks.of(
				Blocks.FLAG_ACTIVE | Blocks.FLAG_GHOST, 
				config.getShapeColors().getColor(Blocks.FLAG_ACTIVE | Blocks.FLAG_GHOST, shape.getId(), this), 
				shape.getId());
	}

	public void lock() {
		if(xyshape == null || over)
			return;
		long was = xyshape;
		Shape shape = XYShapes.shape(xyshape);
		field.blit(xyshape, Blocks.of(
				Blocks.FLAG_SOLID,
				config.getShapeColors().getColor(Blocks.FLAG_SOLID, shape.getId(), this),
				shape.getId()));
		setXYShape(null);
		events.fireShapeLocked(was);
	}
	
	public void reset() {
		field.clear();
		setXYShape(null);
		counts = new Counts();
		over = false;
		events.fireGameReset();
	}
	
	public long getBlock(int x, int y) {
		Long b = null;
		if(xyshape != null) {
			int sx = XYShapes.x(xyshape);
			int sy = XYShapes.y(xyshape);
			if(x >= sx && x - sx < Shape.MAX_DIM && y >= sy && y - sy < Shape.MAX_DIM) {
				long m = XYShapes.shape(xyshape).getSplitMask()[y - sy];
				if((m & (1L << (x - sx))) != 0)
					b = block;
			}
		}
		if(b == null && ghost != null) {
			int sx = XYShapes.x(ghost);
			int sy = XYShapes.y(ghost);
			if(x >= sx && x - sx < Shape.MAX_DIM && y >= sy && y - sy < Shape.MAX_DIM) {
				long m = XYShapes.shape(ghost).getSplitMask()[y - sy];
				if((m & (1L << (x - sx))) != 0)
					b = ghostBlock;
			}
		}
		if(b == null)
			b = field.getBlock(x, y);
		if(y < 0)
			b |= Blocks.FLAG_BUFFER;
		return b;
	}
	
	public Field getField() {
		return field;
	}

	public Long getXYShape() {
		return xyshape;
	}

	public void setXYShape(Long xyshape) {
		this.xyshape = xyshape;
		if(xyshape != null) {
			Shape shape = XYShapes.shape(xyshape);
			block = Blocks.of(
					Blocks.FLAG_ACTIVE, 
					config.getShapeColors().getColor(Blocks.FLAG_ACTIVE, shape.getId(), this), 
					shape.getId());
			ghost = config.getGhosting().computeGhost(xyshape, this);
			ghostBlock = Blocks.of(
					Blocks.FLAG_ACTIVE | Blocks.FLAG_GHOST, 
					config.getShapeColors().getColor(Blocks.FLAG_ACTIVE | Blocks.FLAG_GHOST, shape.getId(), this), 
					shape.getId());
		} else {
			ghost = null;
			block = 0;
			ghostBlock = 0;
		}
	}
	
	public Long getGhost() {
		return ghost;
	}
	
	public Counts getCounts() {
		return counts;
	}
	
	public boolean isOver() {
		return over;
	}

	public EngineConfiguration getConfig() {
		return config;
	}

	public void setConfig(EngineConfiguration config) {
		this.config = config;
	}

	public Long getBlock() {
		return block;
	}

	public Long getHeld() {
		return held;
	}
	
	public void setHeld(Long held) {
		this.held = held;
	}
}
