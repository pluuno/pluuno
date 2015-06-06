package org.pluuno.swing;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;

import javax.swing.JPanel;

import org.pluuno.core.Blocks;
import org.pluuno.core.Field;
import org.pluuno.core.play.Engine;

public class EnginePanel extends JPanel {
	private static final long serialVersionUID = 0;
	
	private Engine engine;
	private int bufferHeight;
	
	private class BlockPanel extends JPanel {
		private static final long serialVersionUID = 0;
		
		private int x;
		private int y;
		
		public BlockPanel(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
		@Override
		protected void paintComponent(Graphics g) {
			long block = engine.getBlock(x, y);
			Color c = engine.getConfig().getShapeColors().getColor(Blocks.flags(block), Blocks.shapeId(block), engine);
			g.setColor(c);
			g.fillRect(0, 0, getWidth(), getHeight());
		}
	}
	
	public EnginePanel(Engine engine, int bufferHeight) {
		this.engine = engine;
		this.bufferHeight = bufferHeight;
		Field f = engine.getField();
		setLayout(new GridLayout(f.getFieldHeight() + bufferHeight, f.getWidth()));
		
		for(int y = -bufferHeight; y < f.getFieldHeight(); y++) {
			for(int x = 0; x < f.getWidth(); x++) {
				add(new BlockPanel(x, y));
			}
		}
		
		setDoubleBuffered(true);
		setOpaque(false);
	}
}
