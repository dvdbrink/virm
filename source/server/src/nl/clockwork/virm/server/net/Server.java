package nl.clockwork.virm.server.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable {
	private boolean running;
	private ServerSocket serverSocket;
	private long nConnections;
	private String ip;
	private int port;

	public Server(String ip, int port) {
		this.ip = ip;
		this.port = port;
		running = false;
		nConnections = 0;
	}

	@Override
	public void run() {
		try {
			serverSocket = new ServerSocket();
			serverSocket.bind(new InetSocketAddress(ip, port));

			Socket socket = null;

			running = true;

			while (running) {
				try {
					socket = serverSocket.accept();
					long ssid = nConnections++;
					new Thread(new ClientHandler(ssid, socket)).start();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				serverSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
