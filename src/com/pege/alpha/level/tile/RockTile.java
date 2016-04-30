package com.pege.alpha.level.tile;

import com.pege.alpha.graphics.Sprite;

public class RockTile extends Tile {

	public RockTile(Sprite sprite) {
		super(sprite);
	}
	
	public boolean solid() {
		return false;
	}

}
