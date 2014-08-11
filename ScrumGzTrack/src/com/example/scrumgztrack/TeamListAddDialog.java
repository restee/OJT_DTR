package com.example.scrumgztrack;

import com.example.adapter.TeamListDialogAdapter;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ListView;
import android.widget.Toast;

public class TeamListAddDialog extends Activity {

	private ListView teamListview;
	private TeamListDialogAdapter teamlistAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_team_list_add_dialog);
		teamListview = (ListView) findViewById(R.id.dialog_teamlist_listview);
				
		String[] teamList = getIntent().getStringArrayExtra("teamList");
		teamlistAdapter = new TeamListDialogAdapter(this, teamList);
		teamListview.setAdapter(teamlistAdapter);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.team_list_add_dialog, menu);
		return true;
	}

}
