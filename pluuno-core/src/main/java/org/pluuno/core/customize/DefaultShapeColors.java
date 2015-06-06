package org.pluuno.core.customize;

import java.awt.Color;

import org.pluuno.core.Blocks;
import org.pluuno.core.Shape;
import org.pluuno.core.play.Engine;

public class DefaultShapeColors implements ShapeColors {

	private static final Color TRANSPARENT = new Color(0,0,0,0);
	
	@Override
	public Color getColor(int blockFlags, int shapeId, Engine engine) {
		if((Blocks.FLAG_GHOST & blockFlags) != 0)
			return Color.WHITE;
		if(((Blocks.FLAG_ACTIVE | Blocks.FLAG_SOLID) & blockFlags) != 0)
			return Shape.of(shapeId).getType().getDefaultColor();
		if((Blocks.FLAG_WALL & blockFlags) != 0)
			return Color.BLACK;
		return TRANSPARENT;
	}


}
