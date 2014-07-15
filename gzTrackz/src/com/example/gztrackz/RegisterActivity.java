package com.example.gztrackz;

import java.util.StringTokenizer;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends Activity {

	private EditText emailTxt, firstNameTxt, lastNameTxt, passwordTxt, confirmPasswordTxt;
	private Button registerBtn;
	private Context context;
	private String email,firstName,lastName,password,confirmPassword;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		
		emailTxt = (EditText) findViewById(R.id.emailRegisterEditText);
		passwordTxt = (EditText) findViewById(R.id.passwordRegisterEditText);
		firstNameTxt = (EditText) findViewById(R.id.firstnameEditText);
		lastNameTxt = (EditText) findViewById(R.id.lastnameEditText);
		confirmPasswordTxt = (EditText) findViewById(R.id.confirmpasswordEditText);
		registerBtn = (Button) findViewById(R.id.registerButton);
		context = this;
		
							
		registerBtn.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				email = emailTxt.getText().toString();
				firstName = firstNameTxt.getText().toString();
				lastName = lastNameTxt.getText().toString();
				password = passwordTxt.getText().toString();
				confirmPassword = confirmPasswordTxt.getText().toString();
				
				if(email.length()>0&&firstName.length()>0&&lastName.length()>0&&password.length()>0&&confirmPassword.length()>0){
					if(password.compareTo(confirmPassword)==0){
						new Register(context,email,firstName,lastName,password,confirmPassword).execute();
					}else{
						Toast.makeText(context,"Passwords don't match!", Toast.LENGTH_LONG).show();					
					}
				}else{
					Toast.makeText(context,"Please fill all fields!", Toast.LENGTH_LONG).show();
				}
			}
		});
				
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register, menu);
		return true;
	}

	 private class Register extends AsyncTask<String, Void,String> {
	        
	    	String email,password,firstName,lastName,confirmPassword;
	    	Context context;
	    	ProgressDialog progressD;
	    	
	    	
	    	public Register(Context context,String email,String firstName,String lastName,String password,String confirmPassword){
	    		this.context = context;
	    		this.email = email;
	    		this.password = password;
	    		this.firstName = firstName;
	    		this.lastName = lastName;
	    		this.confirmPassword = confirmPassword;
	    	}
	    	@Override
	        protected void onPreExecute() {
	    		progressD = new ProgressDialog(context);
	    		progressD.setMessage("Registering...");
	    		progressD.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	    		progressD.setCancelable(false);
	    		progressD.setCanceledOnTouchOutside(false);
	    		progressD.show();
	        }
	    	  @Override
	        protected void onPostExecute(String result) {
	        	
	        	if(progressD.isShowing()){
	        		progressD.dismiss();
	        	}	        		        	
		        Toast.makeText(context,result, Toast.LENGTH_LONG).show();
	        	if(result.compareToIgnoreCase("registration successful")==0){	        		
	        		finish();
	        	}
	        }   
	    	@Override
	        protected String doInBackground(String... params) {
	            String flag=null;
	            	            	            	            
	            try {
	            	String urlTopTracks = "http://gz123.site90.net/register/?email=" + email + "&password=" + password +
	            			"&first_name=" + firstName + "&last_name=" + lastName;
					HttpClient client = new DefaultHttpClient();
					ResponseHandler<String> handler = new BasicResponseHandler();
					
					HttpPost request = new HttpPost(urlTopTracks);
					
					String httpResponseTopTracks = client.execute(request, handler);				
					
					StringTokenizer token = new StringTokenizer(httpResponseTopTracks,"<");
					String retrieveResult = token.nextToken();
					
					JSONObject result = new JSONObject(retrieveResult);
					flag = result.getString("result");
										
				} catch (Exception e) {
					flag = "Unexpected error occured.";
					e.printStackTrace();
				}
	            
	            return flag;
	        }

	              
	    }
}
