package nl.clockwork.virm.android;

import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;

import android.content.Context;
import android.preference.PreferenceManager;

public final class Settings {
	public static final String TAG = "virm";

	public static final String PATH = "mats";

	public static final int OPENCV_DETECTOR_ALGORITHM = FeatureDetector.ORB;
	public static final int OPENCV_EXTRACTOR_ALGORITHM = DescriptorExtractor.ORB;
	public static final int OPENCV_MATCHER_ALGORITHM = DescriptorMatcher.BRUTEFORCE_HAMMING;

	public static int MIN_DISTANCE_THRESHOLD;
	public static int MIN_GOOD_MATCHES;

	public static int DESIRED_FRAME_MAT_WIDTH;
	public static int DESIRED_FRAME_MAT_HEIGHT;

	public static int DESIRED_REF_MAT_WIDTH;
	public static int DESIRED_REF_MAT_HEIGHT;

	public static String SERVER_HOST_ADDRESS;
	public static int SERVER_PORT;

	public static boolean load(Context context) {
		MIN_DISTANCE_THRESHOLD = getIntPreference(context,
				R.string.preference_min_distance_threshold,
				R.string.preference_default_min_distance_threshold);

		MIN_GOOD_MATCHES = getIntPreference(context,
				R.string.preference_min_good_matches,
				R.string.preference_default_min_good_matches);

		DESIRED_FRAME_MAT_WIDTH = getIntPreference(context,
				R.string.preference_desired_frame_mat_size,
				R.string.preference_default_desired_frame_mat_size);

		DESIRED_FRAME_MAT_HEIGHT = getIntPreference(context,
				R.string.preference_desired_frame_mat_size,
				R.string.preference_default_desired_frame_mat_size);

		SERVER_HOST_ADDRESS = getStringPreference(context,
				R.string.preference_server_ip,
				R.string.preference_default_server_ip);

		SERVER_PORT = getIntPreference(context,
				R.string.preference_server_port,
				R.string.preference_default_server_port);

		return true;
	}

	private static int getIntPreference(Context context, int preference,
			int preferenceDefault) {
		return Integer.parseInt(getStringPreference(context, preference,
				preferenceDefault));
	}

	private static String getStringPreference(Context context, int preference,
			int preferenceDefault) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getString(context.getString(preference),
						context.getString(preferenceDefault));
	}
}
