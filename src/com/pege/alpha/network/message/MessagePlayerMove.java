package com.pege.alpha.network.message;

public class MessagePlayerMove extends Message {
	
	public double x;
	public double y;
	public boolean running;
	
	public MessagePlayerMove(int playerHash, double x, double y, boolean running) {
		this.entityHash = playerHash;
		this.x = x;
		this.y = y;
		this.running = running;
	}
}
