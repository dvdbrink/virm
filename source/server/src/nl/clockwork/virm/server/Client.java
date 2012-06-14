package nl.clockwork.virm.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import nl.clockwork.virm.log.Log;
import nl.clockwork.virm.net.DataPacket;
import nl.clockwork.virm.net.Packet;
import nl.clockwork.virm.net.PacketHeaders;

public class Client {
	private Socket socket;
	private InputStream in;
	private OutputStream out;

	public Client() throws UnknownHostException, IOException, InterruptedException {
		Log.init(new String[] {"nl.clockwork.virm.log.ConsoleLogger"});
		
		socket = new Socket();
		socket.connect(new InetSocketAddress("172.19.2.30", 1337));
		in = socket.getInputStream();
		out = socket.getOutputStream();
				
		// send ping
		Packet p = new Packet();
		p.addByte(PacketHeaders.PING);
		p.send(out);
		
		File folder = new File("res/descriptor");
		File[] files = folder.listFiles();
		for (int i = 0; i < files.length; i++) {
			p = new Packet();
			p.addByte(PacketHeaders.DETECT);
			p.addBytes(getBytesFromFile(files[i]));
			p.send(out);
			
			waitForResult();
			
			Thread.sleep(1000);
		}
		
		// close gracefully
		p = new Packet();
		p.addByte(PacketHeaders.CLOSE);
		p.send(out);
	}
	
	private void waitForResult() throws IOException {
		boolean result = false;
		while (!result) {
			byte command = (byte) in.read();
			if (command > -1) {
				switch (command) {
					case PacketHeaders.MATCH:
						DataPacket dp = new DataPacket(in);
						Log.d("Matched " + dp.readString());
						result = true;
						break;
					case PacketHeaders.NO_MATCH:
						Log.d("Received NO_MATCH");
						result = true;
						break;
				}
			}
		}
	}

	private byte[] getBytesFromFile(File file) throws IOException {
		InputStream is = new FileInputStream(file);

		long length = file.length();

		if (length > Integer.MAX_VALUE) {
			throw new IOException("File is too large " + file.getName());
		}

		byte[] bytes = new byte[(int) length];

		int offset = 0;
		int numRead = 0;
		while (offset < bytes.length
				&& (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
			offset += numRead;
		}

		if (offset < bytes.length) {
			throw new IOException("Could not completely read file " + file.getName());
		}

		is.close();
		return bytes;
	}

	public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException {
		new Client();
	}
}
