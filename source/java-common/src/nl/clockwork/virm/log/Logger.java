package nl.clockwork.virm.log;

public interface Logger {
	void write(Level level, String message);
	void write(String category, Level level, String message);
}
