package com.pege.alpha.network.message;

public class MessagePlayerMove extends Message {
	
	public double x;
	public double y;
	
	public MessagePlayerMove(int playerHash, double x, double y) {
		this.entityHash = playerHash;
		this.x = x;
		this.y = y;
	}
}
