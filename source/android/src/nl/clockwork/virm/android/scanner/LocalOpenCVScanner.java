package nl.clockwork.virm.android.scanner;

import nl.clockwork.virm.android.Settings;
import nl.clockwork.virm.android.dataset.DataSet;

import org.opencv.core.MatOfDMatch;
import org.opencv.features2d.DMatch;
import org.opencv.features2d.DescriptorMatcher;

import android.content.Context;

public class LocalOpenCVScanner extends BasicOpenCVScanner {
	protected DataSet dataSet;
	protected DescriptorMatcher matcher;
	protected MatOfDMatch matches;

	public LocalOpenCVScanner(Context context, DataSet dataSet) {
		super(context);
		
		this.dataSet = dataSet;
		
		matcher = DescriptorMatcher.create(Settings.OPENCV_MATCHER_ALGORITHM);
		matches = new MatOfDMatch();
	}

	@Override
	public void scan(byte[] data, int width, int height) {
		super.scan(data, width, height);

		long start = System.currentTimeMillis();
		
		detector.detect(yuvResized, keypoints);
		extractor.compute(yuvResized, keypoints, descriptor);
		DataSet.Item match = calculateBestMatch();
		
		long stop = System.currentTimeMillis();
		
		if (match != null) {
			fireMatchEvent(new Result(match.getName(), stop - start));
		} else {
			fireNoMatchEvent();
		}
	}

	private DataSet.Item calculateBestMatch() {
		DataSet.Item bestMatch = null;
		int bestMatchMatches = 0;
		
		for (DataSet.Item item : dataSet) {
			matcher.match(descriptor, item.getDescriptor(), matches);

			int goodMatches = 0;
			for (DMatch m : matches.toList()) {
				if (m.distance < Settings.MIN_DISTANCE_THRESHOLD) {
					goodMatches++;
				}
			}

			if ((bestMatch == null || goodMatches > bestMatchMatches) && goodMatches > Settings.MIN_GOOD_MATCHES) {
				bestMatch = item;
				bestMatchMatches = goodMatches;
			}
		}

		return bestMatch;
	}
}