package com.example.gztrackz;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.login.ClickListeners;

public class ViewStandupActivity extends Activity {

	private TextView date,time,past,current,problems;
	private TextView clickPrevious,clickNow,clickProblems;
	private ClickListeners clickListener;
	private boolean isPreviousClicked,isNowClicked,isProblemsClicked;
	private Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_stand_up_dialog);
		
		init();
		
		date.setText(getIntent().getStringExtra("date").substring(0,11));
		time.setText(getIntent().getStringExtra("date").substring(11));
		past.setText(getIntent().getStringExtra("standup_y"));
		current.setVisibility(View.GONE);
		problems.setVisibility(View.GONE);
		
		clickPrevious.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(isPreviousClicked)
				{
					isPreviousClicked = false;
					past.setText(getIntent().getStringExtra("standup_y"));
					past.setVisibility(View.GONE);
				}
				else
				{
					isPreviousClicked = true;
					past.setVisibility(View.VISIBLE);
				}
				
			}
		});
		clickNow.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(isNowClicked)
				{
					isNowClicked = false;
					current.setText(getIntent().getStringExtra("standup_todo"));
					current.setVisibility(View.VISIBLE);
				}
				else
				{
					isNowClicked = true;
					current.setVisibility(View.GONE);
				}
				
			}
		});
		clickProblems.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(isProblemsClicked)
				{
					isProblemsClicked = false;
					problems.setText(getIntent().getStringExtra("problem"));
					problems.setVisibility(View.VISIBLE);
				}
				else
				{
					isProblemsClicked = true;
					problems.setVisibility(View.GONE);
				}
				
			}
		});
	}
	
	private void init()
	{
		isPreviousClicked = true;
		isNowClicked = true;
		isProblemsClicked = true;
		
		date = (TextView) findViewById(R.id.standupdate);
		time = (TextView) findViewById(R.id.standuptime);
		past = (TextView) findViewById(R.id.previousstandup);
		current = (TextView) findViewById(R.id.todonowstandup);
		problems = (TextView) findViewById(R.id.problemsstandup);
		clickPrevious =  (TextView) findViewById(R.id.previoustext);
		clickNow = (TextView) findViewById(R.id.todotext);
		clickProblems = (TextView) findViewById(R.id.problemstext);
		
		clickListener = new ClickListeners(context);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case android.R.id.home:
	            // app icon in action bar clicked; go home
	            Intent intent = new Intent(this, TabsManager.class);
	            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	            startActivity(intent);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
}
