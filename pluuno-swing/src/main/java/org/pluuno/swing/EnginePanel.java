package org.pluuno.swing;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import org.pluuno.core.Blocks;
import org.pluuno.core.Field;
import org.pluuno.core.play.Engine;
import org.pluuno.core.play.FieldListener;

public class EnginePanel extends JPanel implements FieldListener {
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
			g.setColor(EnginePanel.this.getBackground());
			g.fillRect(0, 0, getWidth(), getHeight());
			Color c = engine.getConfig().getShapeColors().getColor(Blocks.flags(block), Blocks.shapeId(block), engine);
			g.setColor(c);
			g.fillRect(0, 0, getWidth(), getHeight());
		}
	}
	
	public EnginePanel(Engine engine, int bufferHeight) {
		this.engine = engine;
		this.bufferHeight = bufferHeight;
		
		engine.addFieldListener(this);
		
		Field f = engine.getField();
		setLayout(new GridLayout(f.getFieldHeight() + bufferHeight, f.getWidth()));
		
		for(int y = -bufferHeight; y < f.getFieldHeight(); y++) {
			for(int x = 0; x < f.getWidth(); x++) {
				add(new BlockPanel(x, y));
			}
		}
		
		setBackground(Color.BLACK);
		setOpaque(true);
	}

	public void setPreferredBlockSize(int size) {
		setPreferredSize(new Dimension(size * engine.getField().getWidth(), size * (bufferHeight + engine.getField().getFieldHeight())));
	}
	
	@Override
	public void blockModified(int x, int y) {
		if(y < -bufferHeight)
			return;
		getComponent(engine.getField().getWidth() * (y + bufferHeight) + x).repaint();
	}
}
