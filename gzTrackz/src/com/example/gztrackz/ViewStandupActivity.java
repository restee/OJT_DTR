package com.example.gztrackz;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class ViewStandupActivity extends Activity {

	private TextView date,time,standup_y,standup_todo,problem;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_stand_up_dialog);
		date = (TextView) findViewById(R.id.standupdate);
		time = (TextView) findViewById(R.id.standuptime);
		standup_y = (TextView) findViewById(R.id.standupprevioustext);
		standup_todo = (TextView) findViewById(R.id.standupstodonowtext);
		problem = (TextView) findViewById(R.id.standupsproblemtext);
		
		date.setText(getIntent().getStringExtra("date").substring(0,11));
		time.setText(getIntent().getStringExtra("date").substring(11));
		standup_y.setText(getIntent().getStringExtra("standup_y"));
		standup_todo.setText(getIntent().getStringExtra("standup_todo"));
		problem.setText(getIntent().getStringExtra("problem"));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_standup, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case android.R.id.home:
	            // app icon in action bar clicked; goto parent activity.
	            this.finish();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}



}
