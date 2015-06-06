package org.pluuno.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;

import org.pluuno.core.Field;
import org.pluuno.core.ShapeType;
import org.pluuno.core.XYShapes;
import org.pluuno.core.play.Engine;

public class EnginePanelTest {
	public static void main(String[] args) {
		Field field = new Field();
		Engine engine = new Engine(field);
		engine.setXYShape(XYShapes.of(ShapeType.S.getUp(), 0, 0, 0));
		
		EnginePanel fp = new EnginePanel(engine, 4);
		JFrame frame = new JFrame(EnginePanelTest.class.getName());
		frame.setUndecorated(true);
		frame.setLayout(new BorderLayout());
		frame.add(fp, BorderLayout.CENTER);
		frame.pack();
		frame.setSize(100, 240);
		frame.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.exit(0);
			}
		});
		frame.setVisible(true);
		frame.setBackground(new Color(0, 0, 0, 0));
	}
}