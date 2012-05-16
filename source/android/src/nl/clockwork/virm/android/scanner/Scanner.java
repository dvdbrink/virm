package nl.clockwork.virm.android.scanner;

public interface Scanner {
	void scan(byte[] data, int width, int height);

	void addResultListener(ResultListener listener);

	void removeResultListener(ResultListener listener);
}
