package nl.clockwork.android.virm.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		//startActivity(new Intent(this, NetworkTestActivity.class));
		startActivity(new Intent(this, LoadingScreenActivity.class));
	}
}
