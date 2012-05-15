package nl.clockwork.android.virm.ui.activity;

import nl.clockwork.android.virm.R;
import nl.clockwork.android.virm.scanner.Result;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

public class ResultActivity extends BaseActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.result);

		TextView title = (TextView) findViewById(R.id.result_title);
		Result event = (Result) getIntent().getParcelableExtra("result");
		title.setText(event.getTitle());

		TextView painter = (TextView) findViewById(R.id.result_painter);
		painter.setText(event.getScanTime() + "ms");

		TextView description = (TextView) findViewById(R.id.result_description);
		description.setMovementMethod(new ScrollingMovementMethod());
	}
}
