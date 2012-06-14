package nl.clockwork.virm.server.view;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Observable;

import nl.clockwork.virm.log.Level;
import nl.clockwork.virm.log.Logger;

public class BasicGUILogger extends Observable implements Logger {
	private static final String DATETIME_FORMAT = "HH:mm:ss";
	
	@Override
	public void write(Level level, String message) {
		write("", level, message);
	}

	@Override
	public void write(String category, Level level, String message) {
		setChanged();
		notifyObservers(new String[] { now(), level.toString(), category, message });
	}
	
	private String now() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(DATETIME_FORMAT);
		return sdf.format(cal.getTime());
	}
}
