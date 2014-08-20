package com.example.scrumgztrack;



import android.os.Bundle;
import android.app.Activity;
import android.graphics.Typeface;
import android.view.Menu;
import android.widget.TextView;

public class ManageTeam extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manage_team);
		
		TextView nameTxt = (TextView)findViewById(R.id.txtTeamName);
		Typeface tf = Typeface.createFromAsset(getAssets(), "CODE BOLD.otf");
		nameTxt.setTypeface(tf);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.manage_team, menu);
		return true;
	}

}
