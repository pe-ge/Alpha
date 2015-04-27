package com.pege.alpha;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

import com.pege.alpha.entity.mob.player.Player;
import com.pege.alpha.entity.mob.player.Ranger;
import com.pege.alpha.graphics.Screen;
import com.pege.alpha.input.Keyboard;
import com.pege.alpha.input.Mouse;
import com.pege.alpha.level.Level;
import com.pege.alpha.level.TileCoordinate;
import com.pege.alpha.network.Client;

public class Game extends Canvas implements Runnable {
	
	private static final long serialVersionUID = 1L;
	
	private static final int WIDTH = 300;
	private static final int HEIGHT = WIDTH / 16 * 9;
	private static final int SCALE = 3;
	public static final int HORIZONTAL_CENTER = Game.WIDTH * Game.SCALE / 2;
	public static final int VERTICAL_CENTER = Game.HEIGHT * Game.SCALE / 2;
	
	public static String title = "Alpha";
	
	private Thread mainThread;
	private JFrame frame;
	private Keyboard keyboard;
	private Mouse mouse;
	private Level level;
	private Player player;
	
	private boolean running = false;
	
	private Screen screen;
	private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB); //final view
	private int[] pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
	
	public Game() {
		Dimension size = new Dimension(WIDTH * SCALE, HEIGHT * SCALE);
		setPreferredSize(size);
		
		frame = new JFrame();
		screen = new Screen(WIDTH, HEIGHT);
		keyboard = new Keyboard();
		mouse = new Mouse();
		level = Level.spawn;
		TileCoordinate playerSpawn = new TileCoordinate(6, 2);
		player = new Ranger(playerSpawn, keyboard, mouse);
		level.addEntity(player);
		Client.getClient().setLevel(level);
		
		addKeyListener(keyboard);
		addMouseListener(mouse);
		addMouseMotionListener(mouse);
		
		frame.setResizable(false);
		frame.setTitle("Rain");
		frame.add(this);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	public synchronized void start() {
		running = true;
		mainThread = new Thread(this, "Display");
		mainThread.start();
	}
	
	public synchronized void stop() {
		running = false;
		try {
			mainThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		long lastTime = System.nanoTime();
		long timer = System.currentTimeMillis();
		final double ns = 1000000000.0 / 60.0;
		double delta = 0;
		int frames = 0;
		int updates = 0;
		requestFocus();
		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while (delta >= 1.0) {
				update();
				updates++;
				delta--;
			}
			render();
			frames++;
			
			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				frame.setTitle(title + " | " + updates + " ups, " + frames + " fps");
				updates = 0;
				frames = 0;
			}
		}
		stop();
	}

	private void update() {
		keyboard.update();
		level.update();
	}
	
	private void render() {
		BufferStrategy bufferStrategy = getBufferStrategy();
		if (bufferStrategy == null) {
			createBufferStrategy(3);
			return;
		}
		
		screen.clear();
		int xScroll = (int)player.getX() - screen.width / 2;
		int yScroll = (int)player.getY() - screen.height / 2;
		level.render(xScroll, yScroll, screen);
		
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = screen.pixels[i];
		}
		
		Graphics graphics = bufferStrategy.getDrawGraphics();
		graphics.drawImage(image, 0, 0, getWidth(), getHeight(), null);
		graphics.dispose();
		bufferStrategy.show();
	}

	public static void main(String[] args) {
		if (args.length != 2) {
			System.err.println("Usage: java Game [ip] [port]");
			return;
		}
		
		String address = args[0];
		int port = Integer.parseInt(args[1]);
		Client.initClient(address, port);
		
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
		    public void run() {
		    	Client.getClient().disconnect();
		    }
		}));
		
		Game game = new Game();
		game.start();
	}
}
