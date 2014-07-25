package com.example.tabs;

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
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gztrackz.DB_Standups;
import com.example.gztrackz.R;
import com.example.gztrackz.StandUpsDialog;
import com.example.gztrackz.TimeLog;

public class StandupsFragment extends Fragment {

	private DB_Standups standupsDB;
	private String PREFERENCE_NAME = "com.example.gztrackz",FNAME = "com.example.gztrackz.firstname",LNAME = "com.example.gztrackz.lastname",EMAIL="com.example.gztrackz.email";
	private SharedPreferences prefs ;
	private boolean firstCreate=true;
	private String email;
	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_standups, container, false);
		prefs = getActivity().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);		
		email = prefs.getString(EMAIL, null);
			
		
		if(firstCreate){
			firstCreate = false;
			standupsDB = new DB_Standups(getActivity());
			standupsDB.open();
			
			//new RetrieveStandupHistory(getActivity(),email).execute();
		}
		
		return rootView;
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
	        	String urlTopTracks=null;
				
	        	urlTopTracks = "http://gz123.site90.net/get_standups/?email=" + email;
				
				HttpClient client = new DefaultHttpClient();
				ResponseHandler<String> handler = new BasicResponseHandler();
				
				HttpPost request = new HttpPost(urlTopTracks);
				
				String httpResponseTopTracks = client.execute(request, handler);				
				
				StringTokenizer token = new StringTokenizer(httpResponseTopTracks,"<");
				String retrieveResult = token.nextToken();
				JSONArray timeLogResult = new JSONArray(retrieveResult);
				JSONObject temp;
				for(int init=0;init<timeLogResult.length();init++){
					temp = timeLogResult.getJSONObject(init);
					standupsDB.insertRow(temp.getString("email"), temp.getString("date"),temp.getString("standup_y"),temp.getString("standup_todo"),temp.getString("problem"));					
				}
				Log.d("Standup HISTORY     ",retrieveResult);				
			} catch (Exception e) {			
				flag = false;
				e.printStackTrace();
			}
	        
	        return flag;
	    }	          
	}
}
