package nl.clockwork.virm.android;

import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;

public final class C {
	public static final String TAG = "virm";

	public static final String PATH = "db2-50%";

	public static final int OPENCV_DETECTOR_ALGORITHM = FeatureDetector.ORB;
	public static final int OPENCV_EXTRACTOR_ALGORITHM = DescriptorExtractor.ORB;
	public static final int OPENCV_MATCHER_ALGORITHM = DescriptorMatcher.BRUTEFORCE_HAMMING;
	
	public static int MIN_DISTANCE_THRESHOLD = 35;
	public static int MIN_GOOD_MATCHES = 5;

	public static int DESIRED_FRAME_MAT_WIDTH = 150;
	public static int DESIRED_FRAME_MAT_HEIGHT = 150;

	public static int DESIRED_REF_MAT_WIDTH = 150;
	public static int DESIRED_REF_MAT_HEIGHT = 150;
}
