package nl.clockwork.android.virm.history;

import java.util.ArrayList;
import java.util.List;

public class BasicHistory implements History {
	private List<History.Item> items;

	public BasicHistory() {
		items = new ArrayList<History.Item>();
	}

	@Override
	public synchronized void add(History.Item item) {
		if (exists(item) == null) {
			items.add(item);
		}
	}

	@Override
	public synchronized History.Item get(History.Item item) {
		return exists(item);
	}

	@Override
	public synchronized History.Item get(int position) {
		return items.get(position);
	}

	@Override
	public synchronized void clear() {
		items.clear();
	}

	@Override
	public synchronized History.Item exists(History.Item item) {
		for (History.Item i : items) {
			if (i.getId() == item.getId()) {
				return i;
			}
		}
		return null;
	}

	@Override
	public synchronized int size() {
		return items.size();
	}
}
