package nl.clockwork.virm.server.net;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import nl.clockwork.virm.log.Log;
import nl.clockwork.virm.server.detect.Detector;

public class Server implements Runnable {
	private long lastSSID;
	private Detector detector;
	private String ip;
	private int port;
	private ServerSocket serverSocket;
	private boolean running;
	private List<ServerListener> listeners;

	public Server(Detector detector) {
		this.detector = detector;
		
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream("conf/default.properties"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		ip = prop.getProperty("server_host");
		port = Integer.parseInt(prop.getProperty("server_port"));
		
		serverSocket = null;
		running = false;
		listeners = new ArrayList<ServerListener>();
	}

	@Override
	public void run() {
		try {
			serverSocket = new ServerSocket();
			serverSocket.bind(new InetSocketAddress(ip, port));
			Log.i("Server", String.format("Started on %s:%s", ip, port));

			lastSSID = 0;
			Socket socket = null;
			Connection conn = null;
			running = true;
			while (running) {
				try {
					socket = serverSocket.accept();
					conn = new Connection(lastSSID++, socket);
					conn.setStatus(Status.CONNECTED);
					Log.i("Server", String.format("New connection %s:%s", conn.getHostAddress(), conn.getPort()));
					
					onConnection(conn);
					
					new Thread(new ConnectionHandler(detector, conn)).start();
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
	
	private void onConnection(Connection conn) {
		for (ServerListener l : listeners) {
			l.onConnection(conn);
		}
	}
}
