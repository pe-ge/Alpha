package com.pege.alpha.network;

import java.nio.ByteBuffer;

import com.pege.alpha.entity.Entity;
import com.pege.alpha.entity.mob.player.Player;
import com.pege.alpha.entity.projectile.BasicProjectile;

public class MessageConstructor {
	
	private final int INT_SIZE = Integer.SIZE / 8;
	private final int DOUBLE_SIZE = Double.SIZE / 8;

	public byte[] constructMessage(Entity e) {
		if (e instanceof Player) return constructPlayerMove((Player)e);
		if (e instanceof BasicProjectile) return constructProjectileNew((BasicProjectile)e);
		
		throw new RuntimeException("Entity is not supported on network game.");
	}
	
	private byte[] constructMessageType(Entity e) {
		if (e instanceof Player) return toByteArray(MessageType.PLAYER_MOVE.ordinal());
		if (e instanceof BasicProjectile) return toByteArray(MessageType.PROJECTILE_NEW.ordinal());
		
		throw new RuntimeException("Unknown type of network entity to be send: " + e.getClass());
	}

	private byte[] constructPlayerMove(Player player) {
		byte[] messageType = constructMessageType(player);
		byte[] playerHash = toByteArray(player.hashCode());
		byte[] xPos = toByteArray(player.getX());
		byte[] yPos = toByteArray(player.getY());
		
		byte[] message = new byte[messageType.length + playerHash.length + xPos.length + yPos.length];
		
		int i = 0;
		for (int j = 0; j < messageType.length; j++, i++) message[i] = messageType[j];
		for (int j = 0; j < playerHash.length; j++, i++) message[i] = playerHash[j];
		for (int j = 0; j < xPos.length; j++, i++) message[i] = xPos[j];
		for (int j = 0; j < yPos.length; j++, i++) message[i] = yPos[j];
		
		return message;
	}
	
	private byte[] constructProjectileNew(BasicProjectile projectile) {
		byte[] messageType = constructMessageType(projectile);
		byte[] projectileHash = toByteArray(projectile.hashCode());
		byte[] ownerHash = toByteArray(projectile.getOwner().hashCode());
		byte[] xPos = toByteArray(projectile.getX());
		byte[] yPos = toByteArray(projectile.getY());
		byte[] angle = toByteArray(projectile.getAngle());
		
		byte[] message = new byte[messageType.length + projectileHash.length + ownerHash.length + xPos.length + yPos.length + angle.length];
		
		int i = 0;
		for (int j = 0; j < messageType.length; j++, i++) message[i] = messageType[j];
		for (int j = 0; j < projectileHash.length; j++, i++) message[i] = projectileHash[j];
		for (int j = 0; j < ownerHash.length; j++, i++) message[i] = ownerHash[j];
		for (int j = 0; j < xPos.length; j++, i++) message[i] = xPos[j];
		for (int j = 0; j < yPos.length; j++, i++) message[i] = yPos[j];
		for (int j = 0; j < angle.length; j++, i++) message[i] = angle[j];
		
		return message;
	}
	
	public byte[] constructDisconnectMessage(Player player) {
		byte[] messageType = toByteArray(MessageType.PLAYER_DISCONNECT.ordinal());
		byte[] entityHash = toByteArray(player.hashCode());
		
		byte[] message = new byte[messageType.length + entityHash.length];
		
		int i = 0;
		for (int j = 0; j < messageType.length; j++, i++) message[i] = messageType[j];
		for (int j = 0; j < entityHash.length; j++, i++) message[i] = entityHash[j];
		
		return message;
	}
	
	private byte[] toByteArray(double number) {
		return ByteBuffer.allocate(DOUBLE_SIZE).putDouble(number).array();
	}
	
	private byte[] toByteArray(int number) {
		return ByteBuffer.allocate(INT_SIZE).putInt(number).array();
	}
}
