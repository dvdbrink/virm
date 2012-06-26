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

		if (Settings.MODE == Mode.REMOTE) {
			startActivity(new Intent(MainActivity.this, CameraActivity.class));
		} else if (Settings.MODE == Mode.LOCAL) {
			startActivity(new Intent(MainActivity.this, LoadingScreenActivity.class));
		}
	}
}
