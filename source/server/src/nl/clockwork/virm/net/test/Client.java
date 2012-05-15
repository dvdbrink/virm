package nl.clockwork.virm.net.test;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import nl.clockwork.virm.net.Packets;
import nl.clockwork.virm.util.Convert;

public class Client implements Runnable{
	private Socket socket;
	private OutputStream out;

	public Client() throws UnknownHostException, IOException, InterruptedException {
		socket = new Socket();
		socket.connect(new InetSocketAddress("172.19.2.62", 1337));
		out = socket.getOutputStream();
	}

	@Override
	public void run() {
		int packetCount=0;
		
		while(true) {
			byte[] buffer = new byte[209*32];
			try {
				out.write(Packets.MAT);
				out.write(Convert.intToByteArray(209));
				out.write(Convert.intToByteArray(32));
				out.write(buffer);
				out.flush();
				Thread.sleep(1000);				
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			packetCount++;
			System.out.println("Packets sent: " + packetCount);
		}	
	}

//	public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException {
//		new Client();
//	}
}
