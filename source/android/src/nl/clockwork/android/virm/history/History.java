package nl.clockwork.android.virm.history;

public interface History {
	void add(History.Item item);

	History.Item get(History.Item item);

	History.Item get(int position);

	void clear();

	History.Item exists(History.Item item);

	int size();

	public interface Item {
		long getId();

		String getTitle();
		
		long getScanTime();
	}
}
