package com.example.tabs;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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

import com.example.scrumgztrack.CreateTeamDialog;
import com.example.scrumgztrack.OJTOptionDialog;
import com.example.scrumgztrack.Person;
import com.example.scrumgztrack.R;
import com.example.scrumgztrack.TeamListAddDialog;
import com.example.scrumgztrack.TeamListDialog;

public class OJTlistFragment extends Fragment {

	private ListView ojtListview;
	private List<Person> ojtList;
	private OJTlistAdapter ojtAdapter;
	public static final String CREATE_TEAM_BROADCAST = "com.example.gzscrumtrack.createTeamBroadcast";
	public static final String UPDATE_LIST_BROADCAST = "com.example.gzscrumtrack.updateListBroadcast";
	private String email;
	
	
	private BroadcastReceiver createTeamBroadcast = new BroadcastReceiver(){

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			Intent i = new Intent(getActivity(),CreateTeamDialog.class);
			i.putExtra("email",arg1.getStringExtra("email"));
			startActivityForResult(i,1);
		}		
	};	
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode==1){
			if(resultCode==getActivity().RESULT_OK){
				new AddTeam(getActivity(),email,data.getStringExtra("teamName")).execute();
			}
		}
	};
	
	private BroadcastReceiver editListBroadcast = new BroadcastReceiver(){
		@Override
		public void onReceive(Context arg0, Intent arg1) {
			ojtList.get(arg1.getIntExtra("position",0)).setTeamName(arg1.getStringExtra("teamName"));	
			ojtAdapter = new OJTlistAdapter(getActivity(), ojtList,email);
			ojtListview.setAdapter(ojtAdapter);
		}		
	};	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_ojtlist, container,
				false);
		ojtListview = (ListView) rootView.findViewById(R.id.ojtlist_listview);
		email = "dracula@gz.com";
		
		if (isConnectingToInternet()) {
			
			new RetrieveAllOJT(getActivity()).execute();
			
		} else {
			
			Toast.makeText(getActivity(),
					"Please make sure you are connected to the internet.",
					Toast.LENGTH_SHORT).show();
			ojtList = new ArrayList();			
		}
		
		ojtAdapter = new OJTlistAdapter(getActivity(), ojtList,email);
		ojtListview.setAdapter(ojtAdapter);

		ojtListview.setOnItemClickListener(null);
		
		getActivity().registerReceiver(createTeamBroadcast, new IntentFilter(CREATE_TEAM_BROADCAST));
		getActivity().registerReceiver(editListBroadcast, new IntentFilter(UPDATE_LIST_BROADCAST));
		return rootView;				
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		getActivity().unregisterReceiver(createTeamBroadcast);
		getActivity().unregisterReceiver(editListBroadcast);
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
				
				String urlTopTracks = "http://gz123.site90.net/list_trainees/";
				HttpClient client = new DefaultHttpClient();
				ResponseHandler<String> handler = new BasicResponseHandler();

				HttpPost request = new HttpPost(urlTopTracks);

				String httpResponseTopTracks = client.execute(request, handler);
				Log.d("RESULT", httpResponseTopTracks);
				StringTokenizer token = new StringTokenizer(
						httpResponseTopTracks, "<");
				String retrieveResult = token.nextToken();
				JSONArray resultJSON = new JSONArray(retrieveResult);
				JSONObject tempObj;

				for (int init = 0; init < resultJSON.length(); init++) {
					tempObj = resultJSON.getJSONObject(init);
					ojtList.add(new Person(tempObj.getString("first_name"),
							tempObj.getString("last_name"), tempObj
									.getString("email"), tempObj.getString("team")));
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
			ojtAdapter = new OJTlistAdapter(getActivity(), ojtList,email);
			ojtListview.setAdapter(ojtAdapter);
		}
	}
	

	private class AddTeam extends AsyncTask<String, Void, Boolean> {

		Context context;
		String email,teamName;
		ProgressDialog progressD;

		public AddTeam(Context context,String email,String teamName) {
			this.context = context;
			this.email=email;
			this.teamName = teamName;
		}

		@Override
		protected void onPreExecute() {
			progressD = new ProgressDialog(context);
			progressD.setMessage("Creating team...");
			progressD.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progressD.setCanceledOnTouchOutside(false);
			progressD.show();
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			if (progressD.isShowing()) {
				progressD.dismiss();
			}		
		}
		
		
		@Override
		protected Boolean doInBackground(String... params) {
			boolean flag = true;
			
			try {								
				String urlTopTracks = "http://gz123.site90.net/add_team/default.php";
				HttpClient client = new DefaultHttpClient();
				ResponseHandler<String> handler = new BasicResponseHandler();
				Log.d("email", email);
				Log.d("teamName", teamName);
				
				HttpPost request = new HttpPost(urlTopTracks);
				 List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
				    nameValuePairs.add(new BasicNameValuePair("email",email));
				    nameValuePairs.add(new BasicNameValuePair("team",teamName));
				    request.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				    				    
				String httpResponseTopTracks = client.execute(request, handler);
				Log.d("RESULT", httpResponseTopTracks);
				StringTokenizer token = new StringTokenizer(
						httpResponseTopTracks, "<");
				String retrieveResult = token.nextToken();
	
			} catch (Exception e) {
				flag = false;
				e.printStackTrace();
			}

			return flag;
		}
		
		
	}
}
