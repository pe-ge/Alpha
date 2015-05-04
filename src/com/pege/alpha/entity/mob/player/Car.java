package com.pege.alpha.entity.mob.player;

import com.pege.alpha.input.Keyboard;
import com.pege.alpha.level.TileCoordinate;

public class Car extends Player {
	
	private double dx = 0.0, dy = 0.0;
	private double speed = 0.0;
	private int angle = 0;
	
	public Car(TileCoordinate position, Keyboard keyboard) {
		super(position, keyboard);
	}
	
	public void update() {
		super.update();
		
		updateMovement();
		updateShooting();
	}
	
	private void updateMovement() {
		if (keyboard.up) speed += 0.1;
		if (keyboard.down) speed -= 0.1;
		if (keyboard.left) angle -= 5;
		if (keyboard.right) angle += 5;
		
		dx = speed * Math.cos(Math.toRadians(angle));
		dy = speed * Math.sin(Math.toRadians(angle));
		
		speed = 0.95 * speed;
		
		move(dx, dy);
	}
	
	private void updateShooting() {
		fireAllowed--;
		if (keyboard.space && fireAllowed <= 0) {
			double dy = 1;
			double dx = 1;
			shoot(x, y, Math.atan2(dy, dx));
			fireAllowed = fireRate;
		}
	}

}
