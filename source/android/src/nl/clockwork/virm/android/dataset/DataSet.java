package nl.clockwork.virm.android.dataset;

import org.opencv.core.Mat;

public interface DataSet extends Iterable<DataSet.Item> {
	void add(DataSet.Item item);

	DataSet.Item get(int position);

	int size();

	public interface Item {
		String getName();

		Mat getDescriptor();
	}
}
