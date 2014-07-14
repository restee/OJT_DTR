package com.example.tabs;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gztrackz.R;
import com.example.service.TimeService;


public class HomeFragment extends Fragment {

	private ImageView timeLogBTN;
	public MyInterface homeInterface;
	public interface MyInterface {
	    public void buttonClicked(boolean timeIn);
	}
	private String PREFERENCE_NAME = "com.example.gztrackz",FNAME = "com.example.gztrackz.firstname",LNAME = "com.example.gztrackz.lastname",EMAIL="com.example.gztrackz.email";
	private SharedPreferences prefs ;
	String email;
	boolean loggedIn,checked=false;
	private TextView nameTXT,timeTXT,dateTXT,amPmTXT;
	
	private Intent timeServiceIntent;
	private Thread timeThread;
	
	BroadcastReceiver timeReceiver = new BroadcastReceiver(){
		@Override
		public void onReceive(Context arg0, Intent arg1) {
			int hours = Integer.parseInt(arg1.getStringExtra("hours"));
			int minutes = Integer.parseInt(arg1.getStringExtra("minutes"));
			String hourDisplay=null,minutesDisplay;
			if(hours>12){
				hours-=12;				
				amPmTXT.setText("PM");
			}
			if(hours<10){
				   hourDisplay ="0" + Integer.toString(hours);
			}
			else{
				hourDisplay = Integer.toString(hours);
			}
			if(minutes<10){
				minutesDisplay = "0" + Integer.toString(minutes);
			}
			else{
				minutesDisplay = Integer.toString(minutes);
			}
			timeTXT.setText(hourDisplay + ":" + minutesDisplay);
		}		
	};
	
	private ServiceConnection timeConnection = new ServiceConnection(){

		@Override
		public void onServiceConnected(ComponentName className, IBinder service) {

			
		}

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			// TODO Auto-generated method stub
			
		}
		
	};
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_home, container, false);
		prefs = getActivity().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
		TextView timeTxt = (TextView) rootView.findViewById(R.id.timeTxt);
		TextView dateTxt = (TextView) rootView.findViewById(R.id.dateTxt);
		TextView nameTxt = (TextView) rootView.findViewById(R.id.name);
		Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "Walkway_Bold.ttf");
		Typeface tf2 = Typeface.createFromAsset(getActivity().getAssets(), "CODE Bold.otf");
		Typeface tf3 = Typeface.createFromAsset(getActivity().getAssets(), "Nexa Light.otf");
		timeTxt.setTypeface(tf);
		dateTxt.setTypeface(tf2);
		nameTxt.setTypeface(tf3);
		timeLogBTN = (ImageView) rootView.findViewById(R.id.timeLogBTN);
		email = prefs.getString(EMAIL, null);
		nameTXT = (TextView) rootView.findViewById(R.id.name);
		nameTXT.setText("Hello, " + prefs.getString(FNAME,null) + "!");
		timeTXT = (TextView) rootView.findViewById(R.id.timeTxt);
		dateTXT = (TextView) rootView.findViewById(R.id.dateTxt);
		amPmTXT = (TextView) rootView.findViewById(R.id.ampmTxt);
		
		getActivity().registerReceiver(timeReceiver,new IntentFilter("gztrackz.update.time"));
		
		
		
		timeThread = new Thread();
		if(!checked){
			new AlreadyLogged(getActivity(),email).execute();
			checked=true;
		}
		
		timeLogBTN.setOnClickListener(new View.OnClickListener() {		
			@Override
			public void onClick(View v) {								
				new AlreadyLoggedCheck(getActivity(),email).execute();
				Log.d("CHECK",Boolean.toString(loggedIn));
						
			}
		});
	   
	    
		return rootView;
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try{
			homeInterface = (MyInterface) activity;
			
		}catch(Exception e){}
	}
		
	 private boolean isNetworkAvailable() {
	        ConnectivityManager connectivityManager 
	              = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
	        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

	 }
	 @Override
	public void onDestroy() {
		super.onDestroy();
		getActivity().unregisterReceiver(timeReceiver);
	}
	
	private class AlreadyLogged extends AsyncTask<String, Void,Boolean> {	        
	    	String email,password;
	    	Context context;
	    	ProgressDialog progressD;
	    	String date=null, time=null;
	    	boolean timeIn;
	    	
	    	public AlreadyLogged(Context context,String email){
	    		this.context = context;
	    		this.email = email;
	    		this.password = password;
	    	}
	    	
	    	@Override
	        protected void onPreExecute() {
	    		progressD = new ProgressDialog(context);
	    		progressD.setMessage("Checking User Timelog Status!");
	    		progressD.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	    		progressD.show();
	        }
	    	
	    	@Override
	        protected void onPostExecute(Boolean result) {        	
	        	if(progressD.isShowing()){
	        		progressD.dismiss();
	        	}	        		
	        	loggedIn = timeIn;
	        	if(loggedIn){
					//timeLogBTN.setText("Log Out!");	
					timeLogBTN.setImageResource(R.drawable.inactivetimeout);
				}
				else{ 
				//	timeLogBTN.setText("Log In!");
					timeLogBTN.setImageResource(R.drawable.inactivetimein);
				}
	        	if(date!=null&&time!=null){	        			        		 
	        		String dateDisplay,dayOfTheWeek=null,stringMonth=null;
	        		SimpleDateFormat  format = new SimpleDateFormat("yyyy-MM-dd");	        		
	        		try {  
	        		    Date dateOutput = format.parse(date);  
	        		    dayOfTheWeek = (String) android.text.format.DateFormat.format("EEEE", dateOutput);
	        		    stringMonth = (String) android.text.format.DateFormat.format("MMM", dateOutput);
	        		    Toast.makeText(context,dayOfTheWeek,Toast.LENGTH_SHORT).show(); 
	        		} catch (Exception e) {    
	        		    e.printStackTrace();  
	        		}
	        		
	        		dateDisplay = dayOfTheWeek + ", " + stringMonth+ " " + date.substring(8,date.length());
	        		dateTXT.setText(dateDisplay);

	        		Toast.makeText(context, time.substring(0, 2) + ":" + time.substring(3,5), Toast.LENGTH_SHORT).show();
	        		timeThread = new Thread(){
	        			 @Override
	        			    public void run() {
	        			        try {	        			        	
	        			        	int minutes = Integer.parseInt(time.substring(3,5));
	        			        	int hours = Integer.parseInt(time.substring(0,2));
	        			            while(true) {
	        			            	Intent sendTimeBroadcast = new Intent();
	        			            	sendTimeBroadcast.setAction("gztrackz.update.time");
	        			            	sendTimeBroadcast.putExtra("minutes",Integer.toString(minutes));
	        			            	sendTimeBroadcast.putExtra("hours",Integer.toString(hours));
	        			            	getActivity().sendBroadcast(sendTimeBroadcast);
	        			                sleep(60000);
	        			                minutes++;
	        			                if(minutes==60){
	        			                	hours++;
	        			                	minutes=0;
	        			                }	        			                
	        			            }
	        			        } catch (InterruptedException e) {
	        			            e.printStackTrace();
	        			        }
	        			    }
	        		};
	        		timeThread.start();
	        		/*dateTXT.setText(date);
	        		timeTXT.setText(time);*/
	        	}else{
	        		dateTXT.setText("--------, ----- -");
	        		
	        	}
	        }
	    	
	    	@Override
	        protected Boolean doInBackground(String... params) {
	            boolean flag = true;	            
	            try {
	            	String urlTopTracks = "http://gz123.site90.net/loginstatus/?email=" + email ;
					HttpClient client = new DefaultHttpClient();
					ResponseHandler<String> handler = new BasicResponseHandler();
					
					HttpPost request = new HttpPost(urlTopTracks);
					
					String httpResponseTopTracks = client.execute(request, handler);				
					
					StringTokenizer token = new StringTokenizer(httpResponseTopTracks,"<");
					String retrieveResult = token.nextToken();
					
					JSONObject result = new JSONObject(retrieveResult);
					String emailResult = result.getString("active");
					if(emailResult.compareToIgnoreCase("true")==0){
						timeIn = true;						
					}else{
						timeIn = false;
					}
					date = result.getString("date");
					time = result.getString("time");
					Log.d("RESULT",retrieveResult);
					
				} catch (Exception e) {			
					flag = false;
					e.printStackTrace();
				}
	            
	            return flag;
	        }	             
	    }
		
	
	
	private class AlreadyLoggedCheck extends AsyncTask<String, Void,Boolean> {	        
    	String email,password;
    	Context context;
    	ProgressDialog progressD;
    	String date=null, time=null;
    	boolean timeIn;
    	
    	public AlreadyLoggedCheck(Context context,String email){
    		this.context = context;
    		this.email = email;
    		this.password = password;
    	}
    	
    	@Override
        protected void onPreExecute() {
    		progressD = new ProgressDialog(context);
    		progressD.setMessage("Checking User Timelog Status!");
    		progressD.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    		progressD.show();
        }
    	
    	@Override
        protected void onPostExecute(Boolean result) {        	
        	if(progressD.isShowing()){
        		progressD.dismiss();
        	}	        		
        	loggedIn = timeIn;
        	if(!loggedIn){
				timeLogBTN.setImageResource(R.drawable.inactivetimeout);
				homeInterface.buttonClicked(loggedIn);
			}
			else{ 
								
				DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
				    @Override
				    public void onClick(DialogInterface dialog, int which) {
				        switch (which){
				        case DialogInterface.BUTTON_POSITIVE:
				        	homeInterface.buttonClicked(loggedIn);
				        	timeLogBTN.setImageResource(R.drawable.inactivetimein);
				            break;
				        case DialogInterface.BUTTON_NEGATIVE:
				            break;
				        }
				    }
				};
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
				    .setNegativeButton("No", dialogClickListener).show();
				
			}
        	
        	if(date!=null&&time!=null){	        			        		 
        		String dateDisplay,dayOfTheWeek=null,stringMonth=null;
        		SimpleDateFormat  format = new SimpleDateFormat("yyyy-MM-dd");	        		
        		try {  
        		    Date dateOutput = format.parse(date);  
        		    dayOfTheWeek = (String) android.text.format.DateFormat.format("EEEE", dateOutput);
        		    stringMonth = (String) android.text.format.DateFormat.format("MMM", dateOutput);
        		    Toast.makeText(context,dayOfTheWeek,Toast.LENGTH_SHORT).show(); 
        		} catch (Exception e) {  
        		    // TODO Auto-generated catch block  
        		    e.printStackTrace();  
        		}
        		
        		dateDisplay = dayOfTheWeek + ", " + stringMonth+ " " + date.substring(8,date.length());
        		dateTXT.setText(dateDisplay);
        		//timeThread.stop();
        		Toast.makeText(context, time.substring(0, 2) + ":" + time.substring(3,5), Toast.LENGTH_SHORT).show();
        		timeThread = new Thread(){
        			 @Override
        			    public void run() {
        			        try {	        			        	
        			        	int minutes = Integer.parseInt(time.substring(3,5));
        			        	int hours = Integer.parseInt(time.substring(0,2));
        			            while(true) {
        			            	Intent sendTimeBroadcast = new Intent();
        			            	sendTimeBroadcast.setAction("gztrackz.update.time");
        			            	sendTimeBroadcast.putExtra("minutes",Integer.toString(minutes));
        			            	sendTimeBroadcast.putExtra("hours",Integer.toString(hours));
        			            	getActivity().sendBroadcast(sendTimeBroadcast);
        			                sleep(60000);
        			                minutes++;
        			                if(minutes==60){
        			                	hours++;
        			                	minutes=0;
        			                }	        			                
        			            }
        			        } catch (InterruptedException e) {
        			            e.printStackTrace();
        			        }
        			    }
        		};
        		timeThread.start();
        		/*dateTXT.setText(date);
        		timeTXT.setText(time);*/
        	}else{
        		dateTXT.setText("--------, ----- -");        		
        	}
        }
    	
    	@Override
        protected Boolean doInBackground(String... params) {
            boolean flag = true;	            
            try {
            	String urlTopTracks = "http://gz123.site90.net/loginstatus/?email=" + email ;
				HttpClient client = new DefaultHttpClient();
				ResponseHandler<String> handler = new BasicResponseHandler();
				
				HttpPost request = new HttpPost(urlTopTracks);
				
				String httpResponseTopTracks = client.execute(request, handler);				
				
				StringTokenizer token = new StringTokenizer(httpResponseTopTracks,"<");
				String retrieveResult = token.nextToken();
				
				JSONObject result = new JSONObject(retrieveResult);
				String emailResult = result.getString("active");
				if(emailResult.compareToIgnoreCase("true")==0){
					timeIn = true;
					
				}else{
					timeIn = false;
				}
				date = result.getString("date");
				time = result.getString("time");
				Log.d("RESULT",emailResult);
			} catch (Exception e) {			
				flag = false;
				e.printStackTrace();
			}
            
            return flag;
        }	             
    }
	

	
				
	
}
