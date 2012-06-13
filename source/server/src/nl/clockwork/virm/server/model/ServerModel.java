package nl.clockwork.virm.server.model;

import nl.clockwork.virm.server.net.Connection;
import nl.clockwork.virm.server.net.ConnectionListener;
import nl.clockwork.virm.server.net.Server;
import nl.clockwork.virm.server.net.ServerListener;

public class ServerModel extends Model implements ServerListener, ConnectionListener {
	private Server server;
	
	public ServerModel(Server server) {
		this.server = server;
		
		this.server.addListener(this);
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
