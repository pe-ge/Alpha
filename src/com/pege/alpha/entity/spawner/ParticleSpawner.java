package com.pege.alpha.entity.spawner;

import com.pege.alpha.entity.particle.Particle;
import com.pege.alpha.graphics.Screen;
import com.pege.alpha.level.Level;

public class ParticleSpawner extends Spawner {
	
	public ParticleSpawner(int x, int y, int life, int amount, Level level) {
		super(x, y);
		for (int i = 0; i < amount; i++) {
			level.addEntity(new Particle(x, y, life));
		}
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(Screen screen) {
		// TODO Auto-generated method stub
		
	}

	
}
