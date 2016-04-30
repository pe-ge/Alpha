package com.pege.alpha.level.tile;

import com.pege.alpha.graphics.Sprite;

public class HedgeTile extends Tile {

	public HedgeTile(Sprite sprite) {
		super(sprite);
	}
	
	public boolean solid() {
		return true;
	}

}
