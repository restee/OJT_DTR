package com.example.tabs;

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
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
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
	private TextView nameTXT;
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
	
	private class AlreadyLogged extends AsyncTask<String, Void,Boolean> {	        
	    	String email,password;
	    	Context context;
	    	ProgressDialog progressD;
	    	String date, time;
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
					Log.d("RESULT",emailResult);
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
    	String date, time;
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
				Log.d("RESULT",emailResult);
			} catch (Exception e) {			
				flag = false;
				e.printStackTrace();
			}
            
            return flag;
        }	             
    }
	

	
	
				
	
}
