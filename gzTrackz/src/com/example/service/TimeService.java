package com.example.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class TimeService extends Service {

	@Override
	public IBinder onBind(Intent arg0) {
		
		return null;
	}
	
	public class TimeBinder extends Binder{
		public TimeService getService(){
			return TimeService.this;
		}
	}
	
}
