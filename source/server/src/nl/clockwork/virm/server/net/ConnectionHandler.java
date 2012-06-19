package nl.clockwork.virm.server.net;

import java.io.IOException;

import nl.clockwork.virm.log.Log;
import nl.clockwork.virm.net.DataPacket;
import nl.clockwork.virm.net.Packet;
import nl.clockwork.virm.net.PacketHeaders;
import nl.clockwork.virm.server.detect.Detectable;
import nl.clockwork.virm.server.detect.Detector;

public class ConnectionHandler implements Runnable {
	private Detector detector;
	private Connection conn;
	private boolean running;

	public ConnectionHandler(Detector detector, Connection conn) {
		this.detector = detector;
		this.conn = conn;
		
		running = false;
	}

	@Override
	public void run() {
		try {
			running = true;
			while (running) {
				byte command = conn.readByte();
				if (command > -1) {
					handlePacket(command, conn.readPacket());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
				Log.d(conn.getSSID() + "", String.format("Closed on %s:%s", conn.getHostAddress(), conn.getPort()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void handlePacket(byte command, DataPacket dp) throws IOException {
		switch (command) {
			case PacketHeaders.PING: 	handlePing(); 	break;
			case PacketHeaders.DETECT: 	handleMat(dp); 	break;
			case PacketHeaders.CLOSE: 	handleClose(); 	break;
		}
	}

	private void handlePing() throws IOException {
		Log.d(conn.getSSID() + "", "Received PING");
		sendPacket(PacketHeaders.PING);
		Log.d(conn.getSSID() + "", "Send PING");
	}
	
	private void handleMat(DataPacket dp) throws IOException {
		int[][] mat = readMat(dp);

		Detectable result = detector.detect(mat);
		if (result == null) {
			sendPacket(PacketHeaders.NO_MATCH);
			Log.d(conn.getSSID() + "", "Send NO_MATCH");
		} else {
			sendMatch(result);
			Log.d(conn.getSSID() + "", "Send MATCH (" + result.getName() + ")");
		}
	}
	
	private void handleClose() {
		Log.d(conn.getSSID() + "", "Received CLOSE");
		running = false;
	}
	
	private int[][] readMat(DataPacket dp) {
		int rows = dp.readInt();
		int cols = dp.readInt();
		
		int[][] matrix = new int[rows][cols];
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				matrix[row][col] = dp.readByte() & 0xFF;
			}
		}
		
		return matrix;
	}

	public void sendMatch(Detectable d) throws IOException {
		Packet packet = new Packet();
		packet.addByte(PacketHeaders.MATCH);
		packet.addString(d.getName());
		conn.send(packet);
	}
	
	public void sendPacket(byte command) throws IOException {
		Packet packet = new Packet();
		packet.addByte(command);
		conn.send(packet);
	}
}
