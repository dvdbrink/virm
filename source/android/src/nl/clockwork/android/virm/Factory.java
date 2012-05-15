package nl.clockwork.android.virm;

import nl.clockwork.android.virm.dataset.BasicDataSet;
import nl.clockwork.android.virm.dataset.DataSet;
import nl.clockwork.android.virm.dataset.Painting;
import nl.clockwork.android.virm.history.BasicHistory;
import nl.clockwork.android.virm.history.History;
import nl.clockwork.android.virm.loader.Loader;
import nl.clockwork.android.virm.loader.LocalLoader;
import nl.clockwork.android.virm.scanner.AsyncOpenCVScanner;
import nl.clockwork.android.virm.scanner.Scanner;
import nl.clockwork.android.virm.ui.preview.AndroidPreview;
import nl.clockwork.android.virm.ui.preview.Preview;

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
		return new LocalLoader(context);
	}

	public static Scanner createScanner(DataSet dataSet) {
		return new AsyncOpenCVScanner(dataSet);
	}

	public static Preview createPreview(Context context) {
		return new AndroidPreview(context);
	}
}
