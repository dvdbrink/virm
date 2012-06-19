package nl.clockwork.virm.server;

import nl.clockwork.virm.server.detect.Detector;
import nl.clockwork.virm.server.detect.Loader;
import nl.clockwork.virm.server.detect.painting.LocalPaintingLoader;
import nl.clockwork.virm.server.detect.painting.PaintingDetector;

public final class Factory {
	public static Loader createLoader() {
		return new LocalPaintingLoader();
	}
	
	public static Detector createDetector(Loader loader) {
		return new PaintingDetector(loader);
	}
}
