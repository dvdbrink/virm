package nl.clockwork.android.virm.dataset;

import org.opencv.core.Mat;

public class Painting implements DataSet.Item {
	private String name;
	private Mat descriptor;

	public Painting(String name, Mat descriptor) {
		this.name = name;
		this.descriptor = descriptor;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Mat getDescriptor() {
		return descriptor;
	}
}
