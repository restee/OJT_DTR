package com.example.scrumgztrack;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class OJTOptionDialog extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ojtoption_dialog);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ojtoption_dialog, menu);
		return true;
	}

}
