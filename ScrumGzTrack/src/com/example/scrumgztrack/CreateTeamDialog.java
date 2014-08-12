package com.example.scrumgztrack;

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

import com.example.adapter.OJTlistAdapter;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreateTeamDialog extends Activity {

	private Button create;
	private EditText teamTXT;
	private String email;
	private Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_team_dialog);
		create= (Button) findViewById(R.id.btnCreateTeam);
		teamTXT = (EditText) findViewById(R.id.txtAddTeam);
		email = getIntent().getStringExtra("email");
		context = this;
		create.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View arg0) {		
				if(teamTXT.getText().toString().length()>0){
					Intent i =new  Intent();
					i.putExtra("teamName", teamTXT.getText().toString());
					setResult(RESULT_OK,i);
					finish();
				}
			}
		});		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.create_team_dialog, menu);
		return true;
	}
	
	
}
