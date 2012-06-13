package nl.clockwork.virm.net;

import java.io.IOException;
import java.io.InputStream;

import nl.clockwork.virm.util.Convert;

public class DataPacket {
	private InputStream in;

	public DataPacket(InputStream in) {
		this.in = in;
	}
	
	public byte readByte() {
		return readBytes(1)[0];
	}

	public int readInt() {
		return Convert.byteArrayToInt(readBytes(4));
	}
	
	public String readString() {
		int length = readInt();
		return new String(readBytes(length));
	}

	private byte[] readBytes(int length) {
		byte[] buffer = new byte[length];
		try {
			in.read(buffer);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffer;
	}
}
