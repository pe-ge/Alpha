package com.pege.alpha.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import com.pege.alpha.entity.Entity;
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
	
	public void send(final Entity e) {
		Thread sender = new Thread("Sender") {
			public void run() {
				byte[] data = messageConstructor.constructMessage(e);
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
		byte[] message = messageConstructor.constructDisconnectMessage(level.getPlayer());
		DatagramPacket packet = new DatagramPacket(message, message.length, serverAddress, serverPort);
		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
