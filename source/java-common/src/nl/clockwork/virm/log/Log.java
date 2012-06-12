package nl.clockwork.virm.log;

public final class Log {
	private static Logger logger;
	private static Logger getLogger() {
		if (logger == null) {
			logger = new ConsoleLogger();
		}
		return logger;
	}
	
	public static void i(String message)
	{
		getLogger().write(Level.INFO, message);
	}

	public static void d(String message)
	{
		getLogger().write(Level.DEBUG, message);
	}
	
	public static void w(String message)
	{
		getLogger().write(Level.WARN, message);
	}
	
	public static void e(String message)
	{
		getLogger().write(Level.ERROR, message);
	}
	
	public static void f(String message)
	{
		getLogger().write(Level.FATAL, message);
	}
}
