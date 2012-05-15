package nl.clockwork.virm.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import nl.clockwork.virm.logic.Recognizer;
import nl.clockwork.virm.ui.ServerView;

public class ClientHandler implements Runnable {
	private ServerView view;
	private boolean running;
	private Socket socket;
	private InputStream in;
	private OutputStream out;
	private long ssid;

	public ClientHandler(long ssid, Socket socket, ServerView view) {
		this.view = view;
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
					handlePacket(new DataPacket(b, in));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				view.addLogText(ssid, "Closing socket.");
				view.setConnectionStatus(ssid, "Disconnected");
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void handlePacket(DataPacket dp) throws IOException {
		switch (dp.getCommand()) {
		case Packets.PING:
			view.addLogText(ssid, "Received PING.");
			sendPing();
			break;
		case Packets.MAT:
			view.addLogText(ssid, "Received MAT.");
			handleMat(dp);
			break;
		case Packets.CLOSE:
			view.addLogText(ssid, "Received CLOSE.");
			handleClose();
			break;
		}
	}

	private void handleClose() {
		sendOk();
		running = false;
	}

	private void handleMat(DataPacket dp) {
		view.setConnectionStatus(ssid, "Reading capture");

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
		view.addLogText(ssid, "Sending MATCH.");
		Packet packet = new Packet();
		packet.addByte(Packets.MATCH);
		packet.addString(file);
		packet.send(out);
	}

	private void sendNoMatch() {
		view.addLogText(ssid, "Sending NO_MATCH.");
		Packet packet = new Packet();
		packet.addByte(Packets.NO_MATCH);
		packet.send(out);
	}

	private void sendPing() {
		view.addLogText(ssid, "Sending PING.");
		Packet packet = new Packet();
		packet.addByte(Packets.PING);
		packet.send(out);
	}

	private void sendOk() {
		view.addLogText(ssid, "Sending OK.");
		Packet packet = new Packet();
		packet.addByte(Packets.OK);
		packet.send(out);
	}
}
