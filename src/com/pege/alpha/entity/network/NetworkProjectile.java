package com.pege.alpha.entity.network;

import com.pege.alpha.entity.mob.Mob;
import com.pege.alpha.entity.projectile.BasicProjectile;

public class NetworkProjectile extends BasicProjectile {
	
	public NetworkProjectile() {
		
	}

	public NetworkProjectile(Mob owner, double xOrigin, double yOrigin, double angle) {
		super(owner, xOrigin, yOrigin, angle);
	}

}
