package org.pluuno.swing;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class FieldPanel extends JPanel {
	private static class Layout implements LayoutManager {

		@Override
		public void addLayoutComponent(String name, Component comp) {
			comp.setSize(comp.getPreferredSize());
		}

		@Override
		public void removeLayoutComponent(Component comp) {
		}

		@Override
		public Dimension preferredLayoutSize(Container parent) {
			return parent.getPreferredSize();
		}

		@Override
		public Dimension minimumLayoutSize(Container parent) {
			return new Dimension(1, 1);
		}

		@Override
		public void layoutContainer(Container parent) {
		}
		
	}
	
	private BufferedImage backgroundImage;
	private BufferedImage scaledBackgroundImage;
	
	public FieldPanel(BufferedImage backgroundImage, int width, int height) {
		this.backgroundImage = backgroundImage;
		setLayout(new Layout());
		setPreferredSize(new Dimension(width, height));
		setSize(width, height);
		Image scaled = FieldPanel.this.backgroundImage.getScaledInstance(width, height, 0);
		scaledBackgroundImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		scaledBackgroundImage.getGraphics().drawImage(scaled, 0, 0, null);
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				Image scaled = FieldPanel.this.backgroundImage.getScaledInstance(getWidth(), getHeight(), 0);
				scaledBackgroundImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
				scaledBackgroundImage.getGraphics().drawImage(scaled, 0, 0, null);
				for(int i = 0; i < getComponentCount(); i++) {
					Component c = getComponent(i);
					if(!(c instanceof EnginePanel))
						continue;
					EnginePanel ep = (EnginePanel) c;
					Rectangle b = ep.getBounds();
					ep.setBackgroundImage(scaledBackgroundImage.getSubimage(b.x, b.y, ep.getWidth(), ep.getHeight()));
				}
			}
		});
	}
	
	public void addEngine(final EnginePanel ep, int x, int y) {
		ep.setBackgroundImage(scaledBackgroundImage.getSubimage(x, y, ep.getPreferredSize().width, ep.getPreferredSize().height));
		add(ep);
		ep.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentMoved(ComponentEvent e) {
				Rectangle b = ep.getBounds();
				ep.setBackgroundImage(scaledBackgroundImage.getSubimage(b.x, b.y, b.width, b.height));
			}
		});
		ep.setLocation(x, y);
		ep.setSize(ep.getPreferredSize());
		MouseAdapter drag = new MouseAdapter() {
			private Point mp;
			private Point origin;
			@Override
			public void mousePressed(MouseEvent e) {
				mp = SwingUtilities.convertPoint(ep, e.getPoint(), FieldPanel.this);
				origin = ep.getLocation();
			}
			@Override
			public void mouseDragged(MouseEvent e) {
				Point p = SwingUtilities.convertPoint(ep, e.getPoint(), FieldPanel.this);
				Point l = new Point(origin.x - mp.x + p.x, origin.y - mp.y + p.y);
				l.x = Math.max(l.x, 0);
				l.y = Math.max(l.y, 0);
				l.x = Math.min(l.x, FieldPanel.this.getWidth() - ep.getWidth());
				l.y = Math.min(l.y, FieldPanel.this.getHeight() - ep.getHeight());
				ep.setLocation(l.x, l.y);
			}
		};
		ep.addMouseListener(drag);
		ep.addMouseMotionListener(drag);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		g.drawImage(scaledBackgroundImage, 0, 0, null);
	}
}
