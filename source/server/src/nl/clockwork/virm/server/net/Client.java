package nl.clockwork.virm.server.net;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import nl.clockwork.virm.net.Packets;

public class Client {
	private Socket socket;
	private OutputStream out;

	public Client() throws UnknownHostException, IOException, InterruptedException {
		socket = new Socket();
		socket.connect(new InetSocketAddress("172.19.2.30", 1337));
		out = socket.getOutputStream();
		out.write(Packets.CLOSE);
		out.flush();
	}

	public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException {
		new Client();
	}
}
