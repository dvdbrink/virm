package nl.clockwork.virm.android;

public enum Mode {
	LOCAL, REMOTE;
	
	@Override
	public String toString() {
		String s = super.toString();
		return s.substring(0, 1) + s.substring(1).toLowerCase();
	}
}
