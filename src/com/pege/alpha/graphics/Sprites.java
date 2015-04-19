package com.pege.alpha.graphics;

public class Sprites {
	
	public static Sprite grassSprite1 = new Sprite(16, 0, 0, SpriteSheet.tiles);
	public static Sprite grassSprite2 = new Sprite(16, 0, 1, SpriteSheet.tiles);
	public static Sprite flowerSprite = new Sprite(16, 1, 0, SpriteSheet.tiles);
	public static Sprite rockSprite = new Sprite(16, 2, 0, SpriteSheet.tiles);
	public static Sprite woodSprite = new Sprite(16, 3, 0, SpriteSheet.tiles);
	public static Sprite voidSprite = new Sprite(16, 0x120a0ff);
	
	public static Sprite projectile = new Sprite(16, 0, 0, SpriteSheet.projectiles, 5, 6, 6);
	
	public static Sprite redParticle = new Sprite(2, 0xfa0000);
	public static Sprite blackParticle = new Sprite(1, 0);
	
	public static Sprite[] playerUp = new Sprite[]{
		new Sprite(32, 0, 5, SpriteSheet.tiles, 15, 7, 15),
		new Sprite(32, 0, 6, SpriteSheet.tiles, 15, 7, 15),
		new Sprite(32, 0, 7, SpriteSheet.tiles, 15, 7, 15)
	};
	
	public static Sprite[] playerDown = new Sprite[]{
		new Sprite(32, 2, 5, SpriteSheet.tiles, 15, 7, 15),
		new Sprite(32, 2, 6, SpriteSheet.tiles, 15, 7, 15),
		new Sprite(32, 2, 7, SpriteSheet.tiles, 15, 7, 15)
	};
	
	public static Sprite[] playerLeft = new Sprite[]{
		new Sprite(32, 3, 5, SpriteSheet.tiles, 15, 7, 15),
		new Sprite(32, 3, 6, SpriteSheet.tiles, 15, 7, 15),
		new Sprite(32, 3, 7, SpriteSheet.tiles, 15, 7, 15)
	};
	
	public static Sprite[] playerRight = new Sprite[]{
		new Sprite(32, 1, 5, SpriteSheet.tiles, 15, 7, 15),
		new Sprite(32, 1, 6, SpriteSheet.tiles, 15, 7, 15),
		new Sprite(32, 1, 7, SpriteSheet.tiles, 15, 7, 15)
	};
}
