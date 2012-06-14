package nl.clockwork.virm.log;

public enum Level {
	DEBUG, INFO, WARN, ERROR, FATAL;

	@Override
	public String toString() {
		String s = super.toString();
		return s.substring(0, 1) + s.substring(1).toLowerCase();
	}
}
