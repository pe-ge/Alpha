package com.pege.alpha.network;

import java.util.Map;

import com.pege.alpha.entity.Entity;
import com.pege.alpha.entity.mob.Mob;
import com.pege.alpha.level.Level;
import com.pege.alpha.network.entity.NetworkProjectile;
import com.pege.alpha.network.entity.NetworkRanger;
import com.pege.alpha.network.message.Message;
import com.pege.alpha.network.message.MessageDisconnect;
import com.pege.alpha.network.message.MessagePlayerMove;
import com.pege.alpha.network.message.MessageProjectileNew;

public class MessageProcessor {
	
	private Map<Integer, Entity> entities;
	private Level level;
	
	public MessageProcessor(Map<Integer, Entity> entities) {
		this.entities = entities;
	}
	
	public void setLevel(Level level) {
		this.level = level;
	}
	
	public void processMessage(Message message) {
		if (message instanceof MessageDisconnect) processDisconnect((MessageDisconnect)message);
		if (message instanceof MessagePlayerMove) processPlayerMove((MessagePlayerMove)message);
		if (message instanceof MessageProjectileNew) processProjectileNew((MessageProjectileNew)message);
	}

	private void processDisconnect(MessageDisconnect message) {
		int entityHash = message.entityHash;
		Entity e = entities.get(entityHash);
		e.remove();
		entities.remove(entityHash);
	}

	private void processPlayerMove(MessagePlayerMove message) {
		NetworkRanger networkRanger = (NetworkRanger)findEntity(message);
		networkRanger.setRunning(message.running);
		networkRanger.moveXY(message.x, message. y);
	}

	private void processProjectileNew(MessageProjectileNew message) {
		NetworkProjectile projectile = (NetworkProjectile)findEntity(message);
		projectile.setOwner((Mob)entities.get(message.ownerHash));
		projectile.setXY(message.x, message. y);
		projectile.setAngle(message.angle);
	}
	
	private Entity findEntity(Message message) {
		int entityHash = message.entityHash;
		if (entities.get(entityHash) == null) {
			Entity e = createEntity(message);
			level.addEntity(e);
			entities.put(entityHash, e);
		}
		
		return entities.get(entityHash);
	}
	
	private Entity createEntity(Message message) {
		if (message instanceof MessagePlayerMove) return new NetworkRanger();
		if (message instanceof MessageProjectileNew) return new NetworkProjectile();
		
		return null;
	}
}
