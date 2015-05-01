package com.pege.alpha.entity.mob.network;

import com.pege.alpha.entity.mob.Mob;
import com.pege.alpha.entity.projectile.Projectile;
import com.pege.alpha.graphics.Sprites;

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
		this.sprite = Sprites.projectile;
		
		this.dx = speed * Math.cos(angle);
		this.dy = speed * Math.sin(angle);
		this.dd = Math.sqrt(dx * dx + dy * dy);
	}
	
	public void setOwner(Mob owner) {
		this.owner = owner;
	}
	
	public void setX(double x) {
		this.x = x;
		this.xOrigin = x;
	}
	
	public void setY(double y) {
		this.y = y;
		this.yOrigin = y;
	}
	
	public void setAngle(double angle) {
		this.angle = angle;
		this.dx = speed * Math.cos(angle);
		this.dy = speed * Math.sin(angle);
		this.dd = Math.sqrt(dx * dx + dy * dy);
	}
}
