package nl.clockwork.virm.server;

import java.io.File;

import nl.clockwork.virm.log.Log;


public final class Recognizer {
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
		mats = new String[files.length/* * 20*/];
		for (int i = 0; i < files.length; i++) {
			//for (int j = 0; j < 20; j++)
				mats[i/**20+j*/] = files[i].getPath();
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
		return "null";
	}
	
	public synchronized String detectMat(int[][] mat) {
		long start = System.currentTimeMillis();
		int loc = nativeDetectMat(mat.length, mat[0].length, mat);
		long stop = System.currentTimeMillis();
		Log.d("Detection time: " + (stop - start) + "ms");
		if (loc > -1) {
			return mats[loc];
		}
		return "null";
	}
}
