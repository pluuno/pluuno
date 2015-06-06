package org.pluuno.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.pluuno.core.Field;
import org.pluuno.core.ShapeType;
import org.pluuno.core.XYShapes;
import org.pluuno.core.play.Engine;

public class EnginePanelTest {
	private static class RandomJFrame extends JFrame {
		public RandomJFrame() {
			super(EnginePanelTest.class.getName());
			setRootPaneCheckingEnabled(true);
		}
	}
	
	public static void main(String[] args) {
		Field field = new Field();
		Engine engine = new Engine(field);
		engine.spawn(ShapeType.T);
		
		EnginePanel fp = new EnginePanel(engine, 4);
		JFrame frame = new RandomJFrame();
		JPanel content = new JPanel(new BorderLayout()) {
			private Random rnd = new Random();
			@Override
			protected void paintComponent(Graphics g) {
				for(int y = 0; y < getHeight(); y++) {
					for(int x = 0; x < getWidth(); x++) {
						g.setColor(new Color(rnd.nextInt() & 0xFFFFFF));
						g.drawRect(x, y, 1, 1);
					}
				}
			}
		};
		content.setOpaque(true);
		
		frame.setContentPane(content);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(fp, BorderLayout.CENTER);
		frame.pack();
		frame.setSize(10*40, 24*40);
		frame.setVisible(true);
		
	}
}
