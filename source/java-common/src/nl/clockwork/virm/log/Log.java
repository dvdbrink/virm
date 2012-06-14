package nl.clockwork.virm.log;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public final class Log {
	private static boolean initiated = false;
	private static Map<String, Logger> loggers;
	
	public static void init(String[] loggerNames) {
		loggers = new HashMap<String, Logger>();
		
		for (int i = 0; i < loggerNames.length; i++) {
			try {
				String name = loggerNames[i];
				Class<?> cl = Class.forName(loggerNames[i]);
				Constructor<?> co = cl.getConstructor();
				loggers.put(name, (Logger) co.newInstance());
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		
		initiated = true;
	}
	
	public static Logger get(String name) {
		if (loggers.containsValue(name)) {
			return loggers.get(name);
		}
		return null;
	}
	
	public static void log(Level level, String message) {
		if (initiated) {
			for (Logger logger : loggers.values()) {
				logger.write(level, message);
			}
		} else {
			System.err.println("Logger has not been initiated.");
		}
	}
	
	public static void log(String category, Level level, String message) {
		if (initiated) {
			for (Logger logger : loggers.values()) {
				logger.write(category, level, message);
			}
		} else {
			System.err.println("Logger has not been initiated.");
		}
	}
	
	public static void i(String message)
	{
		log(Level.INFO, message);
	}

	public static void d(String message)
	{
		log(Level.DEBUG, message);
	}
	
	public static void w(String message)
	{
		log(Level.WARN, message);
	}
	
	public static void e(String message)
	{
		log(Level.ERROR, message);
	}
	
	public static void f(String message)
	{
		log(Level.FATAL, message);
	}
	
	public static void i(String category, String message)
	{
		log(category, Level.INFO, message);
	}

	public static void d(String category, String message)
	{
		log(category, Level.DEBUG, message);
	}
	
	public static void w(String category, String message)
	{
		log(category, Level.WARN, message);
	}
	
	public static void e(String category, String message)
	{
		log(category, Level.ERROR, message);
	}
	
	public static void f(String category, String message)
	{
		log(category, Level.FATAL, message);
	}
}
