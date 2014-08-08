package com.example.tabs;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.adapter.OJTlistAdapter;
import com.example.scrumgztrack.OJTOptionDialog;
import com.example.scrumgztrack.Person;
import com.example.scrumgztrack.R;
import com.example.scrumgztrack.TeamListDialog;

public class OJTlistFragment extends Fragment {

	private ListView ojtListview;
	private List<Person> ojtList;
	private OJTlistAdapter ojtAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_ojtlist, container,
				false);
		ojtListview = (ListView) rootView.findViewById(R.id.ojtlist_listview);

		if (isConnectingToInternet()) {
			
			new RetrieveAllOJT(getActivity()).execute();
			
		} else {
			
			Toast.makeText(getActivity(),
					"Please make sure you are connected to the internet.",
					Toast.LENGTH_SHORT).show();
			ojtList = new ArrayList();
			
		}
		
		ojtAdapter = new OJTlistAdapter(getActivity(), ojtList);
		ojtListview.setAdapter(ojtAdapter);

		ojtListview
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int position, long arg3) {
						Intent i = new Intent(getActivity(),
								OJTOptionDialog.class);
						startActivity(i);
					}
				});

		return rootView;
	}

	public boolean isConnectingToInternet() {
		ConnectivityManager connectivity = (ConnectivityManager) getActivity()
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null)
				for (int i = 0; i < info.length; i++)
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}

		}
		return false;
	}

	private class RetrieveAllOJT extends AsyncTask<String, Void, Boolean> {

		Context context;
		ProgressDialog progressD;

		public RetrieveAllOJT(Context context) {
			this.context = context;

		}

		@Override
		protected void onPreExecute() {
			progressD = new ProgressDialog(context);
			progressD.setMessage("Retrieving list of OJTs...");
			progressD.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progressD.setCanceledOnTouchOutside(false);
			progressD.show();
			ojtList = new ArrayList();
		}

		@Override
		protected Boolean doInBackground(String... params) {
			boolean flag = true;
			
			try {
				
				String urlTopTracks = "http://gz123.site90.net/get_trainee";
				HttpClient client = new DefaultHttpClient();
				ResponseHandler<String> handler = new BasicResponseHandler();

				HttpPost request = new HttpPost(urlTopTracks);

				String httpResponseTopTracks = client.execute(request, handler);

				StringTokenizer token = new StringTokenizer(
						httpResponseTopTracks, "<");
				String retrieveResult = token.nextToken();
				JSONArray resultJSON = new JSONArray(retrieveResult);
				JSONObject tempObj;

				for (int init = 0; init < resultJSON.length(); init++) {
					tempObj = resultJSON.getJSONObject(init);
					ojtList.add(new Person(tempObj.getString("first_name"),
							tempObj.getString("last_name"), tempObj
									.getString("email")));
				}

				Log.d("LIST OF OJTS", retrieveResult);

			} catch (Exception e) {
				flag = false;
				e.printStackTrace();
			}

			return flag;
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			if (progressD.isShowing()) {
				progressD.dismiss();
			}

			ojtAdapter = new OJTlistAdapter(getActivity(), ojtList);
			ojtListview.setAdapter(ojtAdapter);
		}
	}
}
