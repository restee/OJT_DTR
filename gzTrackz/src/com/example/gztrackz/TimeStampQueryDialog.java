package com.example.gztrackz;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class TimeStampQueryDialog extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.query_history_dialog);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.time_stamp_query_dialog, menu);
		return true;
	}

}
