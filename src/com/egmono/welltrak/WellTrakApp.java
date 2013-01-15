package com.egmono.welltrak;

import android.app.Application;
import android.content.Context;
import android.util.Log;

public class WellTrakApp extends Application {
	
	private static final String TAG = WellTrakApp.class.getSimpleName();
	private static WellTrakApp instance;
	
	
	@Override
	public void onCreate() {
		super.onCreate();
		Log.i(TAG, "WellTrakApp.onCreate was called");
		instance = this;
	}
	
	public static Context getContext() {
		return instance.getApplicationContext();
	}
}
