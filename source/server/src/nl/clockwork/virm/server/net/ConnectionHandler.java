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
		Log.i(conn.toString(), String.format("Connected on %s:%s", conn.getHostAddress(), conn.getPort()));
		try {
			running = true;
			while (running) {
				byte command = conn.readByte();
				if (command > -1) {
					handlePacket(command, conn.readPacket());
				}
			}
		} catch (IOException e) {
			Log.e(conn.toString(), "IOException", e);
		} finally {
			try {
				conn.close();
				Log.i(conn.toString(), String.format("Closed on %s:%s", conn.getHostAddress(), conn.getPort()));
			} catch (IOException e) {
				Log.e(conn.toString(), "", e);
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
		Log.d(conn.toString(), "Received PING");
		sendPacket(PacketHeaders.PING);
		Log.d(conn.toString(), "Send PING");
	}
	
	private void handleMat(DataPacket dp) throws IOException {
		//Log.d(conn.toString(), "Received DETECT");
		int[][] mat = readMat(dp);

		Detectable result = detector.detect(mat);
		if (result == null) {
			sendPacket(PacketHeaders.NO_MATCH);
			//Log.d(conn.toString(), "Send NO_MATCH");
		} else {
			Log.i(conn.toString(), "Matched " + result.getName());
			sendMatch(result);
			//Log.d(conn.toString(), "Send MATCH");
		}
	}
	
	private void handleClose() {
		Log.d(conn.toString(), "Received CLOSE");
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
