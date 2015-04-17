package com.pege.alpha.entity.spawner;

import com.pege.alpha.entity.Entity;

public abstract class Spawner extends Entity {
	
	public enum Type {
		MOB, PARTICLE
	}
	
	public Spawner(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
}
