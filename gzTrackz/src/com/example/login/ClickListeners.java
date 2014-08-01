package com.example.login;

import android.content.Context;
import android.view.View;

import com.example.gztrackz.R;

public class ClickListeners implements View.OnClickListener {
	
	Context context;
	
	public ClickListeners(Context context) {
		
		this.context = context;
	}

	@Override
	public void onClick(View clickedView) {
		
		Activity_Login loginActivity = (Activity_Login) context;
		
		String tag = (String) clickedView.getTag();
		
		String userClickedRegister = context.getResources().getString(R.string.loginactivity_register);
		String userClickedLogin = context.getResources().getString(R.string.loginactivity_login);
		
		if(tag.equals(userClickedLogin)) {
			
			loginActivity.userClickedLoginBtn();
			
		} else if (tag.equals(userClickedRegister)) {
			
			loginActivity.userClickedRegisterBtn();
		}
		
	}

}
