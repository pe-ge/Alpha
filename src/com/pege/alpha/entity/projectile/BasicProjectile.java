package com.pege.alpha.entity.projectile;

import com.pege.alpha.entity.mob.Mob;
import com.pege.alpha.entity.spawner.ParticleSpawner;
import com.pege.alpha.graphics.Screen;
import com.pege.alpha.graphics.Sprites;

public class BasicProjectile extends Projectile {
	
	public BasicProjectile() {
		this(null, 0.0, 0.0, 0.0);
	}
	
	public BasicProjectile(Mob owner, double xOrigin, double yOrigin, double angle) {
		super(owner, xOrigin, yOrigin, angle);
		this.speed = 5;
		this.range = random.nextInt(150) + 150;
		this.damage = 20;
		this.distance = 0;
		this.sprite = Sprites.projectile;
		
		this.dx = speed * Math.cos(angle);
		this.dy = speed * Math.sin(angle);
		this.dd = Math.sqrt(dx * dx + dy * dy);
		
		this.lastUpdatedTime = System.nanoTime();
		this.sendUpdate = true;
	}
	
	public void update() {
		sendUpdateToServer();
		move();
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
