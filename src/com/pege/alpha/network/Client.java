package com.pege.alpha.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

import com.pege.alpha.entity.Entity;
import com.pege.alpha.entity.mob.player.Player;
import com.pege.alpha.level.Level;
import com.pege.alpha.network.message.Message;

public class Client extends Thread {

	private DatagramSocket socket;
	private InetAddress serverAddress;
	private int serverPort;
	private boolean running;
	
	private MessageConstructor messageConstructor;
	private MessageDeconstructor messageDeconstructor;
	private MessageProcessor messageProcessor;
	
	private Map<Integer, Entity> networkEntities = new HashMap<Integer, Entity>();
	private Level level;
	
	private long lastUpdatedTime = System.nanoTime();
	private long updateRate = 200000000L; //0.2sec
	
	public Client(String serverAddress, int serverPort) {
		super("Alpha Client");
		
		try {
			this.socket = new DatagramSocket();
			this.serverAddress = InetAddress.getByName(serverAddress);
			this.serverPort = serverPort;
		} catch (UnknownHostException | SocketException e) {
			e.printStackTrace();
		}
		this.running = true;
		this.messageConstructor = new MessageConstructor();
		this.messageDeconstructor = new MessageDeconstructor();
		this.messageProcessor = new MessageProcessor(networkEntities);
	}
	
	public void setLevel(Level level) {
		this.level = level;
		messageProcessor.setLevel(level);
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
			
			processPacket(packet);
		}
	}
	
	private void processPacket(final DatagramPacket packet) {
		Thread processor = new Thread("Processor") {
			public void run() {
				byte[] data = packet.getData();
				Message message = messageDeconstructor.deconstructMessage(data);
				messageProcessor.processMessage(message);
			}
		};
		processor.start();
	}
	
	public void sendEntities() {
		Queue<Entity> entitiesToSend = level.getEntitiesToSend();
		while (!entitiesToSend.isEmpty()) {
			Entity entity = entitiesToSend.poll();
			send(entity);
		}
	}
	
	private void send(Entity e) {
		if (e instanceof Player) {
			sendPlayer((Player)e);
			return;
		}
		
		sendEntity(e);
	}
	
	private void sendEntity(Entity e) {
		byte[] message = messageConstructor.constructMessage(e);
		send(message);
	}
	
	private void sendPlayer(Player p) {
		long time = System.nanoTime();
		if (time - lastUpdatedTime > updateRate) {
			sendEntity(p);
			lastUpdatedTime = time;
		}
	}
	
	private void send(byte[] data) {
		DatagramPacket packet = new DatagramPacket(data, data.length, serverAddress, serverPort);
		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void disconnect(Player player) {
		running = false;
		byte[] message = messageConstructor.constructDisconnectMessage(player);
		send(message);
	}

}
