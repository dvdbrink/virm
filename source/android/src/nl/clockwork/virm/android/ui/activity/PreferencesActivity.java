package nl.clockwork.virm.android.ui.activity;

import nl.clockwork.virm.android.C;
import nl.clockwork.virm.android.R;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class PreferencesActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener {
	private boolean preferencesChanged;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		preferencesChanged = false;
		
		addPreferencesFromResource(R.xml.preferences);

		PreferenceManager.getDefaultSharedPreferences(this)
				.registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		PreferenceManager.getDefaultSharedPreferences(this)
				.registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		PreferenceManager.getDefaultSharedPreferences(this)
				.unregisterOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		PreferenceManager.getDefaultSharedPreferences(this)
				.unregisterOnSharedPreferenceChangeListener(this);

		if (preferencesChanged) {
			C.MIN_DISTANCE_THRESHOLD = getIntPreference(
					R.string.preference_min_distance_threshold,
					R.string.preference_default_min_distance_threshold);
			C.MIN_GOOD_MATCHES = getIntPreference(
					R.string.preference_min_good_matches,
					R.string.preference_default_min_good_matches);
			C.DESIRED_FRAME_MAT_WIDTH = getIntPreference(
					R.string.preference_desired_frame_mat_size,
					R.string.preference_default_desired_frame_mat_size);
			C.DESIRED_FRAME_MAT_HEIGHT = getIntPreference(
					R.string.preference_desired_frame_mat_size,
					R.string.preference_default_desired_frame_mat_size);
			Toast.makeText(this, "Preferences saved", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.preferences, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.restore_defaults:
			PreferenceManager.getDefaultSharedPreferences(this).edit().clear().apply();
			restart();
			Toast.makeText(this, "Defaults restored", Toast.LENGTH_SHORT).show();
			break;
		}
		return true;
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		preferencesChanged = true;

		Preference p = findPreference(key);
		if (p instanceof EditTextPreference) {
			EditTextPreference editTextPref = (EditTextPreference) p;
			editTextPref.setSummary(editTextPref.getText());
		}
	}

	private int getIntPreference(int preference, int preferenceDefault) {
		return Integer.parseInt(getStringPreference(preference, preferenceDefault));
	}

	private String getStringPreference(int preference, int preferenceDefault) {
		return PreferenceManager.getDefaultSharedPreferences(this).getString(
				this.getString(preference), this.getString(preferenceDefault));
	}

	private void restart() {
		Intent intent = getIntent();
		overridePendingTransition(0, 0);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		finish();
		overridePendingTransition(0, 0);
		startActivity(intent);
	}
}
