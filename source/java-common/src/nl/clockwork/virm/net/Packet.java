package nl.clockwork.virm.net;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import nl.clockwork.virm.util.Convert;

public class Packet {
	private ByteArrayOutputStream buffer;
	
	public Packet() {
		buffer = new ByteArrayOutputStream();
	}
	
	public void addByte(byte b) {
		buffer.write(b);
	}
	
	public void addByte(int i) {
		addByte((byte) i);
	}
	
	public void addBytes(byte[] bytes) {
		for (int i = 0; i < bytes.length; i++) {
			addByte(bytes[i]);
		}
	}
	
	public void addShort(short s) {
		addBytes(Convert.shortToByteArray(s));
	}
	
	public void addInt(int i) {
		addBytes(Convert.intToByteArray(i));
	}
	
	public void addLong(long l) {
		addBytes(Convert.longToByteArray(l));
	}
	
	public void addString(String s) {
		addInt(s.length());
		addBytes(s.getBytes());
	}
	
	public void send(OutputStream out) {
		try {
			out.write(buffer.toByteArray());
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
