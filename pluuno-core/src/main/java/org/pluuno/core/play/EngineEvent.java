package org.pluuno.core.play;

import java.util.EventObject;

public class EngineEvent extends EventObject {
	public static final int COMMAND_PERFORMED = 0;
	public static final int COMMAND_NOT_PERFORMED = 1;
	public static final int SHAPE_LOCKED = 2;
	public static final int SHAPE_SPAWNED = 3;
	public static final int GAME_OVER = 4;
	public static final int GAME_RESET = 5;
	
	private static final long serialVersionUID = 0;
	
	private int id;
	private Command command;
	private Long xyshape;
	
	public EngineEvent(Engine source, int id, Command command, Long xyshape) {
		super(source);
		this.id = id;
		this.command = command;
		this.xyshape = xyshape;
	}

	@Override
	public Engine getSource() {
		return (Engine) super.getSource();
	}
	
	public int getId() {
		return id;
	}
	
	public Command getCommand() {
		return command;
	}
	
	public Long getXYShape() {
		return xyshape;
	}
}