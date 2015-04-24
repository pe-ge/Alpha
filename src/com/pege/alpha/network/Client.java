package com.pege.alpha.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

import com.pege.alpha.entity.Entity;
import com.pege.alpha.entity.mob.player.Player;

public class Client {
	
	private static Client client;
	
	private DatagramSocket socket;
	private InetAddress ip;
	private int port;
	
	private final int INT_SIZE = Integer.SIZE / 8;
	private final int DOUBLE_SIZE = Double.SIZE / 8;

	public Client(String address, int port) {
		this.port = port;
		try {
			this.socket = new DatagramSocket();
			this.ip = InetAddress.getByName(address);
		} catch (UnknownHostException | SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void registerPlayer(Player p) {
		sendPosition(p);
	}
	
	public String receive() {
		byte[] data = new byte[1024];
		DatagramPacket packet = new DatagramPacket(data, data.length);
		
		try {
			socket.receive(packet);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return new String(packet.getData());
	}
	
	public void sendPosition(Entity e) {
		byte[] entityType = e.getClass().toString().getBytes();
		byte[] entityHash = toByteArray(e.hashCode());
		byte[] xPos = toByteArray(e.getX());
		byte[] yPos = toByteArray(e.getY());
		byte dataLength = (byte)(entityType.length + entityHash.length + xPos.length + yPos.length);
		
		byte[] dataToSend = new byte[dataLength + 1];
		dataToSend[0] = dataLength;
		int i = 1;
		for (int j = 0; j < entityType.length; j++, i++) dataToSend[i] = entityType[j];
		for (int j = 0; j < entityHash.length; j++, i++) dataToSend[i] = entityHash[j];
		for (int j = 0; j < xPos.length; j++, i++) dataToSend[i] = xPos[j];
		for (int j = 0; j < yPos.length; j++, i++) dataToSend[i] = yPos[j];
		
		send(dataToSend);
	}
	
	private byte[] toByteArray(double number) {
		return ByteBuffer.allocate(DOUBLE_SIZE).putDouble(number).array();
	}
	
	private byte[] toByteArray(int number) {
		return ByteBuffer.allocate(INT_SIZE).putInt(number).array();
	}
	
	private void send(final byte[] data) {
		Thread send = new Thread("Send") {
			public void run() {
				DatagramPacket packet = new DatagramPacket(data, data.length, ip, port);
				try {
					socket.send(packet);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		send.start();
	}

	public static void initClient(String address, int port) {
		if (client == null) {
			client = new Client(address, port);
		}
	}
	
	public static Client getClient() {
		return client;
	}
	
}
