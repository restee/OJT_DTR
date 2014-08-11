package com.example.tabs;


import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.adapter.TeamListAdapter;
import com.example.scrumgztrack.Gz_ScrumMaster;
import com.example.scrumgztrack.R;
import com.example.scrumgztrack.TeamListAddDialog;


public class TeamListFragment extends Fragment {

	private String email;
	private ListView teamListview;
	private List<String> teamList;
	private TeamListAdapter teamAdapter;
	private Gz_ScrumMaster scrumMaster;
	public static final String ADD_TO_TEAM_BROADCAST = "com.example.gzscrumtrack.addToTeamBroadcast";
	public static final String TEAM_ADDED_BROADCAST = "com.example.gzscrumtrack.teamAddedBroadcast";
	private String ojtEmail;
	
	private BroadcastReceiver addToTeamBroadcast = new BroadcastReceiver(){
		@Override
		public void onReceive(Context arg0, Intent arg1) {
			if(arg1.getStringExtra("action").compareToIgnoreCase("remove")==0){
				scrumMaster = new Gz_ScrumMaster("",getActivity());
				scrumMaster.removeFromTeam(arg1.getStringExtra("ojtEmail"),arg1.getIntExtra("position",0));
			}else{
				Intent i = new Intent(getActivity(),TeamListAddDialog.class);			
				String[] teamNames = new String[teamList.size()];
				for(int init=0;init<teamList.size();init++){
					teamNames[init] = teamList.get(init);
				}
				i.putExtra("teamList",teamNames);				
				i.putExtra("position",arg1.getIntExtra("position",0));				
				i.putExtra("ojtEmail",arg1.getStringExtra("ojtEmail"));
				startActivityForResult(i,2);
			}
		}		
	};
	
	private BroadcastReceiver teamAddedBroadcast = new BroadcastReceiver(){
		@Override
		public void onReceive(Context arg0, Intent arg1) {
			teamList = new ArrayList();
			new GetAllTeams(getActivity(),email).execute();			
		}		
	};
	
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {		
		if(requestCode==2){
			if(resultCode==getActivity().RESULT_OK){
				//Toast.makeText(getActivity(),data.getStringExtra("ojtEmail"),Toast.LENGTH_SHORT).show();
				scrumMaster = new Gz_ScrumMaster(data.getStringExtra("teamName"),getActivity());	
				
				scrumMaster.addToTeam(data.getStringExtra("ojtEmail"),data.getIntExtra("position",0));
			}
		}
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_teamlist, container, false);
		teamListview = (ListView) rootView.findViewById(R.id.teamlist_listview);
		email = "dracula@gz.com";
		teamList = new ArrayList();
		
		teamAdapter = new TeamListAdapter(getActivity(),teamList);
		teamListview.setAdapter(teamAdapter);
		new GetAllTeams(getActivity(),email).execute();
		
		getActivity().registerReceiver(addToTeamBroadcast, new IntentFilter(ADD_TO_TEAM_BROADCAST));
		getActivity().registerReceiver(teamAddedBroadcast, new IntentFilter(TEAM_ADDED_BROADCAST));
		return rootView;
	}
	
	@Override
	public void onDestroy() {
	
		super.onDestroy();
		getActivity().unregisterReceiver(addToTeamBroadcast);
		getActivity().unregisterReceiver(teamAddedBroadcast);
	}
	
	private class GetAllTeams extends AsyncTask<String, Void, Boolean> {
		Context context;
		String email;
		ProgressDialog progressD;

		public GetAllTeams(Context context,String email) {
			this.context = context;
			this.email=email;
			
		}

		@Override
		protected void onPreExecute() {
			
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			teamAdapter = new TeamListAdapter(getActivity(),teamList);
			teamListview.setAdapter(teamAdapter);		
		}
		
		
		@Override
		protected Boolean doInBackground(String... params) {
			boolean flag = true;
			
			try {								
				String urlTopTracks = "http://gz123.site90.net/get_teams/default.php";
				HttpClient client = new DefaultHttpClient();
				ResponseHandler<String> handler = new BasicResponseHandler();
				Log.d("email", email);
								
				HttpPost request = new HttpPost(urlTopTracks);
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
				nameValuePairs.add(new BasicNameValuePair("email",email));
				request.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				    				    
				String httpResponseTopTracks = client.execute(request, handler);
				Log.d("RESULT", httpResponseTopTracks);
				StringTokenizer token = new StringTokenizer(
						httpResponseTopTracks, "<");
				String retrieveResult = token.nextToken();
				
				JSONArray timeLogResult = new JSONArray(retrieveResult);
				JSONObject temp;
				for(int init = 0;init<timeLogResult.length();init++){					
					temp = timeLogResult.getJSONObject(init);
					teamList.add(temp.getString("team_name"));
				}
								
				
			} catch (Exception e) {
				flag = false;
				e.printStackTrace();
			}

			return flag;
		}
		
		
	}
}
