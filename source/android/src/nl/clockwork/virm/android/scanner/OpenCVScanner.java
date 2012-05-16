package nl.clockwork.virm.android.scanner;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import nl.clockwork.virm.android.C;
import nl.clockwork.virm.android.dataset.DataSet;
import nl.clockwork.virm.android.history.History;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.features2d.DMatch;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;
import org.opencv.imgproc.Imgproc;

public class OpenCVScanner implements Scanner {
	protected DataSet dataSet;

	private List<ResultListener> listeners;

	protected FeatureDetector detector;
	protected DescriptorExtractor extractor;
	protected DescriptorMatcher matcher;

	protected MatOfKeyPoint keypoints;
	protected MatOfDMatch matches;

	protected Mat descriptor, yuv, yuvResized;

	public OpenCVScanner(DataSet dataSet) {
		this.dataSet = dataSet;

		listeners = new ArrayList<ResultListener>();

		detector = FeatureDetector.create(C.OPENCV_DETECTOR_ALGORITHM);
		extractor = DescriptorExtractor.create(C.OPENCV_EXTRACTOR_ALGORITHM);
		matcher = DescriptorMatcher.create(C.OPENCV_MATCHER_ALGORITHM);

		keypoints = new MatOfKeyPoint();
		matches = new MatOfDMatch();

		descriptor = new Mat();
	}

	@Override
	public void scan(byte[] data, int width, int height) {
		if (yuv == null || yuvResized == null) {
			yuv = new Mat(height + height / 2, width, CvType.CV_8UC1);
			yuvResized = new Mat(C.DESIRED_FRAME_MAT_HEIGHT, C.DESIRED_FRAME_MAT_WIDTH, CvType.CV_8UC1);
		}

		yuv.put(0, 0, data);
		Imgproc.resize(yuv, yuvResized, yuvResized.size());

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

	@Override
	public void addResultListener(ResultListener listener) {
		listeners.add(listener);
	}

	@Override
	public void removeResultListener(ResultListener listener) {
		listeners.remove(listener);
	}

	private DataSet.Item calculateBestMatch() {
		DataSet.Item bestMatch = null;
		int bestMatchMatches = 0;
		
		for (DataSet.Item item : dataSet) {
			matcher.match(descriptor, item.getDescriptor(), matches);

			int goodMatches = 0;
			for (DMatch m : matches.toList()) {
				if (m.distance < C.MIN_DISTANCE_THRESHOLD) {
					goodMatches++;
				}
			}

			if ((bestMatch == null || goodMatches > bestMatchMatches) && goodMatches > C.MIN_GOOD_MATCHES) {
				bestMatch = item;
				bestMatchMatches = goodMatches;
			}
		}

		return bestMatch;
	}

	protected void fireMatchEvent(History.Item result) {
		Iterator<ResultListener> it = listeners.iterator();
		while (it.hasNext()) {
			it.next().onMatch(result);
		}
	}

	protected void fireNoMatchEvent() {
		Iterator<ResultListener> it = listeners.iterator();
		while (it.hasNext()) {
			it.next().onNoMatch();
		}
	}
}
