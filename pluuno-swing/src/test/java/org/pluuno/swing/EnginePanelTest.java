package org.pluuno.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;

import javax.swing.JFrame;

import org.pluuno.core.Field;
import org.pluuno.core.ShapeType;
import org.pluuno.core.XYShapes;
import org.pluuno.core.play.Engine;

public class EnginePanelTest {
	public static void main(String[] args) {
		Field field = new Field();
		Engine engine = new Engine(field);
		engine.setXYShape(XYShapes.of(ShapeType.O.getRight(), 0, 0, 0xdeadbeef));
		
		EnginePanel fp = new EnginePanel(engine, 4);
		JFrame frame = new JFrame(EnginePanelTest.class.getName());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.add(fp, BorderLayout.CENTER);
		frame.pack();
		frame.setSize(10*40, 24*40);
		frame.setVisible(true);
	}
}
