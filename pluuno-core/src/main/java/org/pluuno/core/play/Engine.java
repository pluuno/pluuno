package org.pluuno.core.play;

import java.util.Objects;

import org.pluuno.core.Field;
import org.pluuno.core.Orientation;
import org.pluuno.core.ShapeType;
import org.pluuno.core.XYShapes;

public class Engine {
	private Field field;
	private Long xyshape;
	private ShapeType held;
	
	private EngineConfiguration config;
	
	private int currentXYShapeID;
	private long frameCount = 0;
	private long moveCount = 0;
	private long gravityFrameOffset = 0;
	
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
				moveCount++;
			}
			break;
		case SHIFT_RIGHT: 
			nxy = XYShapes.shifted(xyshape, 1, 0);
			if(!field.intersects(nxy)) {
				xyshape = nxy;
				moveCount++;
			}
			break;
		case SHIFT_DOWN: 
			nxy = XYShapes.shifted(xyshape, 0, 1);
			if(!field.intersects(nxy)) {
				xyshape = nxy;
				moveCount++;
			}
			break;
		case SHIFT_LEFT: 
			nxy = XYShapes.shifted(xyshape, -1, 0);
			if(!field.intersects(nxy)) {
				xyshape = nxy;
				moveCount++;
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
				moveCount++;
			break;
		case SOFT_SHIFT_RIGHT:
			was = xyshape;
			nxy = XYShapes.shifted(xyshape, 1, 0);
			while(!field.intersects(nxy)) {
				xyshape = nxy;
				nxy = XYShapes.shifted(nxy, 1, 0);
			}
			if(was != xyshape)
				moveCount++;
			break;
		case SOFT_SHIFT_DOWN:
			was = xyshape;
			nxy = XYShapes.shifted(xyshape, 0, 1);
			while(!field.intersects(nxy)) {
				xyshape = nxy;
				nxy = XYShapes.shifted(nxy, 0, 1);
			}
			if(was != xyshape)
				moveCount++;
			break;
		case SOFT_SHIFT_LEFT:
			was = xyshape;
			nxy = XYShapes.shifted(xyshape, -1, 0);
			while(!field.intersects(nxy)) {
				xyshape = nxy;
				nxy = XYShapes.shifted(nxy, -1, 0);
			}
			if(was != xyshape)
				moveCount++;
			break;
		case HARD_SHIFT_UP:
			nxy = XYShapes.shifted(xyshape, 0, -1);
			while(!field.intersects(nxy)) {
				xyshape = nxy;
				nxy = XYShapes.shifted(nxy, 0, -1);
			}
			moveCount++;
			lock();
			break;
		case HARD_SHIFT_RIGHT:
			nxy = XYShapes.shifted(xyshape, 1, 0);
			while(!field.intersects(nxy)) {
				xyshape = nxy;
				nxy = XYShapes.shifted(nxy, 1, 0);
			}
			moveCount++;
			lock();
			break;
		case HARD_SHIFT_DOWN:
			nxy = XYShapes.shifted(xyshape, 0, 1);
			while(!field.intersects(nxy)) {
				xyshape = nxy;
				nxy = XYShapes.shifted(nxy, 0, 1);
			}
			moveCount++;
			lock();
			break;
		case HARD_SHIFT_LEFT:
			nxy = XYShapes.shifted(xyshape, -1, 0);
			while(!field.intersects(nxy)) {
				xyshape = nxy;
				nxy = XYShapes.shifted(nxy, -1, 0);
			}
			moveCount++;
			lock();
			break;
		case ROTATE_CLOCKWISE:
			xyshape = config.getRotationSystem().rotateClockwise(xyshape, field);
			moveCount++;
			break;
		case ROTATE_COUNTERCLOCKWISE:
			xyshape = config.getRotationSystem().rotateCounterclockwise(xyshape, field);
			moveCount++;
			break;
		case HOLD:
			ShapeType wasHeld = held;
			held = XYShapes.shape(xyshape).getType();
			xyshape = null;
			if(wasHeld != null)
				spawn(wasHeld);
			moveCount++;
			break;
		}
	}
	
	public void spawn(ShapeType type) {
		gravityFrameOffset = frameCount;
		int x = config.getStartingPositions().startingX(type);
		int y = config.getStartingPositions().startingY(type);
		Orientation orientation = config.getStartingPositions().startingOrientation(type);
		xyshape = XYShapes.of(type.getShape(orientation), x, y, ++currentXYShapeID);
	}

	public void lock() {
		if(xyshape == null)
			return;
		field.blit(xyshape, config.getShapeColors().getColor(XYShapes.shape(xyshape)));
		xyshape = null;
	}
	
	public void reset() {
		field.clear();
		xyshape = null;
		frameCount = 0;
		moveCount = 0;
		gravityFrameOffset = 0;
		currentXYShapeID = 0;
		over = false;
	}
	
	public Field getField() {
		return field;
	}

	public Long getXyshape() {
		return xyshape;
	}

	public void setXyshape(Long xyshape) {
		this.xyshape = xyshape;
	}
	
	public long getFrameCount() {
		return frameCount;
	}
	
	public long getMoveCount() {
		return moveCount;
	}
	
	public long getGravityFrameOffset() {
		return gravityFrameOffset;
	}
	
	public void setGravityFrameOffset(long gravityFrameOffset) {
		this.gravityFrameOffset = gravityFrameOffset;
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
}
