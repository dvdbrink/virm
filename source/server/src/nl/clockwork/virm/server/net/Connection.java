package nl.clockwork.virm.server.net;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import nl.clockwork.virm.net.DataPacket;
import nl.clockwork.virm.net.Packet;

public class Connection extends Observable {
	private Long ssid;
	private Socket socket;
	private Status status;
	private List<ConnectionListener> listeners;
	
	public Connection(Long ssid, Socket socket) {
		this.ssid = ssid;
		this.socket = socket;
		
		listeners = new ArrayList<ConnectionListener>();
	}
	
	public Long getSSID() {
		return ssid;
	}
	
	public Status getStatus() {
		return status;
	}
	
	public String getHostAddress() {
		return socket.getInetAddress().getHostAddress();
	}
	
	public int getPort() {
		return socket.getPort();
	}
	
	public void setStatus(Status status) {
		this.status = status;
		onStatusChanged();
	}
	
	public byte readByte() throws IOException {
		return (byte) socket.getInputStream().read();
	}
	
	public DataPacket readPacket() throws IOException {
		return new DataPacket(socket.getInputStream());
	}
	
	public void send(Packet p) throws IOException {
		p.send(socket.getOutputStream());
	}
	
	public void close() throws IOException {
		socket.close();
		setStatus(Status.DISCONNECTED);
	}
	
	public void addListener(ConnectionListener l) {
		listeners.add(l);
	}
	
	private void onStatusChanged() {
		for (ConnectionListener l : listeners) {
			l.onStatusUpdate(this);
		}
	}
	
	@Override
	public String toString() {
		return "SSID:" + this.getSSID();
	}
}
