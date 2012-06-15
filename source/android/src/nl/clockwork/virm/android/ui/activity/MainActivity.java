package nl.clockwork.virm.android.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		final CharSequence[] items = {"Local", "Remote"};
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Detection method");
		builder.setItems(items, new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int item) {
		        if (items[item].equals("Local")) {
		        	startActivity(new Intent(MainActivity.this, LoadingScreenActivity.class));
		        } else if (items[item].equals("Remote")) {
		        	Intent intent = new Intent(MainActivity.this, CameraActivity.class);
		        	Bundle b = new Bundle();
		        	b.putString("detection_method", "Remote");
		        	intent.putExtras(b);
		        	startActivity(intent);
		        }
		    }
		});
		AlertDialog alert = builder.create();
		alert.show();
	}
}
