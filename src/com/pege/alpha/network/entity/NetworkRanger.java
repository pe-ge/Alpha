package com.pege.alpha.network.entity;

import com.pege.alpha.entity.mob.Mob;

public class NetworkRanger extends Mob {
	
	private double moveX;
	private double moveY;
	
	//distance between actual and received position
	//when the distance is higher than this value, players position
	//will be set manually, instead of moving to received position
	private final double skipDistance = 100.0;
	
	public void moveXY(double x, double y) {
		moveX = x;
		moveY = y;
	}
	
	public void update() {
		super.update();
		updateMovement();
	}

	private void updateMovement() {
		if (Math.abs(moveX - x) + Math.abs(moveY - y) < skipDistance) {
			double dx = speed * signum(moveX - x);
			double dy = speed * signum(moveY - y);
			if (dx != 0 || dy != 0) {
				walking = true;
				move(dx, dy);
			} else {
				walking = false;
			}
		} else {
			setXY(moveX, moveY);
		}
	}
}
