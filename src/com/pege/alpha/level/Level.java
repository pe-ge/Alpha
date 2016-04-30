package com.pege.alpha.level;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.imageio.ImageIO;

import com.pege.alpha.entity.Entity;
import com.pege.alpha.entity.mob.Mob;
import com.pege.alpha.entity.mob.player.Player;
import com.pege.alpha.entity.projectile.BasicProjectile;
import com.pege.alpha.entity.projectile.Projectile;
import com.pege.alpha.graphics.Screen;
import com.pege.alpha.graphics.Sprite;
import com.pege.alpha.graphics.sprites.GeneralSprites;
import com.pege.alpha.level.tile.Tile;

public class Level {
	
	// MAP RELATED FIELDS
	private int width, height;
	private int[] tiles;
	
	// ENTITY RELATED FIELDS 
	private List<Entity> levelEntities = new ArrayList<Entity>();
	private List<Entity> entitiesToAdd = new ArrayList<Entity>();
	private List<Mob> mobs = new ArrayList<Mob>();
	private Player player;
	
	private Queue<Entity> entitiesToSend = new ConcurrentLinkedQueue<Entity>();

	// LEVELS
	public static Level level1 = new Level("/levels/svidnikREAL.png");
	
	public Level(String path) {
		try {
			BufferedImage image = ImageIO.read(Level.class.getResource(path));
			width = image.getWidth();
			height = image.getHeight();
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
		for (Iterator<Entity> iterator = levelEntities.iterator(); iterator.hasNext();) {
			Entity e = iterator.next();
			e.update();
			if (e.removed()) {
				iterator.remove(); //removes from entities list
				removeEntity(e); //removes from all others lists
			}
		}
		
		levelEntities.addAll(entitiesToAdd);
		entitiesToAdd.clear();
		
		sendPlayerPosition();
	}
	
	private void sendPlayerPosition() {
		entitiesToSend.add(getPlayer());
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
		
		for (Entity e : levelEntities) {
			e.render(screen);
		}
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
	
	public Queue<Entity> getEntitiesToSend() {
		return entitiesToSend;
	}
	
	public void addEntity(Entity e) {
		e.setLevel(this);
		entitiesToAdd.add(e);
		if (e instanceof Mob) mobs.add((Mob)e);
		
		if (e instanceof BasicProjectile) entitiesToSend.add(e);
	}
	
	private void removeEntity(Entity e) {
		if (e instanceof Mob) mobs.remove(e);
	}
	
	/*public Tile getTile(int x, int y) {
		if (x < 0 || x >= width || y < 0 || y >= height) return Tile.voidTile;
		if (tiles[x + y * width] == Tile.COLOUR_GRASS1) return Tile.grassTile1;
		if (tiles[x + y * width] == Tile.COLOUR_GRASS2) return Tile.grassTile2;
		if (tiles[x + y * width] == Tile.COLOUR_FLOWER) return Tile.flowerTile;
		if (tiles[x + y * width] == Tile.COLOUR_ROCK) return Tile.rockTile;
		if (tiles[x + y * width] == Tile.COLOUR_WOOD) return Tile.woodTile;
		return Tile.voidTile;
	}*/
	
	public Tile getTile(int x, int y) {
		if (x < 0 || x >= width || y < 0 || y >= height) return Tile.voidTile;
		
		int tile = tiles[x + y * width];
		Tile result = Tile.tiles.get(tile);
		if (result == null) {
			result = new Tile(new Sprite(16, tile)) {
			};
		}
		
		return result;
	}
	
	public boolean collisionEntityTile(Entity e, double tileX, double tileY) {
		Sprite s = e.getSprite();
		if (s == null) {
			return false;
		}
		
		int size = s.getEntitySize();
		int xOffset = s.getxOffset();
		int yOffset = s.getyOffset();
		boolean solid = false;
		for (int c = 0; c < 4; c++) { //4 corners of sprite
			int xt = ((int)(e.getX() + tileX) - c % 2 * size + xOffset) >> 4;
			int yt = ((int)(e.getY() + tileY) - c / 2 * size + yOffset) >> 4;
			solid |= getTile(xt, yt).solid();
		}
		return solid;
	}
	
	public Mob collisionMobProjectile(Projectile p, double dx, double dy) {
		for (Mob m : mobs) {
			if (collisionMobProjectile(m, p, dx, dy)) {
				return m;
			}
		}
		
		return null;
	}
	
	private boolean collisionMobProjectile(Mob m, Projectile p, double dx, double dy) {
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
