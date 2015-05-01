package com.pege.alpha.entity.projectile;

import java.util.Random;

import com.pege.alpha.entity.Entity;
import com.pege.alpha.entity.mob.Mob;
import com.pege.alpha.entity.spawner.ParticleSpawner;
import com.pege.alpha.graphics.Screen;
import com.pege.alpha.graphics.Sprites;

public class Projectile extends Entity {
	
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
		boolean collisionEntityTile = level.collisionEntityTile(this, dx, dy);
		Mob collisionMobParticle = level.collisionMobParticle(this, dx, dy);
		
		if (!collisionEntityTile && collisionMobParticle == null) {
			x += dx;
			y += dy;
			distance += dd;
		} else {
			if (collisionEntityTile) {
				level.addEntity(new ParticleSpawner(Sprites.blackParticle, (int)x + 4, (int)y + 1, 20, 50, level));
			}
			if (collisionMobParticle != null) {
				int mobX = (int)collisionMobParticle.getX();
				int mobY = (int)collisionMobParticle.getY();
				level.addEntity(new ParticleSpawner(Sprites.redParticle, mobX, mobY, 20, 200, level));
			}
			
			remove();
		}
	}
	
	public void render(Screen screen) {
		if (!removed()) screen.renderSprite((int)x - 4, (int)y - 4, sprite);
	}
}
