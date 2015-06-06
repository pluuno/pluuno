package org.pluuno.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.pluuno.core.Blocks;
import org.pluuno.core.Field;
import org.pluuno.core.play.Engine;
import org.pluuno.core.play.FieldListener;

public class EnginePanel extends JPanel implements FieldListener {
	private static final long serialVersionUID = 0;
	
	private Engine engine;
	private int bufferHeight;
	private Image backgroundImage;
	
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
			Rectangle r = getBounds();
			g.drawImage(
					backgroundImage,
					0, 0, r.width, r.height,
					r.x, r.y, r.x + r.width, r.y + r.height,
					null);
			long block = engine.getBlock(x, y);
			int flags = Blocks.flags(block);
			short shapeId = Blocks.shapeId(block);
			Color c = engine.getConfig().getShapeColors().getColor(flags, shapeId, engine);
			g.setColor(c);
			g.fillRect(0, 0, getWidth(), getHeight());
			if((flags & Blocks.FLAG_ACTIVE) != 0) {
				c = engine.getConfig().getShapeColors().getColor(
						Blocks.FLAG_GHOST,
						shapeId,
						engine);
				g.setColor(c);
				g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
			}
			if((flags & Blocks.FLAG_GHOST) != 0) {
				c = engine.getConfig().getShapeColors().getColor(
						Blocks.FLAG_ACTIVE,
						shapeId,
						engine);
				g.setColor(c);
				g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
			}
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
		
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				if(backgroundImage.getWidth(null) == getWidth() && backgroundImage.getHeight(null) == getHeight())
					return;
				backgroundImage = backgroundImage.getScaledInstance(getWidth(), getHeight(), 0);
			}
		});
	}

	public void setPreferredBlockSize(int size) {
		setPreferredSize(new Dimension(size * engine.getField().getWidth(), size * (bufferHeight + engine.getField().getFieldHeight())));
	}
	
	public Image getBackgroundImage() {
		return backgroundImage;
	}
	
	public void setBackgroundImage(Image backgroundImage) {
		this.backgroundImage = backgroundImage;
	}
	
	@Override
	public void blockModified(int x, int y) {
		if(y < -bufferHeight)
			return;
		getComponent(engine.getField().getWidth() * (y + bufferHeight) + x).repaint();
	}
}
