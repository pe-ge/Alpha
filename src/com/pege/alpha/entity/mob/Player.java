package com.pege.alpha.entity.mob;

import java.awt.event.MouseEvent;

import com.pege.alpha.Game;
import com.pege.alpha.input.Keyboard;
import com.pege.alpha.input.Mouse;
import com.pege.alpha.level.TileCoordinate;

public class Player extends Mob {
	
	private Keyboard keyboard;
	private Mouse mouse;
	
	public Player(TileCoordinate position, Keyboard keyboard, Mouse mouse) {
		super(position);
		this.keyboard = keyboard;
		this.mouse = mouse;
		speed = 1.3;
	}
	
	public void update() {
		super.update();
		
		double xa = 0, ya = 0;
		if (keyboard.up) ya -= speed;
		if (keyboard.down) ya += speed;
		if (keyboard.left) xa -= speed;
		if (keyboard.right) xa += speed;
		
		if (xa != 0 || ya != 0) {
			walking = true;
			move(xa, ya);
		} else {
			walking = false;
		}
		
		updateShooting();
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
