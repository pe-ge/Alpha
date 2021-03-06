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
	
	protected int time = 0;
	protected int health = 100;
	protected boolean alive = true;

	protected Random random = new Random();
	
	// movement-related variables
	protected Direction direction = Direction.DOWN;
	protected boolean walking = false;
	protected boolean running = false;
	protected final double WALKING_SPEED = 1.0;
	protected final double RUNNING_SPEED = 15.0;
	protected double speed = WALKING_SPEED;
	protected int spriteIndex = 0;
	
	// shooting-related variables
	protected boolean shooting = false;
	protected int shootTime = 0;
	protected final int CHARGING_TIME = 15;
	protected final int FIRING_TIME = 30;
	protected final int FIRE_RATE = CHARGING_TIME + FIRING_TIME;
	
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
	
	public void updateSpeed() {
		if (isRunning()) {
			speed = RUNNING_SPEED;
		} else {
			speed = WALKING_SPEED;
		}
	}
	
	public void move(double dx, double dy) {
		if (shooting) {
			return;
		}
		
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
		tick();
		updateShooting();
		if (health <= 0) {
			alive = false;
		}
	}
	
	protected void tick() {
		time = (time != 100000 ? time + 1 : 0);
	}
	
	protected void updateShooting() {
		if (shooting) {
			int timeDiff = time - shootTime - CHARGING_TIME;
			
			if (timeDiff == 0) {
				double angle = Math.atan2(direction.getY(), direction.getX());
				Projectile projectile = new BasicProjectile(this, x, y, angle);
				level.addEntity(projectile);
			}
			
			if (timeDiff == FIRING_TIME) {
				shooting = false;
			}
		}
	}
	
	public void render(Screen screen) {
		setSprite();
		screen.renderSprite((int)(x - 16), (int)(y - 16), sprite);
	}
	
	private void setSprite() {
		if (!alive) {
			setBasicSprite(GokuSprites.dyingRight);
		} else if (shooting) {
			if (direction == Direction.UP) setShootingSprite(GokuSprites.shootingUp);
			if (direction == Direction.DOWN) setShootingSprite(GokuSprites.shootingDown);
			if (direction == Direction.LEFT) setShootingSprite(GokuSprites.shootingLeft);
			if (direction == Direction.RIGHT) setShootingSprite(GokuSprites.shootingRight);
		} else if (!walking && !running) {
			if (direction == Direction.UP) setBasicSprite(GokuSprites.standingUp);
			if (direction == Direction.DOWN) setBasicSprite(GokuSprites.standingDown);
			if (direction == Direction.LEFT) setBasicSprite(GokuSprites.standingLeft);
			if (direction == Direction.RIGHT) setBasicSprite(GokuSprites.standingRight);
		} else if (walking && !running) {
			if (direction == Direction.UP) setBasicSprite(GokuSprites.walkingUp);
			if (direction == Direction.DOWN) setBasicSprite(GokuSprites.walkingDown);
			if (direction == Direction.LEFT) setBasicSprite(GokuSprites.walkingLeft);
			if (direction == Direction.RIGHT) setBasicSprite(GokuSprites.walkingRight);
		} else {
			if (direction == Direction.UP) setBasicSprite(GokuSprites.runningUp);
			if (direction == Direction.DOWN) setBasicSprite(GokuSprites.runningDown);
			if (direction == Direction.LEFT) setBasicSprite(GokuSprites.runningLeft);
			if (direction == Direction.RIGHT) setBasicSprite(GokuSprites.runningRight);
		}
	}

	private void setBasicSprite(Sprite[] sprites) {
		if (!walking && !running && alive) {
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
		int spriteIndex = 0;
		
		int startFiring = time - shootTime - CHARGING_TIME;
		if (startFiring > 0) {
			if ((startFiring / FIRING_TIME) % 2 == 0) {
				spriteIndex = 1;
			} else {
				spriteIndex = 2;
			}
		}
		
		sprite = sprites[spriteIndex];
	}
	
	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}
}
