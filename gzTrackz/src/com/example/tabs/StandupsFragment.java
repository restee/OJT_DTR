package com.example.tabs;

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

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.gztrackz.DB_Standups;
import com.example.gztrackz.R;
import com.example.gztrackz.StandUpDateAdapter;
import com.example.gztrackz.StandUpsDialog;
import com.example.gztrackz.Standup;
import com.example.gztrackz.TimeLog;
import com.example.gztrackz.ViewStandupActivity;

public class StandupsFragment extends Fragment {

	private DB_Standups standupsDB;
	private String PREFERENCE_NAME = "com.example.gztrackz",FNAME = "com.example.gztrackz.firstname",LNAME = "com.example.gztrackz.lastname",EMAIL="com.example.gztrackz.email";
	private SharedPreferences prefs ;
	private boolean firstCreate=true;
	private String email;
	private ListView dateList;
	private StandUpDateAdapter standupAdapter;
	private List<Standup> standupList;
	private TextView nothing;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_standups, container, false);
		prefs = getActivity().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);		
		email = prefs.getString(EMAIL, null);
		nothing = (TextView) rootView.findViewById(R.id.norecordfound);
		dateList = (ListView) rootView.findViewById(R.id.standupslist);
		standupList = new ArrayList();
		standupAdapter = new StandUpDateAdapter(getActivity(),standupList);
		dateList.setAdapter(standupAdapter);
		
			firstCreate = false;
			standupsDB = new DB_Standups(getActivity());
			standupsDB.open();
			//standupsDB.removeAll();
			
			
			
			standupList = standupsDB.getAllRowOf(email);
			standupAdapter = new StandUpDateAdapter(getActivity(),standupList);
			dateList.setAdapter(standupAdapter);
			if(standupList.size()>0){
				nothing.setVisibility(View.INVISIBLE);
			}else
				nothing.setVisibility(View.VISIBLE);
			
			if(isConnectingToInternet()){
				new RetrieveStandupHistory(getActivity(),email).execute();
			}
			
			dateList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long arg3) {
					Intent i = new Intent(getActivity(),ViewStandupActivity.class);
					i.putExtra("date",standupList.get(position).getDate());
					i.putExtra("standup_y",standupList.get(position).getStandup_y());
					i.putExtra("standup_todo",standupList.get(position).getStandup_todo());
					i.putExtra("problem", standupList.get(position).getProblem());
					startActivity(i);
				}
			});
		
		
		return rootView;
	}
	 public boolean isConnectingToInternet(){
	        ConnectivityManager connectivity = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
	          if (connectivity != null)
	          {
	              NetworkInfo[] info = connectivity.getAllNetworkInfo();
	              if (info != null)
	                  for (int i = 0; i < info.length; i++)
	                      if (info[i].getState() == NetworkInfo.State.CONNECTED)
	                      {
	                          return true;
	                      }
	 
	          }
	          return false;
	    }
	private class RetrieveStandupHistory extends AsyncTask<String, Void,Boolean> {	        
		String email;
		Context context;
		
		public RetrieveStandupHistory(Context context,String email){
			this.context = context;
			this.email = email;					
		}
		
		@Override
	    protected void onPreExecute() {
			Log.d("STARTED RETRIEVING STANDUP HISTORY","---------------------");
	    }
		
		@Override
	    protected void onPostExecute(Boolean result) {        	
	    	
	    }
		
		@Override
	    protected Boolean doInBackground(String... params) {
	        boolean flag = true;	            
	        try {	        	
	        	Standup latestDay = standupsDB.getLatestRowOf(email);
	        	
	        	String urlTopTracks=null;
	        	if(latestDay.getDate()!=null){	        		
	        		urlTopTracks = "http://gz123.site90.net/get_standups_greater/?email=" + email + "&date=" + latestDay.getDate().substring(0,10);
	        		Log.d("date",latestDay.getDate());
	        		Log.d("standup_y",latestDay.getStandup_y());
	        		
	        	}else
	        		urlTopTracks = "http://gz123.site90.net/get_standups/?email=" + email;
				
				HttpClient client = new DefaultHttpClient();
				ResponseHandler<String> handler = new BasicResponseHandler();
				
				HttpPost request = new HttpPost(urlTopTracks);
				
				String httpResponseTopTracks = client.execute(request, handler);				
				
				StringTokenizer token = new StringTokenizer(httpResponseTopTracks,"<");
				String retrieveResult = token.nextToken();
				JSONArray timeLogResult = new JSONArray(retrieveResult);
				JSONObject temp;
				Log.d("Standup HISTORY     " + urlTopTracks,retrieveResult);			
				for(int init=0;init<timeLogResult.length();init++){
					temp = timeLogResult.getJSONObject(init);
					if(latestDay.getDate()!=null&&init==0){
						standupsDB.updateRow(temp.getString("email"), temp.getString("date"),temp.getString("standup_y"),temp.getString("standup_todo"),temp.getString("problem"));
					}else
						standupsDB.insertRow(temp.getString("email"), temp.getString("date"),temp.getString("standup_y"),temp.getString("standup_todo"),temp.getString("problem"));					
				}
					
			} catch (Exception e) {			
				flag = false;
				e.printStackTrace();
			}
	        
	        return flag;
	    }	          
	}
}
