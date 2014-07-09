package com.example.gztrackz;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.EditText;

public class RegisterActivity extends Activity {

	private EditText emailTxt, firstNameTxt, lastNameTxt, passwordTxt, confirmPasswordTxt;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		
		emailTxt = (EditText) findViewById(R.id.emailRegisterEditText);
		passwordTxt = (EditText) findViewById(R.id.passwordRegisterEditText);
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register, menu);
		return true;
	}

}
