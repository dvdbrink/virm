package nl.clockwork.virm.android.scanner;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import nl.clockwork.virm.android.Settings;
import nl.clockwork.virm.android.history.History;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.FeatureDetector;
import org.opencv.imgproc.Imgproc;

import android.content.Context;

public abstract class BasicOpenCVScanner implements Scanner {
	protected Context context;
	protected List<ResultListener> listeners;
	protected FeatureDetector detector;
	protected DescriptorExtractor extractor;
	protected MatOfKeyPoint keypoints;
	protected Mat descriptor, yuv, yuvResized;
	
	public BasicOpenCVScanner(Context context) {
		this.context = context;
		
		listeners = new ArrayList<ResultListener>();
		detector = FeatureDetector.create(Settings.OPENCV_DETECTOR);
		extractor = DescriptorExtractor.create(Settings.OPENCV_EXTRACTOR);
		keypoints = new MatOfKeyPoint();
		descriptor = new Mat();
	}

	@Override
	public void scan(byte[] data, int width, int height) {
		if (yuv == null || yuvResized == null) {
			// CV_8UC1 is grayscale
			yuv = new Mat(height + height / 2, width, CvType.CV_8UC1);
			yuvResized = new Mat(Settings.FRAME_HEIGHT, Settings.FRAME_WIDTH, CvType.CV_8UC1);
		}

		yuv.put(0, 0, data);
		Imgproc.resize(yuv, yuvResized, yuvResized.size());
	}
	
	@Override
	public void addResultListener(ResultListener listener) {
		listeners.add(listener);
	}

	@Override
	public void removeResultListener(ResultListener listener) {
		listeners.remove(listener);
	}
	
	@Override
	public void destroy() {
		if (descriptor != null) {
			descriptor.release();
		}
		if (yuv != null) {
			yuv.release();
		}
		if (yuvResized != null) {
			yuvResized.release();
		}
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
