package org.pluuno.swing;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import javax.swing.JPanel;

import org.pluuno.core.Blocks;
import org.pluuno.core.Field;

public class FieldPanel extends JPanel {
	private static final long serialVersionUID = 0;
	
	private Field field;
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
			Color c = Blocks.color(field.getBlock(x, y));
			g.setColor(c);
			g.fillRect(0, 0, getWidth(), getHeight());
			if(y == 0 && bufferHeight > 0) {
				g.setColor(Color.WHITE);
				g.drawLine(0, 0, getWidth(), 0);
			}
		}
	}
	
	public FieldPanel(Field field, int bufferHeight) {
		this.field = field;
		this.bufferHeight = bufferHeight;
		setLayout(new GridLayout(field.getFieldHeight() + bufferHeight, field.getWidth()));
		
		for(int y = -bufferHeight; y < field.getFieldHeight(); y++) {
			for(int x = 0; x < field.getWidth(); x++) {
				add(new BlockPanel(x, y));
			}
		}
		
		setDoubleBuffered(true);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setComposite(AlphaComposite.Clear);
		g2.fillRect(0, 0, getWidth(), getHeight());

		g2 = (Graphics2D) g.create();
		g2.setColor(getBackground());
		g2.fillRect(0, 0, getWidth(), getHeight());
	}
}
