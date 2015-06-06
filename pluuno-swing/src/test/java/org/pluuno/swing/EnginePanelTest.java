package org.pluuno.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;
import javax.swing.Timer;

import org.pluuno.core.Field;
import org.pluuno.core.ShapeType;
import org.pluuno.core.play.Engine;

public class EnginePanelTest {
	public static void main(String[] args) {
		Field field = new Field();
		final Engine engine = new Engine(field);
		engine.spawn(ShapeType.T);
		
		EnginePanel fp = new EnginePanel(engine, 4);
		InputListener input = new InputListener(engine);
		engine.addEngineListener(input);
		fp.setFocusable(true);
		fp.setFocusCycleRoot(true);
		fp.setFocusTraversalKeysEnabled(false);
		fp.addKeyListener(input);

		final JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(fp, BorderLayout.CENTER);
		frame.pack();
		frame.setSize(10*40, 24*40);
		frame.setVisible(true);
		
		
		fp.requestFocusInWindow();
		
		Runnable tick = new Runnable() {
			@Override
			public void run() {
				try {
					engine.tick();
				} catch(Throwable t) {
					t.printStackTrace();
				}
			}
		};
		
		Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(
				tick, 
				0, 
				TimeUnit.NANOSECONDS.convert(1, TimeUnit.SECONDS) / 60, 
				TimeUnit.NANOSECONDS);
	}
}
