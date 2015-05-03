package com.pege.alpha.entity.mob;

import com.pege.alpha.entity.Entity;
import com.pege.alpha.entity.projectile.BasicProjectile;
import com.pege.alpha.entity.projectile.Projectile;
import com.pege.alpha.graphics.Screen;
import com.pege.alpha.graphics.Sprite;
import com.pege.alpha.graphics.spritesheets.Goku;
import com.pege.alpha.level.TileCoordinate;

public class Mob extends Entity {
		
	public enum Direction {
		UP, DOWN, LEFT, RIGHT
	}
	
	protected Direction direction = Direction.DOWN;
	protected boolean walking = false;
	protected int fireRate = 20;
	protected int fireAllowed = 0;
	protected int time = 0;
	protected int life = 100;
	protected double speed = 2.0;
	
	public Mob() {
		this(0.0, 0.0);
	}
	
	public Mob(double x, double y) {
		this.x = x;
		this.y = y;
		sprite = Goku.standingDown[0]; 
	}
	
	public Mob(TileCoordinate position) {
		this(position.x(), position.y());
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
		
		if (!level.collisionEntityTile(this, dx, dy)) {
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
			if (!level.collisionEntityTile(this, signX, 0)) {
				x += signX;
			}
			
			dx -= signX;
		}
		
		while (Math.abs(dy) > 1.0) {
			if (!level.collisionEntityTile(this, 0, signY)) {
				y += signY;
			}
			
			dy -= signY;
		}
	}
	
	protected int signum(double x) {
		int xi = (int)x;
		xi = Math.min(1, xi);
		xi = Math.max(-1, xi);
		return xi;
	}

	protected void shoot(double x, double y, double angle) {
		Projectile projectile = new BasicProjectile(this, x, y, angle);
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
		if (direction == Direction.UP) setSprite(Goku.standingUp);
		if (direction == Direction.DOWN) setSprite(Goku.standingDown);
		if (direction == Direction.LEFT) setSprite(Goku.standingLeft);
		if (direction == Direction.RIGHT) setSprite(Goku.standingRight);
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
