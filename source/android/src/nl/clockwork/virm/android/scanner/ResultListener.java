package nl.clockwork.virm.android.scanner;

import java.util.EventListener;

import nl.clockwork.virm.android.history.History;

public interface ResultListener extends EventListener {
	void onMatch(History.Item event);

	void onNoMatch();
}
