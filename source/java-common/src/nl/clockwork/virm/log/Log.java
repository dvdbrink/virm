package nl.clockwork.virm.log;

public final class Log {
	public static void i(String message)
	{
		System.out.println("[INFO] " + message);
	}

	public static void d(String message)
	{
		System.out.println("[DEBUG] " + message);
	}
	
	public static void e(String message)
	{
		System.out.println("[ERROR] " + message);
	}
}
