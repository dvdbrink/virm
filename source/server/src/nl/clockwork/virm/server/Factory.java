package nl.clockwork.virm.server;

import nl.clockwork.virm.server.detect.Detector;
import nl.clockwork.virm.server.detect.Loader;
import nl.clockwork.virm.server.detect.painting.PaintingDetector;
import nl.clockwork.virm.server.detect.painting.RemotePaintingLoader;

public final class Factory {
	public static Loader getLoader() {
		return RemotePaintingLoader.get();
	}
	
	public static Detector getDetector() {
		return PaintingDetector.get();
	}
}
