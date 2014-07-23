package com.example.tabs;

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
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.gztrackz.DB_User_Time_Log;
import com.example.gztrackz.R;
import com.example.gztrackz.StandUpsDialog;
import com.example.gztrackz.TimeLog;
import com.example.gztrackz.TimeStampQueryDialog;

public class TimestampsFragment extends Fragment {
	
	private String PREFERENCE_NAME = "com.example.gztrackz",FNAME = "com.example.gztrackz.firstname",LNAME = "com.example.gztrackz.lastname",EMAIL="com.example.gztrackz.email";
	private SharedPreferences prefs ;
	private String email;
	private boolean firstCreate=true;
	private DB_User_Time_Log timeLogDB;
	private List<TimeLog> timelogs;
	private Button queryBTN;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_timestamp, container, false);
		prefs = getActivity().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);		
		email = prefs.getString(EMAIL, null);
		
		
		if(firstCreate){
			timeLogDB = new DB_User_Time_Log(getActivity());
			timeLogDB.open();
			new RetrieveTimeLogHistory(getActivity(),email).execute();		
			
			timelogs = timeLogDB.getAllRowOf(email);
			
			for(int init = 0; init<timelogs.size();init++){
				Log.d(timelogs.get(init).getEmail(),timelogs.get(init).getTimeIn() + "      " + timelogs.get(init).getTimeOut());
			}
			firstCreate = false;
		}
		
		queryBTN = (Button) rootView.findViewById(R.id.historyquerybutton);		
		queryBTN.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(getActivity(),TimeStampQueryDialog.class);
				startActivityForResult(i,1);
			}
		});
		
		return rootView;
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {	
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==1){
			if(resultCode==getActivity().RESULT_OK){
				Log.d("year",data.getStringExtra("year")+"");
				Log.d("month",data.getStringExtra("month")+"");
				Log.d("day",data.getStringExtra("day")+"");
			}			
		}
		
	}

	private class RetrieveTimeLogHistory extends AsyncTask<String, Void,Boolean> {	        
		String email;
		Context context;
		
		public RetrieveTimeLogHistory(Context context,String email){
			this.context = context;
			this.email = email;			
		}
		
		@Override
	    protected void onPreExecute() {
			Log.d("STARTED RETRIEVING TIMELOG HISTORY","GOGOGOGOGO");
	    }
		
		@Override
	    protected void onPostExecute(Boolean result) {        	
	    	
	    }
		
		@Override
	    protected Boolean doInBackground(String... params) {
	        boolean flag = true;	            
	        try {
	        	
	        	String urlTopTracks;
				TimeLog latest =timeLogDB.getLatestRowOf(email);
				Log.d("LATEST","");
				if(latest.getEmail()!=null){
					String timein = latest.getTimeIn();
					timein = timein.replace(" ", "%20");
					urlTopTracks = "http://gz123.site90.net/get_timein_greater/?email=" + email +"&timein=" + timein;
					Log.d("CALLED", latest.getEmail() + "'");
				}else{
					urlTopTracks = "http://gz123.site90.net/get_userlog/?email=" + email ;
					Log.d("CALLED NONE", latest.getEmail() + "");
					
				}
				
				HttpClient client = new DefaultHttpClient();
				ResponseHandler<String> handler = new BasicResponseHandler();
				
				HttpPost request = new HttpPost(urlTopTracks);
				
				String httpResponseTopTracks = client.execute(request, handler);				
				
				StringTokenizer token = new StringTokenizer(httpResponseTopTracks,"<");
				String retrieveResult = token.nextToken();
				JSONArray timeLogResult = new JSONArray(retrieveResult);
				JSONObject temp;
				for(int init = 0;init<timeLogResult.length();init++){					
					temp = timeLogResult.getJSONObject(init);
					if(latest.getEmail()!=null&&init==0){
						timeLogDB.updateRow(email,temp.getString("timein") , temp.getString("timeout"));
					}else
						timeLogDB.insertRow(email,temp.getString("timein") , temp.getString("timeout"));
				}
				Log.d("TIMELOG HISTORY     " + Integer.toString(timeLogResult.length()),retrieveResult);				
			} catch (Exception e) {			
				flag = false;
				e.printStackTrace();
			}
	        
	        return flag;
	    }	          
	}
}
