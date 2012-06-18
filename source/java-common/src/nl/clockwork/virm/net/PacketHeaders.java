package nl.clockwork.virm.net;

public final class PacketHeaders {
	public static final byte PING 		= 0x01;
	public static final byte OK 		= 0x02;
	public static final byte FAIL 		= 0x03;
	public static final byte CLOSE 		= 0x04;
	public static final byte DETECT 	= 0x05;
	public static final byte MATCH 		= 0x06;
	public static final byte NO_MATCH 	= 0x07;
}
