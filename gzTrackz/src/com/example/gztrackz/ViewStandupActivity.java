package com.example.gztrackz;

import java.util.ArrayList;

import list_adapters.ExpandableListAdapter;
import list_objects.Group;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import android.widget.TextView;

public class ViewStandupActivity extends Activity {

	private TextView date,time;
	SparseArray<Group> groups = new SparseArray<Group>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_stand_up_dialog);
		date = (TextView) findViewById(R.id.standupdate);
		time = (TextView) findViewById(R.id.standuptime);
		date.setText(getIntent().getStringExtra("date").substring(0,11));
		time.setText(getIntent().getStringExtra("date").substring(11));
		createData();
		ExpandableListView listView = (ExpandableListView) findViewById(R.id.standuplist);
		ExpandableListAdapter adapter = new ExpandableListAdapter(this,groups);
		listView.setAdapter(adapter);
		
	}
	public void createData() {
		int count=0;
		String [] headDetails = {"What I did in the previous", "What I plan to do now", "What were the problems encountered"};
		ArrayList<String> standUpDetails = new ArrayList<String>();
		standUpDetails.add(getIntent().getStringExtra("standup_y"));
		standUpDetails.add(getIntent().getStringExtra("standup_todo"));
		standUpDetails.add(getIntent().getStringExtra("problem"));
	    for (int j = 0; j < 3; j++) {
	      Group group = new Group(headDetails[count]);
	      for (int i = 0; i < 1; i++) {
	        group.children.add(standUpDetails.get(count));
	      }
	      count++;
	      groups.append(j, group);
	    }
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
