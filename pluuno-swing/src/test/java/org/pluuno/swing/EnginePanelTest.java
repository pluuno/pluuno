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
	public static void main(String[] args) {
		Field field = new Field(30, 20, 20);
		final Engine engine = new Engine(field);
		
		InputListener input = new InputListener(engine);
		engine.addEngineListener(input);

		final EnginePanel fp = new EnginePanel(engine, 4);
		fp.setBackground(Color.BLACK);
		fp.setFocusable(true);
		fp.setFocusCycleRoot(true);
		fp.setFocusTraversalKeysEnabled(false);
		fp.addKeyListener(input);
		fp.setPreferredBlockSize(24);

		final JFrame frame = new JFrame();
		frame.setBackground(Color.BLACK);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(fp, BorderLayout.CENTER);
		frame.addComponentListener(new ComponentAdapter() {
			private BufferedImage img;
			{
				try {
					img = ImageIO.read(EnginePanelTest.class.getResource("DSCF0347.JPG"));
				} catch (IOException ex) {
					throw new RuntimeException(ex);
				}
			}
			@Override
			public void componentResized(ComponentEvent e) {
				fp.setBackgroundImage(img.getScaledInstance(fp.getWidth(), fp.getHeight(), 0));
			}
		});
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
