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
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gztrackz.DB_Standups;
import com.example.gztrackz.R;
import com.example.gztrackz.ResultListAdapter;
import com.example.gztrackz.StandUpDateAdapter;
import com.example.gztrackz.StandUpsDialog;
import com.example.gztrackz.Standup;
import com.example.gztrackz.TimeLog;
import com.example.gztrackz.TimeStampQueryDialog;
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
	private Button historyQuery;
	public static String STANDUPRECEIVE =  "com.example.gztrackz.newStandup";
	
	private BroadcastReceiver standupReceiver = new BroadcastReceiver(){

		@Override
		public void onReceive(Context arg0, Intent arg1) {		
			new RetrieveStandupHistory(getActivity(),email).execute();
		}		
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_standups, container, false);
		prefs = getActivity().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);		
		email = prefs.getString(EMAIL, null);
		nothing = (TextView) rootView.findViewById(R.id.norecordfound);
		dateList = (ListView) rootView.findViewById(R.id.standupslist);
		historyQuery = (Button) rootView.findViewById(R.id.historyquerybutton);
		
		
		if(firstCreate){
			firstCreate = false;
			standupsDB = new DB_Standups(getActivity());
			standupsDB.open();
			
			
			standupList = new ArrayList();			
			if(isConnectingToInternet()){
				new RetrieveStandupHistory(getActivity(),email).execute();
			}
		}else{		
			if(standupList.size()>0){				
				nothing.setVisibility(View.INVISIBLE);
			}else
				nothing.setVisibility(View.VISIBLE);
		}
		
		getActivity().registerReceiver(standupReceiver,new IntentFilter(STANDUPRECEIVE));
		standupAdapter = new StandUpDateAdapter(getActivity(),standupList);
		dateList.setAdapter(standupAdapter);
		
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
		
		historyQuery.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(getActivity(),TimeStampQueryDialog.class);
				startActivityForResult(i,2);			
			}
		});
			
			
		return rootView;
	}
	
	@Override
	public void onDestroy() {
	
		super.onDestroy();
		getActivity().unregisterReceiver(standupReceiver);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {	
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==2){
			if(resultCode==getActivity().RESULT_OK){
				Log.d("year",Integer.toString(data.getIntExtra("year",0)));
				Log.d("month",Integer.toString(data.getIntExtra("month",0)));
				Log.d("day",Integer.toString(data.getIntExtra("day",0)));
				int day = data.getIntExtra("day",0);
				int month = data.getIntExtra("month",0);
				int year = data.getIntExtra("year",0);
				String date2,date;
				if(year!=0){
					date = Integer.toString(year) + "-" ;
					
					if(month!=0){
						if(month<10){
							date = date +"0" + Integer.toString(month) + "-";			
						}else{
							date = date + Integer.toString(month) + "-";
						}
						if(day!=0){
							if(day<10){
								date = date + "0" + Integer.toString(day) + " 00:00:00";
							}else{
								date = date + Integer.toString(day) + " 00:00:00";
							}
							
							date2 = nextDay(year,month,day);
							Log.d("Today",date);
							Log.d("Tomorrow", date2);
						}else{
							date = date + "01 00:00:00";
							date2 = nextMonth(year,month);							
							Log.d("Today",date);
							Log.d("Next Month", date2);							
						}
					}else{

						date = date + "01-01 00:00:00";
						date2 = Integer.toString(year+1) +"-01-01 00:00:00"; 
						Log.d("Today",date);
						Log.d("Next Year", date2);						
					}		
					standupList = standupsDB.getAllDay(email, date, date2);
					if(standupList.size()>0){						
						standupAdapter = new StandUpDateAdapter(getActivity(),standupList);
						dateList.setAdapter(standupAdapter);
						nothing.setVisibility(View.INVISIBLE);
					}else{
						
						nothing.setVisibility(View.VISIBLE);
					}
					
				}
			}			
		}
		
	}
	
	
	private String nextDay(int year,int month, int day){
		String flag=null;
		day++;
		if((month==1||month==3||month==5||month==7||month==8||month==10||month==12 )&& day==32){
			day = 1;
			month++;
		}else if((month==4||month==6||month==9||month==11) && day==31){
			day = 1;
			month++;
		}else if (month==2){			
			if(year%4==0 && day==30){
				day = 1;
				month++;
			}else if (day==29){
				day=1;
				month++;
			}
		}
		
		if(month==13){
			month=1;
			year++;
		}
		flag = Integer.toString(year) + "-" ;
		
		if(month<10){
			flag = flag +"0" + Integer.toString(month) + "-";			
		}else{
			flag = flag + Integer.toString(month) + "-";
		}
		
		if(day<10){
			flag = flag + "0" + Integer.toString(day) + " 00:00:00";
		}else{
			flag = flag + Integer.toString(day) + " 00:00:00";
		}
		
		return flag;
	}
	
	private String nextMonth(int year,int month){
		String flag = null;
		month++;
		if(month==13){
			year++;
			month=1;
		}
		flag = Integer.toString(year);
		
		if(month<10){
			flag = flag + "-0" + Integer.toString(month) + "-01 00:00:00";
		}else{
			flag = flag + "-" + Integer.toString(month) + "-01 00:00:00";			
		}
		
		return flag;
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
