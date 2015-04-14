package com.pege.alpha.entity.mob.player;

import java.awt.event.MouseEvent;

import com.pege.alpha.Game;
import com.pege.alpha.input.Keyboard;
import com.pege.alpha.input.Mouse;
import com.pege.alpha.level.TileCoordinate;

public class Car extends Player {
	
	private double dx = 0.0, dy = 0.0;
	private double speed = 0.0;
	private int angle = 0;
	
	public Car(TileCoordinate position, Keyboard keyboard, Mouse mouse) {
		super(position, keyboard, mouse);
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
		if (mouse.getButton() == MouseEvent.BUTTON1 && fireAllowed <= 0) {
			double dy = mouse.getY() - Game.VERTICAL_CENTER;
			double dx = mouse.getX() - Game.HORIZONTAL_CENTER;
			shoot(x, y, Math.atan2(dy, dx));
			fireAllowed = fireRate;
		}
	}

}
