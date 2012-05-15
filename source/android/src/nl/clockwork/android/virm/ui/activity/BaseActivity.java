package nl.clockwork.android.virm.ui.activity;

import nl.clockwork.android.virm.Virm;
import android.app.Activity;

public class BaseActivity extends Activity {
	public Virm getVirm() {
		return (Virm) getApplication();
	}
}
