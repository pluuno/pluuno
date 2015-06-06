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
	}
	
	private Field field;
	private Long xyshape;
	private long block;
	private Long held;
	
	private EngineConfiguration config = DefaultEngineConfiguration.get();
	private Counts counts = new Counts();
	
	private boolean over;
	
	public Engine(Field field) {
		this.field = Objects.requireNonNull(field);
	}
	
	public void perform(Command command) {
		if(xyshape == null)
			return;
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
	}
	
	public void spawn(ShapeType type) {
		counts.gravityFrameOffset = counts.frameCount;
		int x = config.getStartingPositions().startingX(type, this);
		int y = config.getStartingPositions().startingY(type, this);
		Orientation orientation = config.getStartingPositions().startingOrientation(type, this);
		Shape shape = type.getShape(orientation);
		setXYShape(XYShapes.of(shape, x, y, ++counts.currentXYShapeID));
	}

	public void lock() {
		if(xyshape == null)
			return;
		Shape shape = XYShapes.shape(xyshape);
		field.blit(xyshape, Blocks.of(
				Blocks.FLAG_SOLID,
				config.getShapeColors().getInactiveColor(shape, this),
				shape.getId()));
		setXYShape(null);
	}
	
	public void reset() {
		field.clear();
		setXYShape(null);
		counts = new Counts();
		over = false;
	}
	
	public long getBlock(int x, int y) {
		if(xyshape != null) {
			int sx = XYShapes.x(xyshape);
			int sy = XYShapes.y(xyshape);
			if(x >= sx && x - sx < Shape.MAX_DIM && y >= sy && y - sy < Shape.MAX_DIM) {
				long m = XYShapes.shape(xyshape).getSplitMask()[y - sy];
				if((m & (1L << (x - sx))) != 0)
					return block;
			}
		}
		return field.getBlock(x, y);
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
					config.getShapeColors().getActiveColor(shape, this), 
					shape.getId());
		} else
			block = 0;
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
