package com.example.gztrackz;

import java.util.StringTokenizer;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class StandUpsDialog extends Activity {

	
	private EditText standup_yTXT,standup_todoTXT,problemTXT;
	private Button done;
	private Context context;
	private String email;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stand_ups_dialog);
		email = getIntent().getStringExtra("email");
		standup_yTXT = (EditText) findViewById(R.id.standupsprevious);
		standup_todoTXT = (EditText) findViewById(R.id.standupsnow);
		problemTXT = (EditText) findViewById(R.id.standupsproblem);
		done = (Button) findViewById(R.id.standupdone);
		context = this;
		done.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				String standup_y,standup_todo,problem;
				standup_y = standup_yTXT.getText().toString();
				standup_todo = standup_todoTXT.getText().toString();
				problem = problemTXT.getText().toString();
				
				if(standup_y.length()>0&&standup_todo.length()>0&&problem.length()>0){					
					new AddStandup(context,email,standup_y,standup_todo,problem).execute();
				}
				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.stand_ups_dialog, menu);
		return true;
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		
	}
	
	 private class AddStandup extends AsyncTask<String, Void,Boolean> {
	        
	    	String email,password;
	    	Context context;
	    	ProgressDialog progressD;
	    	String firstName,lastName;
	    	String standup_y,standup_todo,problem;
	    	
	    	
	    	public AddStandup(Context context,String email,String standup_y,String standup_todo,String problem){
	    		this.context = context;
	    		this.email = email;
	    		this.standup_y = standup_y;
	    		this.standup_todo = standup_todo;
	    		this.problem = problem;
	    	}
	    	@Override
	        protected void onPreExecute() {
	    		progressD = new ProgressDialog(context);
	    		progressD.setMessage("Adding standup...");
	    		progressD.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	    		progressD.setCanceledOnTouchOutside(false);
	    		progressD.show();
	        }
	    	
	    	@Override
	        protected void onPostExecute(Boolean result) {        	
	        	if(progressD.isShowing()){
	        		progressD.dismiss();
	        	}
	        	if(result){	        
		        	finish();
	        	}
	        	else
	        		Toast.makeText(context,"Invalid login credentials!", Toast.LENGTH_LONG).show();
	        }   
	    	@Override
	        protected Boolean doInBackground(String... params) {
	            boolean flag = true;            
	            
	            try {
	            	standup_y = standup_y.replace(" ","%20");
	            	standup_todo = standup_todo.replace(" ","%20");
	            	problem = problem.replace(" ","%20");
	            	String urlTopTracks = "http://gz123.site90.net/standups/?email=" + email + "&standup_y=" + standup_y
	            			+"&standup_todo=" + standup_todo + "&problem=" + problem;
					HttpClient client = new DefaultHttpClient();
					ResponseHandler<String> handler = new BasicResponseHandler();
					
					HttpPost request = new HttpPost(urlTopTracks);
					
					String httpResponseTopTracks = client.execute(request, handler);				
					
					StringTokenizer token = new StringTokenizer(httpResponseTopTracks,"<");
					String retrieveResult = token.nextToken();									
					
				} catch (Exception e) {
					flag = false;
					e.printStackTrace();
				}
	            
	            return flag;
	        }	             
	    }
	 
}
