package nl.clockwork.virm.android.ui.activity;

import nl.clockwork.virm.android.Virm;
import android.app.Activity;

public abstract class BaseActivity extends Activity {
	public Virm getVirm() {
		return (Virm) getApplication();
	}
}
