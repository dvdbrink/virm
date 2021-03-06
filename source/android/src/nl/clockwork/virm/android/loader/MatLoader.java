package nl.clockwork.virm.android.loader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import nl.clockwork.virm.android.Settings;
import nl.clockwork.virm.android.Factory;
import nl.clockwork.virm.android.Virm;
import nl.clockwork.virm.android.dataset.DataSet;
import nl.clockwork.virm.net.DataPacket;

import org.opencv.core.CvType;
import org.opencv.core.Mat;

import android.content.Context;
import android.util.Log;

public class MatLoader implements Loader {
	private Context context;

	public MatLoader(Context context) {
		this.context = context;
	}
	
	@Override
	public DataSet load(OnProgressUpdateCallback onProgressUpdateCallback) {
		DataSet dataset = Factory.createDataSet();
		try {
			String[] fileNames = context.getAssets().list(Settings.DATASET_LOCATION);
			int count = 0;
			for (String fileName : fileNames) {
				Mat descriptor = loadMat(Settings.DATASET_LOCATION + File.separator + fileName);
				dataset.add(Factory.createDataSetItem(fileName, descriptor));
				onProgressUpdateCallback.onProgressUpdate((int) ((++count / (float) fileNames.length) * 100));
			}
			Log.i(Virm.TAG, count + " assets loaded");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return dataset;
	}

	private Mat loadMat(String fileName) {
		Mat descriptor = null;
		InputStream in = null;
		try {
			in = context.getAssets().open(fileName);
			DataPacket dp = new DataPacket(in);
			// first 8 bytes in .mat file are rows and cols
			int rows = dp.readInt();
			int cols = dp.readInt();
			descriptor = new Mat(rows, cols, CvType.CV_8UC1);
			// read unsigned chars from .mat file
			for (int row = 0; row < rows; row++) {
				for (int col = 0; col < cols; col++) {
					descriptor.put(row, col, dp.readByte() & 0xFF); // byte to unsigned byte (as int) hack
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return descriptor;
	}
}
