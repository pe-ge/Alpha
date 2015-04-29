package com.pege.alpha.network;

import java.nio.ByteBuffer;

import com.pege.alpha.network.message.Message;
import com.pege.alpha.network.message.MessageDisconnect;
import com.pege.alpha.network.message.MessagePlayerMove;
import com.pege.alpha.network.message.MessageProjectileNew;

public class MessageDeconstructor {
	
	private final int INT_SIZE = Integer.SIZE / 8;
	private final int DOUBLE_SIZE = Double.SIZE / 8;
	
	public Message deconstructMessage(byte[] message) {
		byte[] messageTypeData = new byte[INT_SIZE];
		for (int i = 0; i < messageTypeData.length; i++) messageTypeData[i] = message[i];
		int messageType = toInt(messageTypeData);
		
		if (messageType == MessageType.PLAYER_DISCONNECT.ordinal()) return deconstructDisconnectMessage(message);
		if (messageType == MessageType.PLAYER_MOVE.ordinal()) return deconstructPlayerMove(message);
		if (messageType == MessageType.PROJECTILE_NEW.ordinal()) return deconstructProjectileNew(message);
		
		throw new RuntimeException("Unknown type of message");
	}
	
	private MessageDisconnect deconstructDisconnectMessage(byte[] message) {
		byte[] messageTypeData = new byte[INT_SIZE];
		byte[] playerHashData = new byte[INT_SIZE];
		int i = 0;
		for (int j = 0; j < messageTypeData.length; j++, i++) messageTypeData[j] = message[i];
		for (int j = 0; j < playerHashData.length; j++, i++) playerHashData[j] = message[i];
		
		int playerHash = toInt(playerHashData);
		return new MessageDisconnect(playerHash);
	}
	
	private MessagePlayerMove deconstructPlayerMove(byte[] message) {
		byte[] messageTypeData = new byte[INT_SIZE];
		byte[] playerHashData = new byte[INT_SIZE];
		byte[] xPosData = new byte[DOUBLE_SIZE];
		byte[] yPosData = new byte[DOUBLE_SIZE];
		
		int i = 0;
		for (int j = 0; j < messageTypeData.length; j++, i++) messageTypeData[j] = message[i];
		for (int j = 0; j < playerHashData.length; j++, i++) playerHashData[j] = message[i];
		for (int j = 0; j < xPosData.length; j++, i++) xPosData[j] = message[i];
		for (int j = 0; j < yPosData.length; j++, i++) yPosData[j] = message[i];
		
		int playerHash = toInt(playerHashData);
		double xPos = toDouble(xPosData);
		double yPos = toDouble(yPosData);

		return new MessagePlayerMove(playerHash, xPos, yPos);
	}
	
	private MessageProjectileNew deconstructProjectileNew(byte[] message) {
		byte[] messageTypeData = new byte[INT_SIZE];
		byte[] projectileHashData = new byte[INT_SIZE];
		byte[] ownerHashData = new byte[INT_SIZE];
		byte[] xPosData = new byte[DOUBLE_SIZE];
		byte[] yPosData = new byte[DOUBLE_SIZE];
		byte[] angleData = new byte[DOUBLE_SIZE];
		
		int i = 0;
		for (int j = 0; j < messageTypeData.length; j++, i++) messageTypeData[j] = message[i];
		for (int j = 0; j < projectileHashData.length; j++, i++) projectileHashData[j] = message[i];
		for (int j = 0; j < ownerHashData.length; j++, i++) ownerHashData[j] = message[i];
		for (int j = 0; j < xPosData.length; j++, i++) xPosData[j] = message[i];
		for (int j = 0; j < yPosData.length; j++, i++) yPosData[j] = message[i];
		for (int j = 0; j < angleData.length; j++, i++) angleData[j] = message[i];
		
		int projectileHash = toInt(projectileHashData);
		int ownerHash = toInt(ownerHashData);
		double xPos = toDouble(xPosData);
		double yPos = toDouble(yPosData);
		double angle = toDouble(angleData);

		return new MessageProjectileNew(projectileHash, ownerHash, xPos, yPos, angle);
	}
	
	private double toDouble(byte[] number) {
		return ByteBuffer.wrap(number).asDoubleBuffer().get();
	}
	
	private int toInt(byte[] number) {
		return ByteBuffer.wrap(number).asIntBuffer().get();
	}
}
