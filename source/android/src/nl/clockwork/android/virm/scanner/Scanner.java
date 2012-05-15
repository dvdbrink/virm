package nl.clockwork.android.virm.scanner;

public interface Scanner {
	void scan(byte[] data, int width, int height);

	void addResultListener(ResultListener listener);

	void removeResultListener(ResultListener listener);
}
