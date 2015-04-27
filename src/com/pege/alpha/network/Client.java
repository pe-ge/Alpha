package com.pege.alpha.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import com.pege.alpha.entity.Entity;
import com.pege.alpha.entity.mob.network.NetworkRanger;
import com.pege.alpha.entity.mob.player.Player;
import com.pege.alpha.entity.projectile.BasicProjectile;
import com.pege.alpha.entity.projectile.Projectile;
import com.pege.alpha.level.Level;

public class Client extends Thread {
	
	private static Client client;
	
	private DatagramSocket socket;
	private InetAddress serverAddress;
	private int serverPort;
	private boolean running;
	
	private final int INT_SIZE = Integer.SIZE / 8;
	private final int DOUBLE_SIZE = Double.SIZE / 8;

	private Map<Integer, Entity> entities = new HashMap<Integer, Entity>();
	private Level level;
	
	private Client(String serverAddress, int serverPort) {
		super("Alpha Client");
		
		try {
			this.socket = new DatagramSocket();
			this.serverAddress = InetAddress.getByName(serverAddress);
			this.serverPort = serverPort;
		} catch (UnknownHostException | SocketException e) {
			e.printStackTrace();
		}
		running = true;
	}
	
	public void setLevel(Level level) {
		this.level = level;
	}
	
	public void run() {
		while (running) {
			byte[] data = new byte[256];
			
			DatagramPacket packet = new DatagramPacket(data, data.length);
			try {
				socket.receive(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			processRecievedPacket(packet);
		}
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
	
	private void processRecievedPacket(final DatagramPacket packet) {
		Thread processor = new Thread("Processor") {
			public void run() {
				byte[] data = packet.getData();
				
				byte[] messageTypeData = new byte[INT_SIZE];
				for (int i = 0; i < messageTypeData.length; i++) messageTypeData[i] = data[i];
				int messageType = toInt(messageTypeData);
				
				if (messageType == MessageType.PLAYER_DISCONNECT.ordinal()) processDisconnectSignal(data);
				if (messageType == MessageType.PLAYER_MOVE.ordinal()) processPlayerMove(data);
			}
		};
		processor.start();
	}
	
	private void processDisconnectSignal(byte[] data) {
		byte[] messageTypeData = new byte[INT_SIZE];
		byte[] entityHashData = new byte[INT_SIZE];
		int i = 0;
		for (int j = 0; j < messageTypeData.length; j++, i++) messageTypeData[j] = data[i];
		for (int j = 0; j < entityHashData.length; j++, i++) entityHashData[j] = data[i];
		
		int entityHash = toInt(entityHashData);
		entities.get(entityHash).remove();
		entities.remove(entityHash);
	}
	
	private void processPlayerMove(byte[] data) {
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

		NetworkRanger networkPlayer = (NetworkRanger)getEntity(messageType, entityHash);
		networkPlayer.setX(xPos);
		networkPlayer.setY(yPos);
	}
	
	private Entity getEntity(int messageType, int entityHash) {
		if (entities.get(entityHash) == null) {
			Entity e = createEntity(messageType);
			level.addEntity(e);
			
			entities.put(entityHash, e);
		}
		
		return entities.get(entityHash);
	}
	
	private Entity createEntity(int entityType) {
		if (entityType == MessageType.PLAYER_MOVE.ordinal()) return new NetworkRanger();
		if (entityType == MessageType.PROJECTILE_NEW.ordinal()) return new BasicProjectile();
		
		return null;
	}
	
	public void send(final Entity e) {
		Thread sender = new Thread("Sender") {
			public void run() {
				byte[] data = constructDataToSend(e);
				DatagramPacket packet = new DatagramPacket(data, data.length, serverAddress, serverPort);
				try {
					socket.send(packet);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		sender.start();
	}
	
	public void disconnect() {
		running = false;
		
		byte[] messageTypeData = toByteArray(MessageType.PLAYER_DISCONNECT.ordinal());
		byte[] entityHashData = toByteArray(level.getPlayer().hashCode());
		
		byte[] dataToSend = new byte[messageTypeData.length + entityHashData.length];
		
		int i = 0;
		for (int j = 0; j < messageTypeData.length; j++, i++) dataToSend[i] = messageTypeData[j];
		for (int j = 0; j < entityHashData.length; j++, i++) dataToSend[i] = entityHashData[j];
		
		DatagramPacket packet = new DatagramPacket(dataToSend, dataToSend.length, serverAddress, serverPort);
		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	private byte[] constructDataToSend(Entity e) {
		byte[] messageTypeData = createMessageType(e);
		byte[] entityHashData = toByteArray(e.hashCode());
		byte[] xPosData = toByteArray(e.getX());
		byte[] yPosData = toByteArray(e.getY());
		
		byte[] dataToSend = new byte[messageTypeData.length +
		                             entityHashData.length + 
		                             xPosData.length + 
		                             yPosData.length];
		
		int i = 0;
		for (int j = 0; j < messageTypeData.length; j++, i++) dataToSend[i] = messageTypeData[j];
		for (int j = 0; j < entityHashData.length; j++, i++) dataToSend[i] = entityHashData[j];
		for (int j = 0; j < xPosData.length; j++, i++) dataToSend[i] = xPosData[j];
		for (int j = 0; j < yPosData.length; j++, i++) dataToSend[i] = yPosData[j];
		
		return dataToSend;
	}
	
	private byte[] createMessageType(Entity e) {
		if (e instanceof Player) return toByteArray(MessageType.PLAYER_MOVE.ordinal());
		if (e instanceof Projectile) return toByteArray(MessageType.PROJECTILE_NEW.ordinal());
		
		throw new RuntimeException("Unknown type of network entity to be send: " + e.getClass());
	}

	public static void initClient(String serverAddress, int serverPort) {
		if (client == null) {
			client = new Client(serverAddress, serverPort);
			client.start();
		}
	}
	
	public static Client getClient() {
		return client;
	}
	
}
