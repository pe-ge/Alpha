package com.pege.alpha.network.message;

public class MessageDisconnect extends Message {
	
	public MessageDisconnect(int playerHash) {
		this.entityHash = playerHash;
	}
}
