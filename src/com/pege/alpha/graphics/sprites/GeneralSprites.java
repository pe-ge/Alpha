package com.pege.alpha.graphics.sprites;

import com.pege.alpha.graphics.Sprite;
import com.pege.alpha.graphics.SpriteSheet;

public class GeneralSprites {
	
	public static Sprite grassSprite1 = new Sprite(16, 0, 0, SpriteSheet.tiles);
	public static Sprite grassSprite2 = new Sprite(16, 0, 1, SpriteSheet.tiles);
	public static Sprite flowerSprite = new Sprite(16, 1, 0, SpriteSheet.tiles);
	public static Sprite rockSprite = new Sprite(16, 2, 0, SpriteSheet.tiles);
	public static Sprite woodSprite = new Sprite(16, 3, 0, SpriteSheet.tiles);
	public static Sprite voidSprite = new Sprite(16, 0x120a0ff);
	
	public static Sprite projectile = new Sprite(16, 0, 0, SpriteSheet.projectiles, 5, 6, 6);
	
	public static Sprite redParticle = new Sprite(2, 0xfa0000);
	public static Sprite blackParticle = new Sprite(1, 0);
}
