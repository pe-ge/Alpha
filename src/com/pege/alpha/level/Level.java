package com.pege.alpha.level;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;

import com.pege.alpha.entity.Entity;
import com.pege.alpha.entity.mob.Dummy;
import com.pege.alpha.entity.mob.Player;
import com.pege.alpha.graphics.Screen;
import com.pege.alpha.graphics.Sprite;
import com.pege.alpha.level.tile.Tile;

public class Level {
	
	private int width, height;
	private int[] tiles;
	
	private List<Entity> entities = new ArrayList<Entity>();
	private List<Entity> entitiesToBeAdded = new ArrayList<Entity>();
	private Player player;
	
	public static Level spawn = new Level("/levels/level1.png");
	
	public Level(String path) {
		loadLevel(path);
		
		TileCoordinate coordinate = new TileCoordinate(10, 2);
		addEntity(new Dummy(coordinate, 50));
	}
	
	public Player getPlayer() {
		if (player == null) {
			for (Entity e : entities) {
				if (e instanceof Player) {
					player = (Player)e;
					break;
				}
			}
		}
		return player;
	}

	protected void loadLevel(String path) {
		try {
			BufferedImage image = ImageIO.read(Level.class.getResource(path));
			width = image.getWidth();
			height = image.getWidth();
			tiles = new int[width * height];
			image.getRGB(0, 0, width, height, tiles, 0, width);
		} catch (IOException e) {
			System.err.println("Could not load level file. Path does not exist: " + path);
			e.printStackTrace();
		}
		
		//randomize grass
		for (int i = 0; i < tiles.length; i++) {
			if (tiles[i] == Tile.COLOUR_GRASS1 && Math.random() < 0.5) {
				tiles[i] = Tile.COLOUR_GRASS2;
			}
		}
	}
	
	public void update() {
		for (Iterator<Entity> iterator = entities.iterator(); iterator.hasNext();) {
			Entity e = iterator.next();
			e.update();
			if (e.removed()) {
				iterator.remove();
			}
		}
		entities.addAll(entitiesToBeAdded);
		entitiesToBeAdded.clear();
	}
	
	public void render(int xScroll, int yScroll, Screen screen) {
		screen.setOffset(xScroll, yScroll);
		int x0 = xScroll >> 4;
		int x1 = (xScroll + screen.width + 16) >> 4;
		int y0 = yScroll >> 4;
		int y1 = (yScroll + screen.height + 16) >> 4;
		
		for (int y = y0; y < y1; y++) {
			for (int x = x0; x < x1; x++) {
				getTile(x, y).render(x, y, screen);
			}
		}
		
		for (Entity e : entities) {
			e.render(screen);
		}
	}
	
	public void addEntity(Entity e) {
		e.setLevel(this);
		entitiesToBeAdded.add(e);
	}
	
	public Tile getTile(int x, int y) {
		if (x < 0 || x >= width || y < 0 || y >= height) return Tile.voidTile;
		if (tiles[x + y * width] == Tile.COLOUR_GRASS1) return Tile.grassTile1;
		if (tiles[x + y * width] == Tile.COLOUR_GRASS2) return Tile.grassTile2;
		if (tiles[x + y * width] == Tile.COLOUR_FLOWER) return Tile.flowerTile;
		if (tiles[x + y * width] == Tile.COLOUR_ROCK) return Tile.rockTile;
		if (tiles[x + y * width] == Tile.COLOUR_WOOD) return Tile.woodTile;
		return Tile.voidTile;
	}
	
	public boolean tileCollision(Entity e, double dx, double dy) {
		Sprite s = e.getSprite();
		if (s == null) {
			return false;
		}
		
		int size = s.getEntitySize();
		int xOffset = s.getxOffset();
		int yOffset = s.getyOffset();
		boolean solid = false;
		for (int c = 0; c < 4; c++) { //4 corners of sprite
			int xt = ((int)(e.getX() + dx) - c % 2 * size + xOffset) >> 4;
			int yt = ((int)(e.getY() + dy) - c / 2 * size + yOffset) >> 4;
			solid |= getTile(xt, yt).solid();
		}
		return solid;
	}
}
