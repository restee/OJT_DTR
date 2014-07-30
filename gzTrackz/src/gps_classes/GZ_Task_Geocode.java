package gps_classes;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class GZ_Task_Geocode extends AsyncTask<String, Void, Boolean> {

	private final static String TAG = "GZ_Task_Geocode";

	private final static String GEOCODE_URL = "http://maps.googleapis.com/maps/api/geocode/json?"
			+ "&latlng=";

	Context context;
	private String latitude, longitude;
	private String address;

	public GZ_Task_Geocode(Context context, double latitude, double longitude) {

		this.context = context;
		this.latitude = String.valueOf(latitude);
		this.longitude = String.valueOf(longitude);
	}

	@Override
	protected Boolean doInBackground(String... arg0) {

		Boolean geocode_successful = false;
		String jsonData = null;
		InputStream inputStream = null;

		try {
			// HTTP Client that supports streaming uploads and downloads
			DefaultHttpClient httpclient = new DefaultHttpClient(
					new BasicHttpParams());

			// Define that I want to use the POST method to grab data from
			// the provided URL
			HttpPost httppost = new HttpPost(GEOCODE_URL + latitude + ","
					+ longitude);

			// Web service used is defined. type JSON
			httppost.setHeader("Content-type", "application/json");

			// Will hold the whole all the data gathered from the URL
			jsonData = retrieveJSONData(inputStream, httpclient, httppost);

		} catch (Exception e) {

			e.printStackTrace();

		} finally {

			// Close the InputStream when you're done with it
			try {
				if (inputStream != null)
					inputStream.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		try {

			geocode_successful = parseJSONData(jsonData);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return geocode_successful;
	}

	private String retrieveJSONData(InputStream inputStream,
			HttpClient httpclient, HttpPost httppost)
			throws IllegalStateException, IOException {

		// Get a response if any from the web service
		HttpResponse response = httpclient.execute(httppost);

		// The content from the requested URL along with headers, etc.
		HttpEntity entity = response.getEntity();

		// Get the main content from the URL
		inputStream = entity.getContent();

		// JSON is UTF-8 by default
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				inputStream, "UTF-8"), 8);

		// store the data
		StringBuilder jsonData = new StringBuilder();

		String line = null;

		// Read in the data from the Buffer until nothing is left
		while ((line = reader.readLine()) != null) {

			// Add data from the buffer to the StringBuilder
			jsonData.append(line + "\n");
		}

		return jsonData.toString();
	}

	private boolean parseJSONData(String jsonData) throws JSONException {
		Log.d("JSON DATA", jsonData);
		boolean geocode_successful = false;

		// Get the root JSONObject
		JSONObject jsonGeocode = new JSONObject(jsonData);

		Log.d(TAG, jsonData);

		if (jsonGeocode != null && jsonGeocode.getString("status").equals("OK")) {

			address = jsonGeocode.getJSONArray("results").getJSONObject(1)
					.getString("formatted_address");

			Log.d("STATUS", "STATUS = " + jsonGeocode.getString("status"));
			Log.d("STATUS", "address = " + address);

			geocode_successful = true;
		}

		return geocode_successful;

	}

	@Override
	protected void onPostExecute(Boolean geocode_successful) {
		super.onPostExecute(geocode_successful);

		if (geocode_successful) {
			Toast.makeText(context, address, Toast.LENGTH_LONG)
					.show();
		} else {

			Toast.makeText(
					context,
					"Can not retrieve location for:\nLatitude: " + latitude
							+ "\nLongitude:" + longitude, Toast.LENGTH_LONG)
					.show();
		}
	}
}