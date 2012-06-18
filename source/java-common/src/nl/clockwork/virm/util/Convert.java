package nl.clockwork.virm.util;

public final class Convert {
	public static byte[] shortToByteArray(short s) {
	    byte[] bytes = new byte[2];
	    for (int i = 0; i < 2; i++){
	    	bytes[i] = (byte) (s >>> (i * 8));
	    }
	    return bytes;
	}
	
	public static byte[] intToByteArray(int integer) {
		byte[] bytes = new byte[4];
		for (int i = 0; i < 4; i++) {
			bytes[i] = (byte) (integer >>> (i * 8));
		}
		return bytes;
	}

	public static byte[] longToByteArray(long l) {
	    byte[] bytes = new byte[8];
	    for (int i = 0; i < 8; i++){
	    	bytes[i] = (byte) (l >>> (i * 8));
	    }
	    return bytes;
	}
	
	public static int byteArrayToInt(byte[] bytes) {
		return byteArrayToInt(bytes, 0);
	}
	
	public static int byteArrayToInt(byte[] bytes, int offset) {
		int integer = 0;
		for (int i = 0; i < 4; i++) {
			integer |= (bytes[i + offset] & 0xFF) << (i << 3);
		}
		return integer;
	}
	
	public static byte[] hexStringToByteArray(String h) {
		byte[] bytes = new byte[h.length()/2];
		for (int i = 0; i < h.length()/2; i++) {
			bytes[i] = ((byte) Integer.parseInt(h.substring(i*2, (i*2)+2), 16));
		}
		return bytes;
	}
	
	public static String byteArrayToHexString(byte[] bytes) {
		String string = new String();
		for (int i = 0; i < bytes.length; i++) {
			string += "[" + i + "] 0x" + Integer.toHexString(bytes[i]) + " = " + (char) bytes[i] + "\n";
		}
		return string;
	}
}
