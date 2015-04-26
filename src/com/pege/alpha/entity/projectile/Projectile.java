package com.pege.alpha.entity.projectile;

import java.util.Random;

import com.pege.alpha.entity.Entity;
import com.pege.alpha.entity.mob.Mob;

public abstract class Projectile extends Entity {
	
	protected Mob owner; //owner of projectile
	protected double angle;
	protected double xOrigin, yOrigin;
	protected double speed;
	protected double dx, dy;
	protected int range, damage;
	protected double distance;
	protected double dd;
	
	protected final Random random = new Random();
	
	public Projectile() {
		
	}
	
	public Projectile(Mob owner, double xOrigin, double yOrigin, double angle) {
		this.owner = owner;
		this.xOrigin = xOrigin;
		this.yOrigin = yOrigin;
		this.x = xOrigin;
		this.y = yOrigin;
		this.angle = angle;
	}
	
	public Mob getOwner() {
		return owner;
	}
}
