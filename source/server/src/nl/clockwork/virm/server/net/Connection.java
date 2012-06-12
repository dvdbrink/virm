package nl.clockwork.virm.server.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Observable;

public class Connection extends Observable {
	private long ssid;
	private Socket socket;
	private Status status;
	
	public Connection(long ssid, Socket socket) {
		this.ssid = ssid;
		this.socket = socket;
		status = Status.UNINITIATED;
	}
	
	public long getSSID() {
		return ssid;
	}
	
	public Socket getSocket() {
		return socket;
	}
	
	public Status getStatus() {
		return status;
	}
	
	public String getStatusAsString() {
		switch (status) {
			case UNINITIATED:	return "uninitiated";
			case CONNECTED:		return "connected";
			case DISCONNECTED:	return "disconnected";
			default: 			return "";
		}
	}
	
	public String getIp() {
		return socket.getInetAddress().getHostAddress();
	}
	
	public int getPort() {
		return socket.getPort();
	}
	
	public InputStream getInputStream() throws IOException {
		return socket.getInputStream();
	}
	
	public OutputStream getOutputStream() throws IOException {
		return socket.getOutputStream();
	}
	
	public void setStatus(Status status) {
		this.status = status;
		notifyObservers();
	}
	
	public void close() throws IOException {
		socket.close();
	}
}
