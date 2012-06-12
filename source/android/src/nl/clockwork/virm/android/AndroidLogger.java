package nl.clockwork.virm.android;

import android.util.Log;
import nl.clockwork.virm.log.Level;
import nl.clockwork.virm.log.Logger;

public class AndroidLogger implements Logger {
	@Override
	public void write(Level level, String message) {
		write("", level, message);
	}
	
	@Override
	public void write(String category, Level level, String message) {
		String tag = C.TAG + (!category.isEmpty() ? "." + category : "");
		switch (level) {
			case DEBUG: Log.d(tag, message); break;
			case INFO: 	Log.i(tag, message); break;
			case WARN: 	Log.w(tag, message); break;
			case ERROR: Log.e(tag, message); break;
			case FATAL: Log.e(tag, message); break;
		}
	}
}
