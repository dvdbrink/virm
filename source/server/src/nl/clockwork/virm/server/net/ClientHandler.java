package nl.clockwork.virm.server.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import nl.clockwork.virm.net.DataPacket;
import nl.clockwork.virm.net.Packet;
import nl.clockwork.virm.net.Packets;
import nl.clockwork.virm.server.Recognizer;

public class ClientHandler implements Runnable {
	private boolean running;
	private Socket socket;
	private InputStream in;
	private OutputStream out;
	private long ssid;

	public ClientHandler(long ssid, Socket socket) {
		this.running = false;
		try {
			this.ssid = ssid;
			this.socket = socket;
			this.in = this.socket.getInputStream();
			this.out = this.socket.getOutputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
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
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void handlePacket(byte command, DataPacket dp) throws IOException {
		switch (command) {
		case Packets.PING:
			sendPing();
			break;
		case Packets.MAT:
			handleMat(dp);
			break;
		case Packets.CLOSE:
			handleClose();
			break;
		}
	}

	private void handleClose() {
		sendOk();
		running = false;
	}

	private void handleMat(DataPacket dp) {
		int rows = dp.readInt();
		int cols = dp.readInt();
		int[][] matrix = new int[rows][cols];
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				matrix[row][col] = dp.readInt();
			}
		}

		String value = Recognizer.get().detectMat(matrix);
		if (value == null || value.isEmpty() || value.equalsIgnoreCase("null")) {
			sendNoMatch();
		} else {
			sendMatch(value);
		}
	}

	private void sendMatch(String file) {
		Packet packet = new Packet();
		packet.addByte(Packets.MATCH);
		packet.addString(file);
		packet.send(out);
	}

	private void sendNoMatch() {
		Packet packet = new Packet();
		packet.addByte(Packets.NO_MATCH);
		packet.send(out);
	}

	private void sendPing() {
		Packet packet = new Packet();
		packet.addByte(Packets.PING);
		packet.send(out);
	}

	private void sendOk() {
		Packet packet = new Packet();
		packet.addByte(Packets.OK);
		packet.send(out);
	}
}
