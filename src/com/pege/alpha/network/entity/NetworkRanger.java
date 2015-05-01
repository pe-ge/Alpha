package com.pege.alpha.network.entity;

import com.pege.alpha.entity.mob.Mob;

public class NetworkRanger extends Mob {
	
	private double receivedX;
	private double receivedY;
	
	public void setX(double x) {
		receivedX = x;
	}
	
	public void setY(double y) {
		receivedY = y;
	}
	
	public void update() {
		super.update();
		updateMovement();
	}

	private void updateMovement() {
		if (receivedX != x || receivedY != y) {
			walking = true;
			
			double dx = speed * signum(receivedX - x);
			double dy = speed * signum(receivedY - y);
			move(dx, dy);
		} else {
			walking = false;
		}
	}
}
