package com.pege.alpha.entity.mob;

import com.pege.alpha.entity.mob.player.Player;
import com.pege.alpha.level.TileCoordinate;

public class Dummy extends Mob {

	private double xa = 0;
	private double ya = 0;
	private int radius;
	private double speed = 0.5;
	
	private Player player;

	public Dummy(TileCoordinate position, int radius) {
		super(position);
		this.radius = radius;
	}
	
	public void update() {
		super.update();
		if (getDistanceFromPlayer() <= radius) {
			xa = speed * signum((int)player.getX() - x);
			ya = speed * signum((int)player.getY() - y);
		} else {
			if (time % (random.nextInt(50) + 30) == 0) {
				xa = speed * (random.nextInt(3) - 1);
				ya = speed * (random.nextInt(3) - 1);
				
				if (random.nextInt(3) == 0) {
					xa = 0;
					ya = 0;
				}
			}
			
		}

		if (xa != 0 || ya != 0) {
			walking = true;
			move(xa, ya);
		} else {
			walking = false;
		}
	}
	
	private double getDistanceFromPlayer() {
		if (player == null) {
			player = level.getPlayer();
		}
		double dx = x - player.getX();
		double dy = y - player.getY();
		double dis = Math.sqrt(dx * dx + dy * dy);
		return dis;
	}

}
