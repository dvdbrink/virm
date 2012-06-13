package nl.clockwork.virm.server.detect.painting;

import java.io.File;

import nl.clockwork.virm.log.Log;
import nl.clockwork.virm.server.detect.Detectable;
import nl.clockwork.virm.server.detect.Detector;

public final class Recognizer implements Detector {
	private static Recognizer instance;
	public static Recognizer get() {
		if (instance == null) {
			instance = new Recognizer();
		}
		return instance;
	}
	
	static {
		System.loadLibrary("recognize");
	}
	
	private native boolean nativeInit(String[] files);
	private synchronized native int nativeDetect(String file);
	private synchronized native int nativeDetectMat(int rows, int cols, int[][] mat);
		
	private String[] mats;
	
	private Recognizer() {
		File folder = new File("res/descriptor");
		File[] files = folder.listFiles();
		mats = new String[files.length];
		for (int i = 0; i < files.length; i++) {
				mats[i] = files[i].getPath();
		}
		Log.d("Files in memory: " + mats.length);
		nativeInit(mats);
	}
	
	public synchronized String detect(String file) {
		long start = System.currentTimeMillis();
		int loc = nativeDetect(file);
		long stop = System.currentTimeMillis();
		Log.d("Detection time: " + (stop - start) + "ms");
		if (loc > -1) {
			return mats[loc];
		}
		return null;
	}
	
	public synchronized String detectMat(int[][] mat) {
		long start = System.currentTimeMillis();
		int loc = nativeDetectMat(mat.length, mat[0].length, mat);
		long stop = System.currentTimeMillis();
		Log.d("Detection time: " + (stop - start) + "ms");
		if (loc > -1) {
			return mats[loc];
		}
		return null;
	}
	
	@Override
	public Detectable detect(Object arg) {
		// TODO Auto-generated method stub
		return null;
	}
}
