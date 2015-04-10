package com.pege.alpha.entity.projectile;

import java.util.Random;

import com.pege.alpha.entity.Entity;

public abstract class Projectile extends Entity {
	
	protected double angle;
	protected double xOrigin, yOrigin;
	protected double dx, dy;
	protected int range, damage;
	protected double distance;
	protected double dd;
	
	protected final Random random = new Random();
	
	public Projectile(double xOrigin, double yOrigin, double angle) {
		this.xOrigin = xOrigin;
		this.yOrigin = yOrigin;
		x = xOrigin;
		y = yOrigin;
		this.angle = angle;
	}
}
