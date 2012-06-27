package nl.clockwork.virm.android.scanner;

import nl.clockwork.virm.android.Settings;
import nl.clockwork.virm.android.dataset.DataSet;

import org.opencv.core.MatOfDMatch;
import org.opencv.features2d.DescriptorMatcher;

import android.content.Context;

public abstract class BaseLocalOpenCVScanner extends BasicOpenCVScanner {
	protected DataSet dataSet;
	protected DescriptorMatcher matcher;
	protected MatOfDMatch matches;

	public BaseLocalOpenCVScanner(Context context, DataSet dataSet) {
		super(context);
		
		this.dataSet = dataSet;
		
		matcher = DescriptorMatcher.create(Settings.OPENCV_MATCHER);
		matches = new MatOfDMatch();
	}
}
