package nl.clockwork.virm.android.ui.activity;

import nl.clockwork.virm.android.Mode;
import nl.clockwork.virm.android.Settings;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Settings.load(this);
		
		Intent intent = null;
		
		if (Settings.MODE == Mode.LOCAL) {
			intent = new Intent(this, LoadingScreenActivity.class);
		} else if (Settings.MODE == Mode.REMOTE) {
			intent = new Intent(this, CameraActivity.class);
		}
		
		if (intent != null) {
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY);
			startActivity(intent);
		}
	}
}
