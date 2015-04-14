package com.pege.alpha.entity.projectile;

import com.pege.alpha.entity.spawner.ParticleSpawner;
import com.pege.alpha.graphics.Screen;
import com.pege.alpha.graphics.Sprites;

public class BasicProjectile extends Projectile {
	
	private int particlesLife = 20;
	private int particlesAmount = 50;

	public BasicProjectile(double xOrigin, double yOrigin, double angle) {
		super(xOrigin, yOrigin, angle);
		speed = 0.1;
		range = random.nextInt(150) + 150;
		damage = 20;
		distance = 0;
		sprite = Sprites.projectile;
		
		dx = speed * Math.cos(angle);
		dy = speed * Math.sin(angle);
		dd = Math.sqrt(dx * dx + dy * dy);
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
			level.addEntity(new ParticleSpawner((int)x, (int)y + 1, particlesLife, particlesAmount, level));
			remove();
		}
	}
	
	public void render(Screen screen) {
		if (!removed()) screen.renderSprite((int)x - 4, (int)y - 4, sprite);
	}

}
