package nl.clockwork.virm.android.dataset;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BasicDataSet implements DataSet {
	private List<DataSet.Item> items;

	public BasicDataSet() {
		items = new ArrayList<DataSet.Item>();
	}

	@Override
	public synchronized void add(DataSet.Item item) {
		items.add(item);
	}

	@Override
	public synchronized DataSet.Item get(int position) {
		return items.get(position);
	}

	@Override
	public synchronized int size() {
		return items.size();
	}

	@Override
	public synchronized Iterator<Item> iterator() {
		return items.iterator();
	}
}
