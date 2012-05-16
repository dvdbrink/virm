package nl.clockwork.virm.server.tmp;

import java.io.IOException;
import java.net.UnknownHostException;

public class MainClient {
	public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException {
		for(int i=0; i < 10; i++) {
			new Thread(new Client()).start();
		}
	}
}
