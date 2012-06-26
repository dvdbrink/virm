package nl.clockwork.virm.server.model;

import nl.clockwork.virm.server.Factory;
import nl.clockwork.virm.server.detect.Detector;
import nl.clockwork.virm.server.detect.Loader;
import nl.clockwork.virm.server.net.Connection;
import nl.clockwork.virm.server.net.ConnectionListener;
import nl.clockwork.virm.server.net.Server;
import nl.clockwork.virm.server.net.ServerListener;

public class ServerModel extends Model implements ServerListener, ConnectionListener {
	private Loader loader;
	private Detector detector;
	private Server server;
	
	public ServerModel() {
		loader = Factory.createLoader();
		detector = Factory.createDetector(loader);
		server = new Server(detector);
		server.addListener(this);
		
		startServer();
	}

	private void startServer() {
		new Thread(server).start();
	}
	
	@Override
	public void onConnection(Connection c) {
		c.addListener(this);
		
		setChanged();
		notifyObservers(c);
	}
	
	@Override
	public void onStatusUpdate(Connection c) {
		setChanged();
		notifyObservers(c);
	}
}
