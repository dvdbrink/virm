package nl.clockwork.virm.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import nl.clockwork.virm.ui.ServerView;

public class Server implements Runnable {
	private boolean running;
	private ServerSocket serverSocket;
	private ServerView view;
	private long nConnections;

	public Server(ServerView view) {
		this.view = view;
		running = false;
		nConnections = 0;
	}

	@Override
	public void run() {
		try {
			serverSocket = new ServerSocket();
			serverSocket.bind(new InetSocketAddress("172.19.2.62", 1337));

			Socket socket = null;

			running = true;

			while (running) {
				try {
					socket = serverSocket.accept();
					long ssid = nConnections++;
					view.addConnection(ssid, socket.getInetAddress()
							.getHostAddress(), Integer.toString(socket
							.getPort()), "Connected");
					new Thread(new ClientHandler(ssid, socket, view)).start();
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
