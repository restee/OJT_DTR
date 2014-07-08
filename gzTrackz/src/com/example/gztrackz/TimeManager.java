package com.example.gztrackz;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class TimeManager extends Activity {

	Button logoutBTN;
	SharedPreferences prefs ;
	String PREFERENCE_NAME = "com.example.gztrackz",FNAME = "com.example.gztrackz.firstname",LNAME = "com.example.gztrackz.lastname",EMAIL="com.example.gztrackz.email";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_time_manager);
		logoutBTN = (Button) findViewById(R.id.logout);
		 prefs = this.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
		 
		logoutBTN.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				SharedPreferences.Editor editor = prefs.edit();
				editor.putString(LNAME,null);
				editor.putString(FNAME, null);
				editor.putString(EMAIL,null);
	            editor.commit();
	            finish();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.time_manager, menu);
		return true;
	}

}
