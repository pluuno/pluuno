package org.pluuno.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

import javax.swing.JFrame;
import org.pluuno.core.Field;
import org.pluuno.core.ShapeType;
import org.pluuno.core.play.Engine;

public class EnginePanelTest {
	public static void main(String[] args) {
		Field field = new Field();
		Engine engine = new Engine(field);
		engine.spawn(ShapeType.T);
		
		EnginePanel fp = new EnginePanel(engine, 4) {
			private static final long serialVersionUID = 0;
			
			private Random rnd = new Random();
			@Override
			protected void paintComponent(Graphics g) {
				for(int i = 0; i < getWidth() * getHeight() / 100; i++) {
					g.setColor(new Color(rnd.nextInt() & 0xFFFFFF));
					g.fillRect(rnd.nextInt(getWidth() - 10), rnd.nextInt(getHeight() - 10), 10, 10);
				}
			}
		};
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(fp, BorderLayout.CENTER);
		frame.pack();
		frame.setSize(10*40, 24*40);
		frame.setVisible(true);
		
	}
}
