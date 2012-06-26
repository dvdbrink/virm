package nl.clockwork.virm.android;

import nl.clockwork.virm.android.dataset.DataSet;
import nl.clockwork.virm.android.history.History;
import android.app.Application;

public class Virm extends Application {
	private DataSet dataSet;
	private History history;

	@Override
	public void onCreate() {
		super.onCreate();

		Settings.load(this);

		dataSet = Factory.createDataSet();
		history = Factory.createHistory();
	}

	public void setDataSet(DataSet dataSet) {
		this.dataSet = dataSet;
	}

	public void setHistory(History history) {
		this.history = history;
	}

	public DataSet getDataSet() {
		return dataSet;
	}

	public History getHistory() {
		return history;
	}
}
