package nl.clockwork.android.virm.ui.activity;

import nl.clockwork.android.virm.Factory;
import nl.clockwork.android.virm.dataset.DataSet;
import nl.clockwork.android.virm.loader.Loader;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

public class LoadingScreenActivity extends BaseActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		new LoadTask().execute();
	}

	private class LoadTask extends AsyncTask<Void, Integer, Void> {
		private ProgressDialog progressDialog;
		private Loader loader;
		private DataSet dataSet;

		@Override
		protected void onPreExecute() {
			progressDialog = new ProgressDialog(LoadingScreenActivity.this);
			progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			progressDialog.setTitle("Loading...");
			progressDialog.setCancelable(false);
			progressDialog.setIndeterminate(false);
			progressDialog.setMax(100);
			progressDialog.setProgress(0);
			progressDialog.show();

			loader = Factory.createLoader(LoadingScreenActivity.this);
		}

		@Override
		protected Void doInBackground(Void... params) {
			synchronized (this) {
				dataSet = loader.load(new Loader.OnProgressUpdateCallback() {
					@Override
					public void onProgressUpdate(int progress) {
						publishProgress(progress);
					}
				});
			}
			return null;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			progressDialog.setProgress(values[0]);
		}

		@Override
		protected void onPostExecute(Void result) {
			progressDialog.dismiss();

			LoadingScreenActivity.this.getVirm().setDataSet(dataSet);
			LoadingScreenActivity.this.startActivity(new Intent(LoadingScreenActivity.this, CameraActivity.class));
		}
	}
}
