package com.example.scrumgztrack;



import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import com.example.adapter.OJTlistAdapter;
import com.example.adapter.TeamMemberAdapter;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Menu;
import android.widget.ListView;
import android.widget.TextView;

public class ManageTeam extends Activity {

	private ListView teamMembersLV;
	private TeamMemberAdapter teamMemberAdapter;
	private List<String> teamMembers;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manage_team);
		
		TextView nameTxt = (TextView)findViewById(R.id.txtTeamName);
		Typeface tf = Typeface.createFromAsset(getAssets(), "CODE Bold.otf");
		nameTxt.setTypeface(tf);
		
		teamMembersLV = (ListView) findViewById(R.id.teamMemberListview);
		teamMembers = new ArrayList();
		teamMembers.add("Iron Man");
		teamMembers.add("Super Man");
		teamMembers.add("The Hulk");
		teamMembers.add("Hawk Eye");
		teamMembers.add("Captain America");
		teamMembers.add("Natasha Romanoff");
		teamMemberAdapter = new TeamMemberAdapter(this,teamMembers);
		teamMembersLV.setAdapter(teamMemberAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.manage_team, menu);
		return true;
	}
	
	private class RetrieveAllOJTinTeam extends AsyncTask<String, Void, Boolean> {
		Context context;
		String teamName;
		ProgressDialog progressD;

		public RetrieveAllOJTinTeam(Context context,String teamName) {
			this.context = context;
			this.teamName = teamName;
		}

		@Override
		protected void onPreExecute() {
			progressD = new ProgressDialog(context);
			progressD.setMessage("Retrieving list of OJTs...");
			progressD.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progressD.setCanceledOnTouchOutside(false);
			progressD.show();
			
		}

		@Override
		protected Boolean doInBackground(String... params) {
			boolean flag = true;
			
			try {
				
				String urlTopTracks = "http://gz123.site90.net/list_trainees/";
				HttpClient client = new DefaultHttpClient();
				ResponseHandler<String> handler = new BasicResponseHandler();

				HttpPost request = new HttpPost(urlTopTracks);

				String httpResponseTopTracks = client.execute(request, handler);
				Log.d("RESULT", httpResponseTopTracks);
				StringTokenizer token = new StringTokenizer(
						httpResponseTopTracks, "<");
				String retrieveResult = token.nextToken();
				JSONArray resultJSON = new JSONArray(retrieveResult);
				JSONObject tempObj;

				for (int init = 0; init < resultJSON.length(); init++) {
					tempObj = resultJSON.getJSONObject(init);
					
				}

				Log.d("LIST OF OJTS", retrieveResult);

			} catch (Exception e) {
				flag = false;
				e.printStackTrace();
			}

			return flag;
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			if (progressD.isShowing()) {
				progressD.dismiss();
			}
			
			
		}
	}

}
