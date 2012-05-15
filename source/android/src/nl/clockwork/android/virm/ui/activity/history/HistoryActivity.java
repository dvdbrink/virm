package nl.clockwork.android.virm.ui.activity.history;

import nl.clockwork.android.virm.R;
import nl.clockwork.android.virm.Virm;
import nl.clockwork.android.virm.history.History;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class HistoryActivity extends ListActivity {
	private History history;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		history = ((Virm) getApplication()).getHistory();
		if (history.size() > 0) {
			setListAdapter(new HistoryAdapter(this, history));
		} else {
			finish();
			Toast.makeText(this, "History is empty", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.history, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.clear_history:
			history.clear();
			finish();
			Toast.makeText(this, "History cleared", Toast.LENGTH_SHORT).show();
			break;
		}
		return true;
	}
}
