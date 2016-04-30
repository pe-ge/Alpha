package com.pege.alpha.network.message;

public class MessageProjectileNew extends Message {
	
	public int ownerHash;
	public double x;
	public double y;
	public double angle;
	
	public MessageProjectileNew(int projectileHash, int ownerHash, double x, double y, double angle) {
		this.entityHash = projectileHash;
		this.ownerHash = ownerHash;
		this.x = x;
		this.y = y;
		this.angle = angle;
	}
}
