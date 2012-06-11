package nl.clockwork.virm.server;

import java.util.Observable;

import nl.clockwork.virm.server.net.Server;

public class Model extends Observable {
	private Server server;
	private Thread serverThread;
	
	public Model() {
		server = new Server("127.0.0.1", 1337);
		serverThread = new Thread(server);
		serverThread.start();
	}
}
