package nl.clockwork.virm.android.scanner;

import nl.clockwork.virm.android.C;
import nl.clockwork.virm.android.dataset.DataSet;
import nl.clockwork.virm.android.history.History;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.features2d.DMatch;
import org.opencv.imgproc.Imgproc;

import android.os.AsyncTask;

public class AsyncOpenCVScanner extends OpenCVScanner {

	public AsyncOpenCVScanner(final DataSet dataSet) {
		super(dataSet);
	}

	@Override
	public void scan(byte[] data, int width, int height) {
		if (yuv == null || yuvResized == null) {
			yuv = new Mat(height + height / 2, width, CvType.CV_8UC1);
			yuvResized = new Mat(C.DESIRED_FRAME_MAT_HEIGHT, C.DESIRED_FRAME_MAT_WIDTH, CvType.CV_8UC1);
		}

		yuv.put(0, 0, data);
		Imgproc.resize(yuv, yuvResized, yuvResized.size());

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
					if (m.distance < C.MIN_DISTANCE_THRESHOLD) {
						goodMatches++;
					}
				}

				if ((bestMatch == null || goodMatches > bestMatchMatches) && goodMatches > C.MIN_GOOD_MATCHES) {
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
