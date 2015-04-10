package com.pege.alpha.entity.spawner;

import com.pege.alpha.entity.Entity;

public abstract class Spawner extends Entity {
	
	public enum Type {
		MOB, PARTICLE
	}
	
	public Spawner(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
}
