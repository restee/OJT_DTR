package com.example.scrumgztrack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;
import android.util.Log;

public class Task_AddRemoveOJT extends AsyncTask<String, Void, Void> {

	@Override
	protected Void doInBackground(String... params) {
		
		String OjtEmail= "ojt_email";
		String OjtTeam= "ojt_team";
		
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(params[0]);

		try {
			
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			
			nameValuePairs.add(new BasicNameValuePair(OjtEmail, params[1]));
			nameValuePairs.add(new BasicNameValuePair(OjtTeam, params[2]));
			
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			// Execute HTTP Post Request
			HttpResponse response = httpclient.execute(httppost);
			
			response = httpclient.execute(httppost);
			String responseStr = EntityUtils.toString(response.getEntity());
			
			Log.d("task add remove", responseStr);

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}

		return null;
	}

}
