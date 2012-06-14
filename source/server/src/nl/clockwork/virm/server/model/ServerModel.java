package nl.clockwork.virm.server.model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import nl.clockwork.virm.server.net.Connection;
import nl.clockwork.virm.server.net.ConnectionListener;
import nl.clockwork.virm.server.net.Server;
import nl.clockwork.virm.server.net.ServerListener;

public class ServerModel extends Model implements ServerListener, ConnectionListener {
	private Server server;
	
	public ServerModel() {
		// TODO settings
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream("conf/default.properties"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String host = prop.getProperty("server_host");
		int port = Integer.parseInt(prop.getProperty("server_port"));
		
		server = new Server(host, port);
		server.addListener(this);
	}

	public void startServer() {
		new Thread(server).start();
	}
	
	@Override
	public void onConnection(Connection c) {
		setChanged();
		notifyObservers(c);
	}

	@Override
	public void onStatusUpdate(Connection c) {
		setChanged();
		notifyObservers(c);
	}
}
