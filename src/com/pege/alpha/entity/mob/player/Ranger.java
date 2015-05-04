package com.pege.alpha.entity.mob.player;

import com.pege.alpha.input.Keyboard;
import com.pege.alpha.level.TileCoordinate;

public class Ranger extends Player {

	public Ranger(TileCoordinate position, Keyboard keyboard) {
		super(position, keyboard);
	}
	
	public void update() {
		super.update();
		updateMovement();
		updateShooting();
	}
	
	private void updateMovement() {
		double dx = 0, dy = 0;
		if (keyboard.up) dy -= speed;
		if (keyboard.down) dy += speed;
		if (keyboard.left) dx -= speed;
		if (keyboard.right) dx += speed;
		
		setRunning(keyboard.shift);
		setSpeed();
		
		if (dx != 0 || dy != 0) {
			walking = true;
			move(dx, dy);
		} else {
			walking = false;
			setRunning(false);
		}
	}
	
	private void updateShooting() {
		fireAllowed--;
		if (keyboard.ctrl && fireAllowed <= 0) {
			double dy = 0.0;
			double dx = 0.0;
			switch (direction) {
				case UP:
					dy = -1.0;
					break;
				case DOWN:
					dy = 1.0;
					break;
				case LEFT:
					dx = -1.0;
					break;
				case RIGHT:
					dx = 1.0;
					break;
			}
			shoot(x, y, Math.atan2(dy, dx));
			fireAllowed = fireRate;
		}
	}

}
