package com.pege.alpha.network;

import java.net.InetAddress;

import com.pege.alpha.entity.Entity;

public class ServerClient {
	
	public InetAddress address;
	public int port;
	public Entity entity;
	
	public ServerClient(InetAddress address, int port, Entity entity) {
		this.address = address;
		this.port = port;
		this.entity = entity;
	}
}
