package nl.clockwork.virm.log;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ConsoleLogger implements Logger {
	private static final String DATETIME_FORMAT = "HH:mm:ss";
	
	@Override
	public void write(Level level, String message) {
		write("", level, message);
	}

	@Override
	public void write(String category, Level level, String message) {
		String out = "[" + now() + "]";
		switch (level) {
			case DEBUG: out += " [DEBUG]"; break;
			case INFO:  out += " [INFO]";  break;
			case WARN: 	out += " [WARN]";  break;
			case ERROR: out += " [ERROR]"; break;
			case FATAL: out += " [FATAL]"; break;
		}
		if (!category.isEmpty()) {
			out += " [" + category + "]";
		}
		System.out.println(out + " " + message);
	}
	
	private String now() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(DATETIME_FORMAT);
		return sdf.format(cal.getTime());
	}
}
