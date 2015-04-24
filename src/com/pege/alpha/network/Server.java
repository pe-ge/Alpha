package com.pege.alpha.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import com.pege.alpha.entity.Entity;
import com.pege.alpha.entity.mob.Mob;
import com.pege.alpha.entity.network.NetworkPlayer;
import com.pege.alpha.entity.network.NetworkProjectile;
import com.pege.alpha.entity.projectile.Projectile;

public class Server {
	
	private static Server server;
	
	private DatagramSocket socket;
	
	private Map<Integer, ServerClient> clients = new HashMap<Integer, ServerClient>();
	
	private boolean running = false;
	
	private final int INT_SIZE = Integer.SIZE / 8;
	private final int DOUBLE_SIZE = Double.SIZE / 8;
	
	public Server(int port) {
		try {
			socket = new DatagramSocket(port);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		
		start();
	}

	private void sendAll(ServerClient updatedClient) {
		for (ServerClient client : clients.values()) {
			if (client.entity instanceof NetworkPlayer && updatedClient != client) {
				byte[] data = constructDataToSend(client.entity);
				send(client, data);
			}
		}
	}
	
	private byte[] constructDataToSend(Entity e) {
		byte[] messageTypeData = constructMessageTypeData(e);
		byte[] entityHashData = toByteArray(e.hashCode());
		byte[] xPosData = toByteArray(e.getX());
		byte[] yPosData = toByteArray(e.getY());
		
		byte[] dataToSend = new byte[messageTypeData.length + entityHashData.length + xPosData.length + yPosData.length];
		
		int i = 0;
		for (int j = 0; j < messageTypeData.length; j++, i++) dataToSend[i] = messageTypeData[j];
		for (int j = 0; j < entityHashData.length; j++, i++) dataToSend[i] = entityHashData[j];
		for (int j = 0; j < xPosData.length; j++, i++) dataToSend[i] = xPosData[j];
		for (int j = 0; j < yPosData.length; j++, i++) dataToSend[i] = yPosData[j];
		
		return dataToSend;
	}
	
	private byte[] constructMessageTypeData(Entity e) {
		if (e instanceof Mob) return toByteArray(MessageType.PLAYER_MOVE.ordinal());
		if (e instanceof Projectile) return toByteArray(MessageType.PROJECTILE_NEW.ordinal());
		
		throw new RuntimeException("Unknown type of network entity to be send: " + e.getClass());
	}
	
	private void send(final ServerClient client, final byte[] data) {
		Thread send = new Thread("Send") {
			public void run() {
				DatagramPacket packet = new DatagramPacket(data, data.length, client.address, client.port);
				try {
					socket.send(packet);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		send.start();
	}

	private void start() {
		Thread recieve = new Thread("Reciever") {
			public void run() {
				while (running) {
					byte[] data = new byte[256];
					
					DatagramPacket packet = new DatagramPacket(data, data.length);
					try {
						socket.receive(packet);
					} catch (IOException e) {
						e.printStackTrace();
					}
					processData(packet);
					
				}
			}
		};
		recieve.start();
	}
	
	private void processData(DatagramPacket packet) {
		byte[] data = packet.getData();
		
		byte[] messageTypeData = new byte[INT_SIZE];
		byte[] entityHashData = new byte[INT_SIZE];
		byte[] xPosData = new byte[DOUBLE_SIZE];
		byte[] yPosData = new byte[DOUBLE_SIZE];
		
		int i = 0;
		for (int j = 0; j < messageTypeData.length; j++, i++) messageTypeData[j] = data[i];
		for (int j = 0; j < entityHashData.length; j++, i++) entityHashData[j] = data[i];
		for (int j = 0; j < xPosData.length; j++, i++) xPosData[j] = data[i];
		for (int j = 0; j < yPosData.length; j++, i++) yPosData[j] = data[i];
		
		int messageType = toInt(messageTypeData);
		int entityHash = toInt(entityHashData);
		double xPos = toDouble(xPosData);
		double yPos = toDouble(yPosData);
		
		updateEntity(packet, messageType, entityHash, xPos, yPos);
		sendAll(clients.get(entityHash));
	}
	
	private void updateEntity(DatagramPacket packet, int messageType, int entityHash, double xPos, double yPos) {
		ServerClient client = null;
		if (!clients.containsKey(entityHash)) {
			Entity e = null;
			if (messageType == MessageType.PLAYER_MOVE.ordinal()) e = new NetworkPlayer(xPos, yPos);
			if (messageType == MessageType.PROJECTILE_NEW.ordinal()); //TODO
			clients.put(entityHash, new ServerClient(packet.getAddress(), packet.getPort(), e));
		}
		client = clients.get(entityHash);
		client.entity.setX(xPos);
		client.entity.setX(yPos);
	}
	
	private double toDouble(byte[] number) {
		return ByteBuffer.wrap(number).asDoubleBuffer().get();
	}
	
	private int toInt(byte[] number) {
		return ByteBuffer.wrap(number).asIntBuffer().get();
	}
	
	private byte[] toByteArray(double number) {
		return ByteBuffer.allocate(DOUBLE_SIZE).putDouble(number).array();
	}
	
	private byte[] toByteArray(int number) {
		return ByteBuffer.allocate(INT_SIZE).putInt(number).array();
	}
	
	public static void initServer(int port) {
		if (server == null) {
			server = new Server(port);
		}
	}
	
	public Server getServer() {
		return server;
	}
}
