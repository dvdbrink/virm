package nl.clockwork.virm.server.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server implements Runnable {	
	private String ip;
	private int port;
	private boolean running;
	private ServerSocket serverSocket;
	private List<ServerListener> listeners;

	public Server(String ip, int port) {
		this.ip = ip;
		this.port = port;
		running = false;
		serverSocket = null;
		listeners = new ArrayList<ServerListener>();
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
					onConnection(socket);
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
	
	public void addListener(ServerListener l) {
		listeners.add(l);
	}
	
	private void onConnection(Socket s) {
		Connection conn = new Connection(0, s);		
		new Thread(new ConnectionHandler(conn)).start();
		
		for (ServerListener l : listeners) {
			l.onConnection(conn);
		}
	}
}
