package nl.clockwork.virm.log;

public class ConsoleLogger implements Logger {
	@Override
	public void write(Level level, String message) {
		write("", level, message);
	}

	@Override
	public void write(String category, Level level, String message) {
		String out = "";
		switch (level) {
			case DEBUG: out += "[DEBUG]"; break;
			case INFO:  out += "[INFO]";  break;
			case WARN: 	out += "[WARN]";  break;
			case ERROR: out += "[ERROR]"; break;
			case FATAL: out += "[FATAL]"; break;
		}
		System.out.println(out + message);
	}
}
