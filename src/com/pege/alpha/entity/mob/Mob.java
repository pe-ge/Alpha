package com.pege.alpha.entity.mob;

import java.util.Random;

import com.pege.alpha.entity.Entity;
import com.pege.alpha.entity.projectile.BasicProjectile;
import com.pege.alpha.entity.projectile.Projectile;
import com.pege.alpha.graphics.Screen;
import com.pege.alpha.graphics.Sprite;
import com.pege.alpha.graphics.sprites.GokuSprites;
import com.pege.alpha.level.TileCoordinate;

public class Mob extends Entity {
	
	protected Direction direction = Direction.DOWN;
	protected boolean walking = false;
	protected boolean running = false;
	protected boolean shooting = false;

	protected int spriteIndex = 0;
		
	protected int time = 0;
	protected int life = 100;
	
	protected final double WALKING_SPEED = 1.0;
	protected final double RUNNING_SPEED = 2.0;
	protected double speed = WALKING_SPEED;
	
	protected int fireRate = 50;
	protected int fireAllowed = 0;
	protected int shootTime = Integer.MAX_VALUE;
	
	protected Random random = new Random();
	
	public Mob() {
		this(0.0, 0.0);
	}
	
	public Mob(double x, double y) {
		this.x = x;
		this.y = y;
		sprite = GokuSprites.standingDown[0]; 
	}
	
	public Mob(TileCoordinate position) {
		this(position.x(), position.y());
	}
	
	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}
	
	public void setSpeed() {
		if (isRunning()) {
			speed = RUNNING_SPEED;
		} else {
			speed = WALKING_SPEED;
		}
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

	protected void shoot() {
		shooting = true;
		shootTime = time;
	}
	
	public void update() {
		time = (time != 100000 ? time + 1 : 0); //increment time
		updateShooting();
	}
	
	protected void updateShooting() {
		fireAllowed--; // TODO: refactor (variable can overflow)
		//System.out.println(shooting + " " + (fireAllowed < 0) + (time - shootTime > fireRate));
		if (shooting && fireAllowed < 0 && time - shootTime > fireRate) {
			double angle = Math.atan2(direction.getY(), direction.getX());
			Projectile projectile = new BasicProjectile(this, x, y, angle);
			level.addEntity(projectile);
			fireAllowed = fireRate;
		}
	}
	
	public void render(Screen screen) {
		setSprite();
		screen.renderSprite((int)(x - 16), (int)(y - 16), sprite);
	}
	
	private void setSprite() {
		if (shooting) {
			if (direction == Direction.UP) setShootingSprite(GokuSprites.shootingUp);
			if (direction == Direction.DOWN) setShootingSprite(GokuSprites.shootingDown);
			if (direction == Direction.LEFT) setShootingSprite(GokuSprites.shootingLeft);
			if (direction == Direction.RIGHT) setShootingSprite(GokuSprites.shootingRight);
		} else if (!walking && !running) {
			if (direction == Direction.UP) setSprite(GokuSprites.standingUp);
			if (direction == Direction.DOWN) setSprite(GokuSprites.standingDown);
			if (direction == Direction.LEFT) setSprite(GokuSprites.standingLeft);
			if (direction == Direction.RIGHT) setSprite(GokuSprites.standingRight);
		} else if (walking && !running) {
			if (direction == Direction.UP) setSprite(GokuSprites.walkingUp);
			if (direction == Direction.DOWN) setSprite(GokuSprites.walkingDown);
			if (direction == Direction.LEFT) setSprite(GokuSprites.walkingLeft);
			if (direction == Direction.RIGHT) setSprite(GokuSprites.walkingRight);
		} else {
			if (direction == Direction.UP) setSprite(GokuSprites.runningUp);
			if (direction == Direction.DOWN) setSprite(GokuSprites.runningDown);
			if (direction == Direction.LEFT) setSprite(GokuSprites.runningLeft);
			if (direction == Direction.RIGHT) setSprite(GokuSprites.runningRight);
		}
	}
	
	private void setSprite(Sprite[] sprites) {
		if (!walking && !running) {
			if (time % (random.nextInt(50) + 20) == 0) { //how long are eyes closed
				spriteIndex = 0;
			}
			if (time % (random.nextInt(500) + 20) == 0) { //how often to blink
				spriteIndex = 1;
			}
		} else {
			spriteIndex = time >> 3;
		}
		
		spriteIndex %= sprites.length;
		sprite = sprites[spriteIndex];
	}
	
	private void setShootingSprite(Sprite[] sprites) {
		int index = 0;
		int timeDiff = time - shootTime;
		if (timeDiff >= fireRate) {
			index = 1;
		}
		if (timeDiff > fireRate + 10) {
			shooting = false;
		}
		sprite = sprites[index];
	}
}
