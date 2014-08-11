package com.example.scrumgztrack;

import com.example.adapter.TeamListDialogAdapter;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class TeamListAddDialog extends Activity {

	private ListView teamListview;
	private TeamListDialogAdapter teamlistAdapter;
	private String[] teamList ;
	private String ojtEmail;
	private int positionMain;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_team_list_add_dialog);
		teamListview = (ListView) findViewById(R.id.dialog_teamlist_listview);
				
		teamList = getIntent().getStringArrayExtra("teamList");
		teamlistAdapter = new TeamListDialogAdapter(this, teamList);
		teamListview.setAdapter(teamlistAdapter);
		ojtEmail = getIntent().getStringExtra("ojtEmail");
		positionMain = getIntent().getIntExtra("position",0);
		
		
		teamListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
					Intent i = new Intent();
					i.putExtra("teamName", teamList[position]);
					i.putExtra("ojtEmail",ojtEmail);	
					i.putExtra("position", positionMain);
					setResult(RESULT_OK, i);
					finish();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.team_list_add_dialog, menu);
		return true;
	}

}
