package nl.clockwork.virm.android.loader;

import nl.clockwork.virm.android.dataset.DataSet;

public interface Loader {
	DataSet load(Loader.OnProgressUpdateCallback onProgressUpdateCallback);

	public interface OnProgressUpdateCallback {
		void onProgressUpdate(int progress);
	}
}
