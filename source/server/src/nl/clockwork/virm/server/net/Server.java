package nl.clockwork.virm.server.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import nl.clockwork.virm.log.Log;
import nl.clockwork.virm.server.Factory;

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
		
		// TODO hack
		Factory.getDetector();
	}

	@Override
	public void run() {
		try {
			serverSocket = new ServerSocket();
			serverSocket.bind(new InetSocketAddress(ip, port));
			Log.i("Server", String.format("Started on %s:%s", ip, port));

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
				Log.i("Server", String.format("Closed on %s:%s", ip, port));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void addListener(ServerListener l) {
		listeners.add(l);
	}
	
	private void onConnection(Socket s) {
		Connection conn = new Connection(0L, s);
		conn.setStatus(Status.CONNECTED);
		Log.i("Server", String.format("New connection %s:%s", conn.getHostAddress(), conn.getPort()));
		
		new Thread(new ConnectionHandler(conn)).start();
		
		for (ServerListener l : listeners) {
			l.onConnection(conn);
		}
	}
}
