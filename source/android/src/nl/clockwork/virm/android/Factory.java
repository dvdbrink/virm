package nl.clockwork.virm.android;

import nl.clockwork.virm.android.dataset.BasicDataSet;
import nl.clockwork.virm.android.dataset.DataSet;
import nl.clockwork.virm.android.dataset.Painting;
import nl.clockwork.virm.android.history.BasicHistory;
import nl.clockwork.virm.android.history.History;
import nl.clockwork.virm.android.loader.Loader;
import nl.clockwork.virm.android.loader.MatLoader;
import nl.clockwork.virm.android.scanner.AsyncLocalOpenCVScanner;
import nl.clockwork.virm.android.scanner.RemoteOpenCVScanner;
import nl.clockwork.virm.android.scanner.Scanner;
import nl.clockwork.virm.android.ui.preview.AndroidPreview;
import nl.clockwork.virm.android.ui.preview.Preview;

import org.opencv.core.Mat;

import android.content.Context;

public final class Factory {
	public static DataSet createDataSet() {
		return new BasicDataSet();
	}

	public static DataSet.Item createDataSetItem(String name, Mat descriptor) {
		return new Painting(name, descriptor);
	}

	public static History createHistory() {
		return new BasicHistory();
	}

	public static Loader createLoader(Context context) {
		return new MatLoader(context);
	}

	public static Scanner createLocalScanner(Context context, DataSet dataSet) {
		return new AsyncLocalOpenCVScanner(context, dataSet);
	}
	
	public static Scanner createRemoteScanner(Context context) {
		return new RemoteOpenCVScanner(context);
	}

	public static Preview createPreview(Context context) {
		return new AndroidPreview(context);
	}
}
