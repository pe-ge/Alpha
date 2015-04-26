package com.pege.alpha.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.HashSet;
import java.util.Set;

public class Server extends Thread {
	
	private static Server server;
	
	private DatagramSocket socket;
	private Set<ServerClient> clients = new HashSet<ServerClient>();
	
	private Server(int port) {
		super("Alpha Server");
		
		try {
			socket = new DatagramSocket(port);
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
	
	private void send(final ServerClient client, final byte[] data) {
		DatagramPacket packet = new DatagramPacket(data, data.length, client.getAddress(), client.getPort());
		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		while (true) {
			byte[] data = new byte[256];
			
			DatagramPacket packet = new DatagramPacket(data, data.length);
			try {
				socket.receive(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}
			sendToAll(packet);
		}
	}
			
	private void sendToAll(final DatagramPacket packet) {
		Thread sender = new Thread("Sender") {
			public void run() {
				ServerClient sender = new ServerClient(packet.getAddress(), packet.getPort());
				clients.add(sender);
				for (ServerClient client : clients) {
					if (!client.equals(sender)) {
						send(client, packet.getData());
					}
				}
			}
		};
		sender.start();
	}
	
	public static void initServer(int port) {
		if (server == null) {
			server = new Server(port);
			server.start();
		}
	}
	
	public Server getServer() {
		return server;
	}
}
