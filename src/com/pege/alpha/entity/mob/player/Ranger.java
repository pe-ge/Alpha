package com.pege.alpha.entity.mob.player;

import java.awt.event.MouseEvent;

import com.pege.alpha.Game;
import com.pege.alpha.input.Keyboard;
import com.pege.alpha.input.Mouse;
import com.pege.alpha.level.TileCoordinate;
import com.pege.alpha.network.Client;

public class Ranger extends Player {
	
	private double speed = 2;
	private boolean sendUpdate = false;

	public Ranger(TileCoordinate position, Keyboard keyboard, Mouse mouse) {
		super(position, keyboard, mouse);
	}
	
	public void update() {
		super.update();
		
		sendUpdate = false;
		
		updateMovement();
		updateShooting();
		
		sendUpdate();
	}
	
	public void sendUpdate() {
		if (sendUpdate) {
			Client.getClient().send(this);
		}
	}
	
	private void updateMovement() {
		double dx = 0, dy = 0;
		if (keyboard.up) dy -= speed;
		if (keyboard.down) dy += speed;
		if (keyboard.left) dx -= speed;
		if (keyboard.right) dx += speed;
		
		if (dx != 0 || dy != 0) {
			walking = true;
			
			double oldX = x;
			double oldY = y;
			move(dx, dy);
			if (x != oldX || y != oldY) sendUpdate = true;
		} else {
			walking = false;
		}
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
