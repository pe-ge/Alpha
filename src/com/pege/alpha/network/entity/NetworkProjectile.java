package com.pege.alpha.network.entity;

import com.pege.alpha.entity.mob.Mob;
import com.pege.alpha.entity.projectile.Projectile;
import com.pege.alpha.graphics.sprites.GeneralSprites;

public class NetworkProjectile extends Projectile {

	public NetworkProjectile() {
		this(null, 0.0, 0.0, 0.0);
	}
	
	public NetworkProjectile(Mob owner, double xOrigin, double yOrigin, double angle) {
		super(owner, xOrigin, yOrigin, angle);
		this.speed = 5;
		this.range = random.nextInt(150) + 150;
		this.damage = 20;
		this.distance = 0;
		this.sprite = GeneralSprites.projectile;
		
		this.dx = speed * Math.cos(angle);
		this.dy = speed * Math.sin(angle);
		this.dd = Math.sqrt(dx * dx + dy * dy);
	}
	
	public void setOwner(Mob owner) {
		this.owner = owner;
	}
	
	public void setAngle(double angle) {
		this.angle = angle;
		this.dx = speed * Math.cos(angle);
		this.dy = speed * Math.sin(angle);
		this.dd = Math.sqrt(dx * dx + dy * dy);
	}
}
