package com.example.gztrackz;

import java.util.StringTokenizer;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ProgressDialog;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.example.tabs.HomeFragment.MyInterface;
import com.example.tabsadapter.TabsPagerAdapter;


@SuppressLint("NewApi")
public class TabsManager extends FragmentActivity implements ActionBar.TabListener,MyInterface  {

	private ViewPager viewPager;
	private TabsPagerAdapter mAdapter;
	private ActionBar actionBar;
	// Tab titles
	private String[] tabs = { "Home", "History", "StandUps" };
	
	private String email;
	private DB_USER_TIME_LOG timeLogDB;
	private Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tabs_manager);
		timeLogDB = new DB_USER_TIME_LOG(this);
		timeLogDB.open();
		// Initialization
		viewPager = (ViewPager) findViewById(R.id.pager);
		actionBar = getActionBar();
		mAdapter = new TabsPagerAdapter(getSupportFragmentManager());
		context  = this;
		viewPager.setAdapter(mAdapter);
		actionBar.setHomeButtonEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);		
		actionBar.setDisplayShowTitleEnabled(false);	
		email = getIntent().getStringExtra("email");
		
		
		// Adding Tabs
		for (String tab_name : tabs) {
			actionBar.addTab(actionBar.newTab().setText(tab_name)
					.setTabListener(this));
		}
					
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// on changing the page
				// make respected tab selected
				actionBar.setSelectedNavigationItem(position);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// on tab selected
		// show respected fragment view
		viewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	}

	@Override
	public void buttonClicked(View v,boolean timeIn) {
		Toast.makeText(getApplicationContext(), email, Toast.LENGTH_LONG).show();	
		new TimeLog(context, email,timeIn).execute();
	}

	
	
	
	private class TimeLog extends AsyncTask<String, Void,Boolean> {	        
	    	String email,password;
	    	Context context;
	    	ProgressDialog progressD;
	    	String date, time;
	    	boolean timeIn;
	    	
	    	public TimeLog(Context context,String email,boolean timeIn){
	    		this.context = context;
	    		this.email = email;
	    		this.password = password;
	    		Log.d("Constructor TimeIn", Boolean.toString(timeIn));
	    		this.timeIn = timeIn;
	    	}
	    	
	    	@Override
	        protected void onPreExecute() {
	    		progressD = new ProgressDialog(context);	    		
	    		if(timeIn)
	    			progressD.setMessage("TimeOut in progress...");
	    		else
	    			progressD.setMessage("TimeIn in progress...");
	    		progressD.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	    		progressD.show();
	    		Log.d("PreCheck",Boolean.toString(timeIn));
	    			    			    
	        }
	    	
	    	@Override
	        protected void onPostExecute(Boolean result) {        	
	        	if(progressD.isShowing()){
	        		progressD.dismiss();
	        	}
	        	if(result){	        
	        		Toast.makeText(context,"Date: " + date + "\nTime: " + time, Toast.LENGTH_LONG).show();	 	        			        		
	        	}
	        	else
	        		Toast.makeText(context,"Unable to execute time in. Please check internet connection!", Toast.LENGTH_LONG).show();	        	
	        	
	    	}
	    	
	    	@Override
	        protected Boolean doInBackground(String... params) {	    		
	    		boolean flag = true;	
	            
	            try {	            	
	            	String urlTopTracks ;
					HttpClient client = new DefaultHttpClient();
					ResponseHandler<String> handler = new BasicResponseHandler();
					
					HttpPost request ;
					
					String httpResponseTopTracks;				
					
					StringTokenizer token;
					String retrieveResult;
					
					JSONObject result;
					String emailResult;	            		            
					Log.d("PreCheck",Boolean.toString(timeIn));
	            	if(timeIn){
	            		urlTopTracks = "http://gz123.site90.net/timeout/?email=" + email ;										
						request = new HttpPost(urlTopTracks);					
						httpResponseTopTracks = client.execute(request, handler);				
						
						 token = new StringTokenizer(httpResponseTopTracks,"<");
						retrieveResult = token.nextToken();
						
						result = new JSONObject(retrieveResult);
						emailResult = result.getString("email");
						if(emailResult.length()==0){					
							flag = false;
						}else{
							date = result.getString("date");
							time = result.getString("time");
							flag = true;
						}
						Log.d("Time out",Boolean.toString(timeIn));
	            	}
	            	else{
		            	urlTopTracks = "http://gz123.site90.net/timein/?email=" + email ;										
						request = new HttpPost(urlTopTracks);					
						httpResponseTopTracks = client.execute(request, handler);				
						
						 token = new StringTokenizer(httpResponseTopTracks,"<");
						retrieveResult = token.nextToken();
						
						result = new JSONObject(retrieveResult);
						emailResult = result.getString("email");
						if(emailResult.length()==0){					
							flag = false;
						}else{
							date = result.getString("date");
							time = result.getString("time");
							flag = true;
						}
						Log.d("Time In",Boolean.toString(timeIn));
					}
				} catch (Exception e) {
					flag = false;
					e.printStackTrace();
				}
	            
	            return flag;
	        }	             
	    }
	

	
}
