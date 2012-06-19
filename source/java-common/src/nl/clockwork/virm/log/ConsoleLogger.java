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
	public void write(Level level, String message, Throwable t) {
		write(level, message);
		t.printStackTrace();
	}
	
	@Override
	public void write(String category, Level level, String message) {
		String out = String.format("[%s] [%s]", now(), level);
		if (!category.isEmpty()) {
			out += " [" + category + "]";
		}
		System.out.println(out + " " + message);
	}

	@Override
	public void write(String category, Level level, String message, Throwable t) {
		write(category, level, message);
		t.printStackTrace();
	}
	
	// TODO util
	private String now() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(DATETIME_FORMAT);
		return sdf.format(cal.getTime());
	}
}
