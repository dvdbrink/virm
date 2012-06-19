package nl.clockwork.virm.android.loader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import nl.clockwork.virm.android.Settings;
import nl.clockwork.virm.android.Factory;
import nl.clockwork.virm.android.dataset.DataSet;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.FeatureDetector;
import org.opencv.imgproc.Imgproc;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class ImageLoader implements Loader {
	private Context context;

	public ImageLoader(Context context) {
		this.context = context;
	}

	@Override
	public DataSet load(Loader.OnProgressUpdateCallback onProgressUpdateCallback) {
		DataSet dataset = Factory.createDataSet();
		try {
			String[] fileNames = context.getAssets().list(Settings.PATH);
			int count = 0;
			for (String fileName : fileNames) {
				Mat descriptor = loadAssetDescriptor(Settings.PATH + File.separator + fileName);
				dataset.add(Factory.createDataSetItem(fileName, descriptor));
				onProgressUpdateCallback.onProgressUpdate((int) ((++count / (float) fileNames.length) * 100));
			}
			Log.i(Settings.TAG, count + " assets loaded");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return dataset;
	}

	private Mat loadAssetDescriptor(String fileName) {
		Mat descriptor = new Mat();
		try {
			InputStream in = context.getAssets().open(fileName);
			Bitmap b = BitmapFactory.decodeStream(in);
			in.close();

			Bitmap bmp = b.copy(Bitmap.Config.ARGB_8888, false);
			Mat data = new Mat();
			Utils.bitmapToMat(bmp, data);
			Mat dataResized = new Mat(Settings.DESIRED_REF_MAT_HEIGHT, Settings.DESIRED_REF_MAT_WIDTH, CvType.CV_8UC1);
			Imgproc.resize(data, dataResized, dataResized.size());

			FeatureDetector detector = FeatureDetector.create(Settings.OPENCV_DETECTOR_ALGORITHM);
			DescriptorExtractor extractor = DescriptorExtractor.create(Settings.OPENCV_EXTRACTOR_ALGORITHM);
			MatOfKeyPoint keypoints = new MatOfKeyPoint();

			detector.detect(dataResized, keypoints);
			extractor.compute(dataResized, keypoints, descriptor);
			dataResized.release();
			data.release();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return descriptor;
	}
}
