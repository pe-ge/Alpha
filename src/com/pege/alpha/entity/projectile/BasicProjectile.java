package com.pege.alpha.entity.projectile;

import com.pege.alpha.entity.mob.Mob;
import com.pege.alpha.graphics.sprites.GeneralSprites;

public class BasicProjectile extends Projectile {
	
	public BasicProjectile(Mob owner, double xOrigin, double yOrigin, double angle) {
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

}
