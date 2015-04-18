package com.pege.alpha.entity.projectile;

import com.pege.alpha.entity.spawner.ParticleSpawner;
import com.pege.alpha.graphics.Screen;
import com.pege.alpha.graphics.Sprites;

public class BasicProjectile extends Projectile {
	
	private int particlesLife;
	private int particlesAmount;

	public BasicProjectile(double xOrigin, double yOrigin, double angle) {
		super(xOrigin, yOrigin, angle);
		this.speed = 10;
		this.range = random.nextInt(150) + 150;
		this.damage = 20;
		this.distance = 0;
		this.sprite = Sprites.projectile;
		
		this.dx = speed * Math.cos(angle);
		this.dy = speed * Math.sin(angle);
		this.dd = Math.sqrt(dx * dx + dy * dy);
		
		this.particlesLife = 20;
		this.particlesAmount = 50;
	}
	
	public void update() {
		move();
	}
	
	public boolean removed() {
		removed |= distance > range;
		return removed;
	}
	
	protected void move() {
		if (!level.tileCollision(this, dx, dy)) {
			x += dx;
			y += dy;
			distance += dd;
		} else {
			level.addEntity(new ParticleSpawner((int)x + 4, (int)y + 1, particlesLife, particlesAmount, level));
			remove();
		}
	}
	
	public void render(Screen screen) {
		if (!removed()) screen.renderSprite((int)x - 4, (int)y - 4, sprite);
	}

}
