package nl.clockwork.virm.android.ui.activity;

import nl.clockwork.virm.android.Factory;
import nl.clockwork.virm.android.R;
import nl.clockwork.virm.android.history.History;
import nl.clockwork.virm.android.scanner.Result;
import nl.clockwork.virm.android.scanner.ResultListener;
import nl.clockwork.virm.android.scanner.Scanner;
import nl.clockwork.virm.android.ui.activity.history.HistoryActivity;
import nl.clockwork.virm.android.ui.preview.Frame;
import nl.clockwork.virm.android.ui.preview.FrameListener;
import nl.clockwork.virm.android.ui.preview.Preview;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class CameraActivity extends BaseActivity implements ResultListener, FrameListener {
	private Scanner scanner;
	private Preview preview;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		scanner = Factory.createScanner(this, getVirm().getDataSet());
		scanner.addResultListener(this);

		preview = Factory.createPreview(this);
		preview.addFrameListener(this);
		preview.setAutoPause(true);

		setContentView(preview.getView());
	}

	@Override
	public void onResume() {
		super.onResume();
		preview.setCapturing(true);
	}

	@Override
	public void onPause() {
		super.onPause();
		preview.setCapturing(false);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.camera, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.open_history:
			startActivity(new Intent(this, HistoryActivity.class));
			break;
		case R.id.open_perferences:
			startActivity(new Intent(this, PreferencesActivity.class));
			break;
		}
		return true;
	}

	@Override
	public void onMatch(History.Item result) {
		getVirm().getHistory().add(result);
		Intent intent = new Intent(this, ResultActivity.class);
		intent.putExtra("result", (Result)result);
		startActivity(intent);
	}

	@Override
	public void onNoMatch() {
		if (!preview.isCapturing()) {
			preview.setCapturing(true);
		}
	}

	@Override
	public void onFrame(Frame event) {
		scanner.scan(event.getData(), event.getWidth(), event.getHeight());
	}
}
