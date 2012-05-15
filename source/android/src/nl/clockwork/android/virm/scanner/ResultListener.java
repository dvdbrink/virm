package nl.clockwork.android.virm.scanner;

import java.util.EventListener;

import nl.clockwork.android.virm.history.History;

public interface ResultListener extends EventListener {
	void onMatch(History.Item event);

	void onNoMatch();
}
