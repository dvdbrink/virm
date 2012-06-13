package nl.clockwork.virm.server.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import nl.clockwork.virm.net.DataPacket;
import nl.clockwork.virm.net.Packet;
import nl.clockwork.virm.net.Packets;
import nl.clockwork.virm.server.Factory;
import nl.clockwork.virm.server.detect.Detectable;

public class ConnectionHandler implements Runnable {
	private Connection conn;
	private InputStream in;
	private OutputStream out;
	private boolean running;

	public ConnectionHandler(Connection conn) {
		try {
			this.conn = conn;
			in = this.conn.getInputStream();
			out = this.conn.getOutputStream();
			running = false;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		conn.setStatus(Status.CONNECTED);
		try {
			running = true;
			while (running) {
				byte b = (byte) in.read();
				if (b > -1) {
					handlePacket(b, new DataPacket(in));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
				conn.setStatus(Status.DISCONNECTED);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void handlePacket(byte command, DataPacket dp) throws IOException {
		switch (command) {
			case Packets.PING:
				sendPacket(Packets.PING);
				break;
			case Packets.DETECT:
				handleMat(dp);
				break;
			case Packets.CLOSE:
				running = false;
				sendPacket(Packets.OK);
				break;
		}
	}

	private void handleMat(DataPacket dp) {		
		int[][] mat = readMat(dp);

		Detectable result = Factory.getDetector().detect(mat);
		if (result == null) {
			sendPacket(Packets.NO_MATCH);
		} else {
			sendMatch(result);
		}
	}
	
	private int[][] readMat(DataPacket dp) {
		int rows = dp.readInt();
		int cols = dp.readInt();
		int[][] matrix = new int[rows][cols];
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				matrix[row][col] = dp.readInt();
			}
		}
		return matrix;
	}

	public void sendMatch(Detectable d) {
		Packet packet = new Packet();
		packet.addByte(Packets.MATCH);
		packet.addString(d.getName());
		packet.send(out);
	}
	
	public void sendPacket(byte command) {
		Packet packet = new Packet();
		packet.addByte(command);
		packet.send(out);
	}
}
