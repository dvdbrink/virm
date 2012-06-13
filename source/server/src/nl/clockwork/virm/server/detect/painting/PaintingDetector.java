package nl.clockwork.virm.server.detect.painting;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import nl.clockwork.virm.server.Factory;
import nl.clockwork.virm.server.detect.Detectable;
import nl.clockwork.virm.server.detect.Detector;

public final class PaintingDetector implements Detector {
	private static PaintingDetector instance = null;
	public static PaintingDetector get() {
		if (instance == null) {
			instance = new PaintingDetector();
		}
		return instance;
	}
	
	static {
		System.loadLibrary("recognize");
	}
	
	private native int nativeInit(int threshold);
	private native boolean nativeAddTrainedDescriptor(int rows, int cols, int[][] mat);
	private native int nativeDetect(int rows, int cols, int[][] mat);
	
	private List<Detectable> paintings;
	
	private PaintingDetector() {
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream("conf/default.properties"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		int threshold = Integer.parseInt(prop.getProperty("detection_threshold"));
		nativeInit(threshold);
		
		paintings = Factory.getLoader().load();
		for (Detectable d : paintings) {
			Painting p = (Painting)d;
			nativeAddTrainedDescriptor(p.getData().length, p.getData()[0].length, p.getData());
		}
	}
	
	@Override
	public synchronized Detectable detect(Object arg) {
		if (arg instanceof int[][]) {
			int[][] mat = (int[][])arg;
			int loc = nativeDetect(mat.length, mat[0].length, mat);
			Detectable result = paintings.get(loc);
			return result;
		}
		return null;
	}
}
