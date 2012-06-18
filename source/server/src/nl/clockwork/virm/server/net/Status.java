package nl.clockwork.virm.server.net;

public enum Status {
	CONNECTED, WAITING, READING_PACKET, HANDLING_PACKET, SENDING_PACKET, DISCONNECTED;

	@Override
	public String toString() {
		String s = super.toString();
		return s.substring(0, 1) + s.substring(1).toLowerCase();
	}
}
