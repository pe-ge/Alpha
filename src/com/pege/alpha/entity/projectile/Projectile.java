package com.pege.alpha.entity.projectile;

import java.util.Random;

import com.pege.alpha.entity.Entity;
import com.pege.alpha.entity.mob.Mob;
import com.pege.alpha.entity.spawner.ParticleSpawner;
import com.pege.alpha.graphics.Screen;
import com.pege.alpha.graphics.Sprites;

public abstract class Projectile extends Entity {
	
	protected Mob owner;
	protected double angle;
	protected double speed;
	protected double range, damage;
	protected double distance;
	
	protected double dx, dy, dd;
	
	protected final Random random = new Random();
	
	public Projectile(Mob owner, double xOrigin, double yOrigin, double angle) {
		this.owner = owner;
		this.x = xOrigin;
		this.y = yOrigin;
		this.angle = angle;
	}
	
	public void update() {
		move();
	}
	
	public Mob getOwner() {
		return owner;
	}
	
	public double getAngle() {
		return angle;
	}
	
	public boolean removed() {
		removed |= distance > range;
		return removed;
	}
	
	protected void move() {
		boolean collisionTile = level.collisionEntityTile(this, dx, dy);
		Mob collisionMob = level.collisionMobProjectile(this, dx, dy);
		
		if (!collisionTile && collisionMob == null) {
			x += dx;
			y += dy;
			distance += dd;
		} else {
			if (collisionTile) {
				level.addEntity(new ParticleSpawner(Sprites.blackParticle, (int)x + 4, (int)y + 1, 20, 50, level));
			}
			if (collisionMob != null) {
				int mobX = (int)collisionMob.getX();
				int mobY = (int)collisionMob.getY();
				level.addEntity(new ParticleSpawner(Sprites.redParticle, mobX, mobY, 20, 200, level));
			}
			
			remove();
		}
	}
	
	public void render(Screen screen) {
		if (!removed()) screen.renderSprite((int)x - 4, (int)y - 4, sprite);
	}
}
