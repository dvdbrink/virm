package nl.clockwork.android.virm.loader;

import nl.clockwork.android.virm.dataset.DataSet;

public interface Loader {
	DataSet load(Loader.OnProgressUpdateCallback onProgressUpdateCallback);

	public interface OnProgressUpdateCallback {
		void onProgressUpdate(int progress);
	}
}
