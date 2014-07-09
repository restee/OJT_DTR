package com.example.gztrackz;

import java.util.StringTokenizer;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	TextView registerTxt;
	Button logInBTN;
	EditText emailTXT, passTXT;
	String PREFERENCE_NAME = "com.example.gztrackz",FNAME = "com.example.gztrackz.firstname",LNAME = "com.example.gztrackz.lastname",EMAIL="com.example.gztrackz.email";
	String firstName,lastName,email;
	Context context;
	SharedPreferences prefs ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prefs = this.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        firstName = prefs.getString(FNAME, null);
        if(firstName!=null){
        	Intent i = new Intent(this,TimeManager.class);
        	startActivity(i);
        }
        context = this;
        logInBTN = (Button) findViewById(R.id.logInButton);
        emailTXT = (EditText) findViewById(R.id.usernameEditText);
        passTXT = (EditText) findViewById(R.id.passwordEditText);
        
        
        registerTxt = (TextView)findViewById(R.id.registerTextView);
        Typeface tf = Typeface.createFromAsset(getAssets(), "Walkway_SemiBold.ttf");
        registerTxt.setTypeface(tf);
        
        
        
        logInBTN.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				new Login(context,emailTXT.getText().toString(),passTXT.getText().toString()).execute();				
			}
		});
        
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    
    private class Login extends AsyncTask<String, Void,Boolean> {
        
    	String email,password;
    	Context context;
    	ProgressDialog progressD;
    	String firstName,lastName;
    	
    	public Login(Context context,String email,String password){
    		this.context = context;
    		this.email = email;
    		this.password = password;
    	}
    	@Override
        protected void onPreExecute() {
    		progressD = new ProgressDialog(context);
    		progressD.setMessage("Logging in...");
    		progressD.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    		progressD.show();
        }
    	  @Override
        protected void onPostExecute(Boolean result) {
        	
        	if(progressD.isShowing()){
        		progressD.dismiss();
        	}
        	if(result){
	        	Toast.makeText(context,firstName, Toast.LENGTH_LONG).show();
	        	Toast.makeText(context,lastName, Toast.LENGTH_LONG).show();
	        	Toast.makeText(context,email, Toast.LENGTH_LONG).show();
	        	Intent i = new Intent(context,TimeManager.class);
	        	startActivity(i);
        	}
        	else
        		Toast.makeText(context,"Invalid login credentials!", Toast.LENGTH_LONG).show();
        }   
    	@Override
        protected Boolean doInBackground(String... params) {
            boolean flag = true;
            
            SharedPreferences.Editor editor = prefs.edit();
            
            
            try {
            	String urlTopTracks = "http://gz123.site90.net/login/?email=" + email + "&password=" + password;
				HttpClient client = new DefaultHttpClient();
				ResponseHandler<String> handler = new BasicResponseHandler();
				Log.d("BITCH YOU MUST RETURN", "YOU MUST RETURN!");
				HttpPost request = new HttpPost(urlTopTracks);
				Log.d("BITCH YOU MUST RETURN", "YOU MUST RETURN!");
				String httpResponseTopTracks = client.execute(request, handler);				
				
				StringTokenizer token = new StringTokenizer(httpResponseTopTracks,"<");
				String retrieveResult = token.nextToken();
				
				JSONObject result = new JSONObject(retrieveResult);
				String emailResult = result.getString("email");
				if(emailResult.length()==0){					
					flag = false;
				}else{
					firstName = result.getString("first_name");
					lastName = result.getString("last_name");
					editor.putString(LNAME,lastName);
					editor.putString(FNAME, firstName);
					editor.putString(EMAIL,emailResult);
		            editor.commit();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
            return flag;
        }

              
    }
		
}
