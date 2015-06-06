package org.pluuno.swing;

import java.awt.BorderLayout;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;
import org.pluuno.core.Field;
import org.pluuno.core.ShapeType;
import org.pluuno.core.play.Engine;

public class EnginePanelTest {
	public static void main(String[] args) {
		Field field = new Field();
		final Engine engine = new Engine(field);
		
		EnginePanel fp = new EnginePanel(engine, 4);
		InputListener input = new InputListener(engine);
		engine.addEngineListener(input);
		fp.setFocusable(true);
		fp.setFocusCycleRoot(true);
		fp.setFocusTraversalKeysEnabled(false);
		fp.addKeyListener(input);
		fp.setPreferredBlockSize(16);

		final JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(fp, BorderLayout.CENTER);
		frame.pack();
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
