package com.example.login;

import java.util.StringTokenizer;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import com.example.utilities.SharedPrefUtil;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;


public class Task_Login extends AsyncTask<String, Void, Boolean> {

	String email, password;
	Context context;
	ProgressDialog progressDialog;
	String firstName, lastName;
	SharedPreferences prefs;
	

	public Task_Login(Context context, String email, String password, SharedPreferences prefs) {
		
		this.context = context;
		this.email = email;
		this.password = password;
		this.prefs = prefs;
		
	}
	
	@Override
	protected Boolean doInBackground(String... params) {
		boolean flag = true;
		SharedPreferences.Editor editor = prefs.edit();

		try {
			String urlTopTracks = "http://gz123.site90.net/login/?email="
					+ email + "&password=" + password;
			HttpClient client = new DefaultHttpClient();
			ResponseHandler<String> handler = new BasicResponseHandler();

			HttpPost request = new HttpPost(urlTopTracks);

			String httpResponseTopTracks = client.execute(request, handler);

			StringTokenizer token = new StringTokenizer(httpResponseTopTracks,
					"<");
			String retrieveResult = token.nextToken();

			JSONObject result = new JSONObject(retrieveResult);
			String emailResult = result.getString("email");
			if (emailResult.length() == 0) {
				flag = false;
			} else {
				
				firstName = result.getString("first_name");
				lastName = result.getString("last_name");
				editor.putString(SharedPrefUtil.KEY_LNAME, lastName);
				editor.putString(SharedPrefUtil.KEY_FNAME, firstName);
				editor.putString(SharedPrefUtil.KEY_EMAIL, emailResult);
				editor.commit();
				
			}
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
		}

		return flag;
	}

	@Override
	protected void onPreExecute() {
		progressDialog = new ProgressDialog(context);
		progressDialog.setMessage("Logging in...");
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.show();
	}

	@Override
	protected void onPostExecute(Boolean result) {
		if (progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
		
		Activity_Login loginActivity = (Activity_Login) context;
		loginActivity.performLoginBasedOnResult(result);
		
	}

}