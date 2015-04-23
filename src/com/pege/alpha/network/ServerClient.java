package com.pege.alpha.network;

import java.net.InetAddress;

public class ServerClient {
	
	public String name;
	public InetAddress address;
	public int port;
	private final int ID;
	
	public ServerClient(String name, InetAddress address, int port, int id) {
		this.name = name;
		this.address = address;
		this.port = port;
		this.ID = id;
	}
	
	public int getId() {
		return ID;
	}
}
