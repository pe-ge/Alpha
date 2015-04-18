package com.pege.alpha.graphics;

public class Sprite {
	
	public final int SPRITE_SIZE; //size of whole sprite in spritesheet
	public int[] pixels;
	
	private int entitySize; //size of entity in pixels
	private int xOffset; //distance in sprite from left corner (in pixels)
	private int yOffset; //distance in sprite from upper corner (in pixels)
	
	private SpriteSheet spriteSheet;
	
	
	public Sprite(int spriteSize, int x, int y, SpriteSheet spriteSheet) {
		this.SPRITE_SIZE = spriteSize;
		this.pixels = new int[SPRITE_SIZE * SPRITE_SIZE];
		this.spriteSheet = spriteSheet;
		load(x * spriteSize, y * spriteSize);
	}
	
	public Sprite(int spriteSize, int x, int y, SpriteSheet spriteSheet, int entitySize, int xOffset, int yOffset) {
		this(spriteSize, x, y, spriteSheet);
		this.entitySize = entitySize;
		this.xOffset = xOffset;
		this.yOffset = yOffset;
	}
	
	public Sprite(int spriteSize, int colour) {
		this.SPRITE_SIZE = spriteSize;
		this.entitySize = spriteSize;
		this.pixels = new int[SPRITE_SIZE * SPRITE_SIZE];
		this.xOffset = 0;
		this.yOffset = 0;
		setColour(colour);
	}

	private void setColour(int colour) {
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = colour;
		}
		
	}

	private void load(int posX, int posY) {
		for (int y = 0; y < SPRITE_SIZE; y++) {
			for (int x = 0; x < SPRITE_SIZE; x++) {
				pixels[x + y * SPRITE_SIZE] = spriteSheet.pixels[(x + posX) + (y + posY) * spriteSheet.SIZE];
			}
		}
	}
	
	public int getEntitySize() {
		return entitySize;
	}

	public void setEntitySize(int entitySize) {
		this.entitySize = entitySize;
	}

	public int getxOffset() {
		return xOffset;
	}

	public void setxOffset(int xOffset) {
		this.xOffset = xOffset;
	}

	public int getyOffset() {
		return yOffset;
	}

	public void setyOffset(int yOffset) {
		this.yOffset = yOffset;
	}
}
