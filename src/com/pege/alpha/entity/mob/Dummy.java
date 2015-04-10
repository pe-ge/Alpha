package com.pege.alpha.entity.mob;

import com.pege.alpha.level.TileCoordinate;

public class Dummy extends Mob {
	
	public Dummy(TileCoordinate position) {
		super(position);
	}

	private int time = 0;
	private int xa = 0;
	private int ya = 0;
	
	public void update() {
		super.update();
		time++;

		if (time % (random.nextInt(50) + 30) == 0) {
			xa = random.nextInt(3) - 1;
			ya = random.nextInt(3) - 1;
			
			if (random.nextInt(3) == 0) {
				xa = 0;
				ya = 0;
			}
		}
		
		if (xa == 0 && ya == 0) {
			walking = false;
		} else {
			walking = true;
		}
		move(xa, ya);
	}

}
