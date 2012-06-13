package nl.clockwork.virm.server;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import nl.clockwork.virm.server.detect.Detector;
import nl.clockwork.virm.server.detect.Loader;
import nl.clockwork.virm.server.detect.painting.PaintingDetector;
import nl.clockwork.virm.server.detect.painting.RemotePaintingLoader;

public final class Factory {
	public static Loader createLoader() {
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream("conf/default.properties"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String host = prop.getProperty("db_host");
		String user = prop.getProperty("db_user");
		String pass = prop.getProperty("db_pass");
		String name = prop.getProperty("db_name");
		
		return new RemotePaintingLoader(host + name, user, pass);
	}
	
	public static Detector createDetector() {
		return new PaintingDetector(Factory.createLoader());
	}
}
