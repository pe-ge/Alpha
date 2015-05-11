package com.pege.alpha.entity.spawner;

import com.pege.alpha.entity.particle.Particle;
import com.pege.alpha.graphics.Screen;
import com.pege.alpha.graphics.sprites.GeneralSprites;
import com.pege.alpha.level.Level;

public class InfiniteParticleSpawner extends Spawner {
	
	private int life;
	private Level level;

	public InfiniteParticleSpawner(int x, int y, int life, Level level) {
		super(x, y);
		this.life = life;
		this.level = level;
	}

	@Override
	public void update() {
		level.addEntity(new Particle(GeneralSprites.blackParticle, x, y, life));
	}

	@Override
	public void render(Screen screen) {
		// TODO Auto-generated method stub

	}

}
