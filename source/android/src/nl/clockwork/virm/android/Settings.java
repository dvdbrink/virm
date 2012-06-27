package nl.clockwork.virm.android;

import android.content.Context;
import android.preference.PreferenceManager;

public final class Settings {
	public static int MODE;

	// Local detection settings
	public static String DATASET_LOCATION;
	public static int OPENCV_DETECTOR;
	public static int OPENCV_EXTRACTOR;
	public static int OPENCV_MATCHER;
	public static int MIN_DISTANCE_THRESHOLD;
	public static int MIN_GOOD_MATCHES;
	public static int FRAME_WIDTH;
	public static int FRAME_HEIGHT;

	// Remote server settings
	public static String SERVER_IP;
	public static int SERVER_PORT;

	public static void load(Context context) {
		MODE = getIntPreference(context,
				R.string.preference_mode,
				R.string.preference_default_mode);
		
		loadLocalDetectionSettings(context);
		loadServerSettings(context);
	}
	
	private static void loadLocalDetectionSettings(Context context) {
		DATASET_LOCATION = getStringPreference(context,
				R.string.preference_dataset_location,
				R.string.preference_default_dataset_location);
		OPENCV_DETECTOR = getIntPreference(context,
				R.string.preference_opencv_detector,
				R.string.preference_default_opencv_detector);
		OPENCV_EXTRACTOR = getIntPreference(context,
				R.string.preference_opencv_extractor,
				R.string.preference_default_opencv_extractor);
		OPENCV_MATCHER = getIntPreference(context,
				R.string.preference_opencv_matcher,
				R.string.preference_default_opencv_matcher);
		MIN_DISTANCE_THRESHOLD = getIntPreference(context,
				R.string.preference_min_distance_threshold,
				R.string.preference_default_min_distance_threshold);
		MIN_GOOD_MATCHES = getIntPreference(context,
				R.string.preference_min_good_matches,
				R.string.preference_default_min_good_matches);
		FRAME_WIDTH = getIntPreference(context,
				R.string.preference_frame_size,
				R.string.preference_default_frame_size);
		FRAME_HEIGHT = getIntPreference(context,
				R.string.preference_frame_size,
				R.string.preference_default_frame_size);
	}
	
	private static void loadServerSettings(Context context) {
		SERVER_IP = getStringPreference(context,
				R.string.preference_server_ip,
				R.string.preference_default_server_ip);
		SERVER_PORT = getIntPreference(context,
				R.string.preference_server_port,
				R.string.preference_default_server_port);
	}

	private static int getIntPreference(Context context, int preference, int preferenceDefault) {
		return Integer.parseInt(getStringPreference(context, preference, preferenceDefault));
	}

	private static String getStringPreference(Context context, int preference, int preferenceDefault) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getString(context.getString(preference), context.getString(preferenceDefault));
	}
}
