package com.example.gztrackz;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
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
        	Toast.makeText(getApplicationContext(), firstName,Toast.LENGTH_LONG).show();
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
        protected Boolean doInBackground(String... params) {
            boolean flag = false;
            
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(FNAME, "GlendonGwapo");            
            editor.commit();
            
            try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
            return flag;
        }

        @Override
        protected void onPostExecute(Boolean result) {
        	
        	if(progressD.isShowing()){
        		progressD.dismiss();
        	}
        }           
    }
		
}
