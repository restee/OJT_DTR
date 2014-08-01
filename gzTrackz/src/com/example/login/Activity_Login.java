package com.example.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gztrackz.R;
import com.example.gztrackz.RegisterActivity;
import com.example.gztrackz.TabsManager;
import com.example.utilities.FontAndToastUtils;
import com.example.utilities.SharedPrefUtil;

public class Activity_Login extends Activity {

	private TextView tv_register;
	private Button btn_login;
	private EditText et_email, et_password;
	private ClickListeners clickListener;
	private FontAndToastUtils utils;

	private Context context;
	private SharedPreferences prefs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// initialize variables
		init();

		prefs = this.getSharedPreferences(SharedPrefUtil.PREFERENCE_NAME,
				Context.MODE_PRIVATE);

		String firstName = prefs.getString(SharedPrefUtil.KEY_FNAME, null);

		if (firstName != null) {
			Intent i = new Intent(this, TabsManager.class);
			i.putExtra("email", prefs.getString(SharedPrefUtil.KEY_EMAIL, null));
			startActivityForResult(i, 1);
		}
		
		btn_login.setOnClickListener(clickListener);

		tv_register.setOnClickListener(clickListener);
	}

	private void init() {

		context = this;
		
		btn_login = (Button) findViewById(R.id.logInButton);
		et_email = (EditText) findViewById(R.id.usernameEditText);
		et_password = (EditText) findViewById(R.id.passwordEditText);
		tv_register = (TextView) findViewById(R.id.registerTextView);
		utils = new FontAndToastUtils(context);
		clickListener = new ClickListeners(context);
		
		//setting tags for click listeners
		btn_login.setTag(getResources().getString(R.string.loginactivity_login));
		tv_register.setTag(getResources().getString(R.string.loginactivity_register));
		
		tv_register.setTypeface(utils.getTtfFont("Walkway_SemiBold.ttf"));
	}
	
	public void userClickedLoginBtn() {
		
		String emailInput = et_email.getText().toString();
		String passInput = et_password.getText().toString();
		
		if (emailInput.length() > 0 && passInput.length() > 0) {

			if (isConnectingToInternet()) {

				new Task_Login(context, emailInput, passInput, prefs)
						.execute();
			} else {

				utils.promptUser("Please make sure internet connection exists!");
			}

		} else {
			
			utils.promptUser("Please fill all fields!");
		}
	}
	
	public void userClickedRegisterBtn() {
		
		Intent i = new Intent(context, RegisterActivity.class);
		startActivity(i);
		
	}

	public void performLoginBasedOnResult(Boolean login) {

		// login successful
		if (login) {
			Intent i = new Intent(context, TabsManager.class);
			i.putExtra("email", prefs.getString(SharedPrefUtil.KEY_EMAIL, null));

			startActivityForResult(i, 1);

		} else
			Toast.makeText(context, "Invalid login credentials!",
					Toast.LENGTH_LONG).show();

	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public boolean isConnectingToInternet() {
		ConnectivityManager connectivity = (ConnectivityManager) context
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_CANCELED)
			finish();
		else {
			et_email.setText(null);
			et_password.setText(null);
			et_email.requestFocus();
		}
	}

	/*
	 * private boolean isNetworkAvailable() { ConnectivityManager
	 * connectivityManager = (ConnectivityManager)
	 * getSystemService(Context.CONNECTIVITY_SERVICE); NetworkInfo
	 * activeNetworkInfo = connectivityManager .getActiveNetworkInfo(); return
	 * activeNetworkInfo != null && activeNetworkInfo.isConnected(); }
	 */
}
