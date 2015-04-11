package com.pege.alpha.entity.mob;

import com.pege.alpha.entity.Entity;
import com.pege.alpha.entity.projectile.BasicProjectile;
import com.pege.alpha.entity.projectile.Projectile;
import com.pege.alpha.graphics.Screen;
import com.pege.alpha.graphics.Sprite;
import com.pege.alpha.graphics.Sprites;
import com.pege.alpha.level.TileCoordinate;

public abstract class Mob extends Entity {
		
	public enum Direction {
		UP, DOWN, LEFT, RIGHT
	}
	
	protected Direction direction = Direction.DOWN;
	protected boolean walking = false;
	protected int fireRate = 10;
	protected int fireAllowed = 0;
	protected int time = 0;
	
	public Mob(TileCoordinate position) {
		this.x = position.x();
		this.y = position.y();
		sprite = Sprites.playerDown[0];
	}
	
	public void move(double dx, double dy) {
		if (dx != 0 && dy != 0) {
			move(dx, 0);
			move(0, dy);
			return;
		}
		
		if (dx > 0) direction = Direction.RIGHT;
		if (dx < 0) direction = Direction.LEFT;
		if (dy > 0) direction = Direction.DOWN;
		if (dy < 0) direction = Direction.UP;
		
		if (!level.tileCollision(this, dx, dy)) {
			x += dx;
			y += dy;
		} else {
			moveOnCollision(dx, dy);
		}
	}
	
	private void moveOnCollision(double dx, double dy) {
		int signX = signum(dx);
		int signY = signum(dy);
		
		while (Math.abs(dx) > 1.0) {
			if (!level.tileCollision(this, signX, 0)) {
				x += signX;
			}
			
			dx -= signX;
		}
		
		while (Math.abs(dy) > 1.0) {
			if (!level.tileCollision(this, 0, signY)) {
				y += signY;
			}
			
			dy -= signY;
		}
	}
	
	protected int signum(double x) {
		if (x > 0.0) {
			return 1;
		} else if (x < 0.0){
			return -1;
		} else {
			return 0;
		}
	}

	protected void shoot(double x, double y, double angle) {
		Projectile projectile = new BasicProjectile(x, y, angle);
		level.addEntity(projectile);
	}
	
	public void update() {
		time = (time != 100000 ? time + 1 : 0); //increment time
	}
	
	public void render(Screen screen) {
		setSprite();
		screen.renderSprite((int)(x - 16), (int)(y - 16), sprite);
	}
	
	private void setSprite() {
		if (direction == Direction.UP) setSprite(Sprites.playerUp);
		if (direction == Direction.DOWN) setSprite(Sprites.playerDown);
		if (direction == Direction.LEFT) setSprite(Sprites.playerLeft);
		if (direction == Direction.RIGHT) setSprite(Sprites.playerRight);
	}
	
	private void setSprite(Sprite[] sprites) {
		int index = 0;
		if (!walking) {
			index = 0;
		} else if (direction == Direction.LEFT || direction == Direction.RIGHT) {
			index = (time >> 3) % 4;
			if (index == 0) {
				index = 1;
			} else if (index == 1 || index == 3) {
				index = 0;
			}
		} else {
			index = (time >> 3) % (sprites.length - 1) + 1;
		}
		
		sprite = sprites[index];
	}
}
