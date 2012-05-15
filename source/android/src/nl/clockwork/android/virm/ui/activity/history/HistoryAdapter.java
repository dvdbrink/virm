package nl.clockwork.android.virm.ui.activity.history;

import java.text.SimpleDateFormat;
import java.util.Date;

import nl.clockwork.android.virm.R;
import nl.clockwork.android.virm.history.History;
import nl.clockwork.android.virm.scanner.Result;
import nl.clockwork.android.virm.ui.activity.ResultActivity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class HistoryAdapter extends BaseAdapter {
	private Context context;
	private History history;

	public HistoryAdapter(Context context, History history) {
		this.context = context;
		this.history = history;
	}

	@Override
	public int getCount() {
		return history.size();
	}

	@Override
	public History.Item getItem(int position) {
		return history.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View row = inflater.inflate(R.layout.history_item, parent, false);

		TextView title = (TextView) row.findViewById(R.id.history_title);
		title.setText(history.get(position).getTitle());

		TextView painter = (TextView) row.findViewById(R.id.history_datetime);
		painter.setText(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));

		row.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, ResultActivity.class);
				intent.putExtra("result", (Result) history.get(position));
				context.startActivity(intent);
			}
		});

		return row;
	}
}
