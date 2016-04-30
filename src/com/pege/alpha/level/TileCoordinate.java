package com.pege.alpha.level;

public class TileCoordinate {
	
	private int x, y;
	private final int TILE_SIZE = 16;
	private final int SHIFT = (int)(Math.log(TILE_SIZE) / Math.log(2));
	
	public TileCoordinate(int x, int y) {
		this.x = x << SHIFT;
		this.y = y << SHIFT;
	}
	
	public int x() {
		return x;
	}
	
	public int y() {
		return y;
	}
	
	public int[] xy() {
		return new int[]{x, y};
	}
}
