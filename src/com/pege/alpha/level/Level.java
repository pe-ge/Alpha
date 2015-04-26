package com.pege.alpha.level;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;

import com.pege.alpha.entity.Entity;
import com.pege.alpha.entity.mob.Dummy;
import com.pege.alpha.entity.mob.Mob;
import com.pege.alpha.entity.mob.player.Player;
import com.pege.alpha.entity.projectile.Projectile;
import com.pege.alpha.graphics.Screen;
import com.pege.alpha.graphics.Sprite;
import com.pege.alpha.level.tile.Tile;
import com.pege.alpha.network.Client;

public class Level {
	
	private int width, height;
	private int[] tiles;
	
	private List<Entity> entities = new ArrayList<Entity>();
	private List<Entity> entitiesToBeAdded = new ArrayList<Entity>();
	private List<Mob> mobs = new ArrayList<Mob>();
	private Player player;
	
	public static Level spawn = new Level("/levels/level1.png");
	
	public Level(String path) {
		loadLevel(path);
	}
	
	public Player getPlayer() {
		if (player == null) {
			for (Mob e : mobs) {
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
	
	public void notify(Entity e) {
		Client.getClient().send(e);
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
		if (e instanceof Mob) mobs.add((Mob)e);
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
	
	public boolean collisionEntityTile(Entity e, double dx, double dy) {
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
	
	public Mob collisionMobParticle(Projectile p, double dx, double dy) {
		for (Mob m : mobs) {
			if (collisionMobParticle(m, p, dx, dy)) {
				return m;
			}
		}
		
		return null;
	}
	
	private boolean collisionMobParticle(Mob m, Projectile p, double dx, double dy) {
		if (m.getSprite() == null || p.getSprite() == null || p.getOwner() == m) {
			return false;
		}
		
		double x1 = m.getX() - 2;
		double y1 = m.getY() - 8;
		double w1 = m.getSprite().SPRITE_SIZE - 20;
		double h1 = m.getSprite().SPRITE_SIZE - 4;
		
		double x2 = p.getX() + dx + 4;
		double y2 = p.getY() + dy + 4;
		double w2 = p.getSprite().getEntitySize();
		double h2 = p.getSprite().getEntitySize();
		
		return x1 < x2 + w2 && x1 + w1 > x2 && y1 < y2 + h2 && y1 + h1 > y2;  
	}
}
