package nl.clockwork.virm.android.scanner;

import nl.clockwork.virm.android.Settings;
import nl.clockwork.virm.android.dataset.DataSet;
import nl.clockwork.virm.android.history.History;

import org.opencv.features2d.DMatch;

import android.content.Context;
import android.os.AsyncTask;

public class AsyncLocalOpenCVScanner extends BaseLocalOpenCVScanner {

	public AsyncLocalOpenCVScanner(Context context, DataSet dataSet) {
		super(context, dataSet);
	}

	@Override
	public void scan(byte[] data, int width, int height) {
		super.scan(data, width, height);
		
		new ScanTask().execute();
	}

	private class ScanTask extends AsyncTask<Void, Void, History.Item> {
		@Override
		protected History.Item doInBackground(Void... params) {
			long start = System.currentTimeMillis();

			detector.detect(yuvResized, keypoints);
			extractor.compute(yuvResized, keypoints, descriptor);

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

			long stop = System.currentTimeMillis();

			if (bestMatch != null) {
				return new Result(bestMatch.getName(), stop - start);
			}

			return null;
		}

		@Override
		protected void onPostExecute(History.Item result) {
			if (result != null) {
				fireMatchEvent(result);
			} else {
				fireNoMatchEvent();
			}
		}
	}
}
