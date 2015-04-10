package com.pege.alpha.graphics;

public class Screen {
	
	public int width;
	public int height;
	public int[] pixels;
	
	public int xOffset, yOffset;
	
	private int TRANSPARENT_COLOUR = 0xffff00ff;
	
	public Screen(int width, int height) {
		this.width = width;
		this.height = height;
		
		pixels = new int[width * height]; 
	}
	
	public void clear() {
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = 0;
		}
	}
	
	public void renderSprite(int xp, int yp, Sprite sprite) {
		xp -= xOffset;
		yp -= yOffset;
		
		int size = sprite.SPRITE_SIZE;
		
		for (int y = 0; y < size; y++) {
			int ya = y + yp;
			for (int x = 0; x < size; x++) {
				int xa = x + xp;
				if (xa < -size || xa >= width || ya < 0 || ya >= height) break;
				if (xa < 0) xa = 0;
				
				int colour = sprite.pixels[x + y * size];
				if (colour != TRANSPARENT_COLOUR) {
					pixels[xa + ya * width] = sprite.pixels[x + y * size];
				}
			}
		}
	}
	
	public void setOffset(int xOffset, int yOffset) {
		this.xOffset = xOffset;
		this.yOffset = yOffset;
	}
}
