package com.pege.alpha.entity.mob.player;

import com.pege.alpha.entity.mob.Mob;
import com.pege.alpha.input.Keyboard;
import com.pege.alpha.input.Mouse;
import com.pege.alpha.level.TileCoordinate;

public abstract class Player extends Mob {
	
	protected Keyboard keyboard;
	protected Mouse mouse;
	
	public Player(TileCoordinate position, Keyboard keyboard, Mouse mouse) {
		super(position);
		this.keyboard = keyboard;
		this.mouse = mouse;
	}
	
	
	
}
