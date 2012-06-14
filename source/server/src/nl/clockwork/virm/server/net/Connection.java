package nl.clockwork.virm.server.net;

import java.io.IOException;
import java.net.Socket;
import java.util.Observable;

import nl.clockwork.virm.net.DataPacket;
import nl.clockwork.virm.net.Packet;

public class Connection extends Observable {
	private Long ssid;
	private Socket socket;
	private Status status;
	
	public Connection(Long ssid, Socket socket) {
		this.ssid = ssid;
		this.socket = socket;
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
		
		setChanged();
		notifyObservers();
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
	}
}
