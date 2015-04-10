package com.pege.alpha.level.tile;

import com.pege.alpha.graphics.Screen;
import com.pege.alpha.graphics.Sprite;
import com.pege.alpha.graphics.Sprites;

public abstract class Tile {
	
	public int x, y;
	public Sprite sprite;
	
	public static Tile grassTile1 = new GrassTile(Sprites.grassSprite1);
	public static Tile grassTile2 = new GrassTile(Sprites.grassSprite2);
	public static Tile flowerTile = new FlowerTile(Sprites.flowerSprite);
	public static Tile rockTile = new RockTile(Sprites.rockSprite);
	public static Tile woodTile = new HedgeTile(Sprites.woodSprite);
	public static Tile voidTile = new VoidTile(Sprites.voidSprite);
	
	public static final int COLOUR_GRASS = 0xff00ff00;
	public static final int COLOUR_FLOWER = 0xffffff00;
	public static final int COLOUR_ROCK = 0xffbababa;
	public static final int COLOUR_WOOD = 0xff7f7f00;

	public Tile(Sprite sprite) {
		this.sprite = sprite;
	}
	
	public void render(int x, int y, Screen screen) {
		screen.renderSprite(x << 4, y << 4, sprite);
	}
	
	public boolean solid() {
		return false;
	}
}
