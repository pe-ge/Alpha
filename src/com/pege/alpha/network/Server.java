package com.pege.alpha.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class Server implements Runnable {
	
	private static Server server;
	
	private DatagramSocket socket;
	
	private List<ServerClient> clients = new ArrayList<ServerClient>();
	
	private boolean running = false;
	private Thread run, send, recieve;
	
	private final int DOUBLE_SIZE = Double.SIZE / 8;
	
	public Server(int port) {
		try {
			socket = new DatagramSocket(port);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		run = new Thread(this, "Alpha Server");
		run.start();
	}

	public void run() {
		running = true;
		recieve();
		send();
	}

	private void send() {
		
	}

	private void recieve() {
		recieve = new Thread("Reciever") {
			public void run() {
				while (running) {
					byte[] data = new byte[256];
					
					DatagramPacket packet = new DatagramPacket(data, data.length);
					try {
						socket.receive(packet);
					} catch (IOException e) {
						e.printStackTrace();
					}
					
					processData(packet.getData());
					
				}
			}
		};
		recieve.start();
	}
	
	private void processData(byte[] data) {
		byte[] entityTypeData = new byte[data[0] - 2 * DOUBLE_SIZE];
		byte[] xPosData = new byte[DOUBLE_SIZE];
		byte[] yPosData = new byte[DOUBLE_SIZE];
		
		int i = 1;
		for (int j = 0; j < entityTypeData.length; j++, i++) entityTypeData[j] = data[i];
		for (int j = 0; j < xPosData.length; j++, i++) xPosData[j] = data[i];
		for (int j = 0; j < yPosData.length; j++, i++) yPosData[j] = data[i];
		
		String entityType = new String(entityTypeData);
		double xPos = toDouble(xPosData);
		double yPos = toDouble(yPosData);
		
	}
	
	private double toDouble(byte[] number) {
		return ByteBuffer.wrap(number).asDoubleBuffer().get();
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
