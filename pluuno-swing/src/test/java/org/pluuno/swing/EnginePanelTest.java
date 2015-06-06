package org.pluuno.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import org.pluuno.core.Field;
import org.pluuno.core.ShapeType;
import org.pluuno.core.play.Engine;

public class EnginePanelTest {
	public static void main(String[] args) throws Exception {
		Field field = new Field(10, 20);
		final Engine engine = new Engine(field);
		
		InputListener input = new InputListener(engine);
		engine.addEngineListener(input);

		final EnginePanel ep = new EnginePanel(engine, 4);
		ep.setBackground(Color.BLACK);
		ep.setFocusable(true);
		ep.setFocusCycleRoot(true);
		ep.setFocusTraversalKeysEnabled(false);
		ep.addKeyListener(input);
		ep.setPreferredBlockSize(16);

		FieldPanel fp = new FieldPanel(ImageIO.read(EnginePanelTest.class.getResource("DSCF0347.JPG")), 640, 480);
		fp.addEngine(ep, fp.getWidth() / 2 - ep.getPreferredSize().width / 2, fp.getHeight() / 2 - ep.getPreferredSize().height / 2);
		
		final JFrame frame = new JFrame();
		frame.setBackground(Color.BLACK);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(fp, BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);
		
		
		ep.requestFocusInWindow();
		
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
