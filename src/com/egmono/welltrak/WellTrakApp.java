package com.egmono.welltrak;

import android.app.Application;
import android.content.Context;
import android.util.Log;

/**
* Global application methods and variables.
*/
public class WellTrakApp extends Application {
	
	private static final String TAG = WellTrakApp.class.getSimpleName();
	private static WellTrakApp instance;
	
	/** Called when the application is first created. */
	@Override
	public void onCreate() {
		super.onCreate();
		Log.i(TAG, "WellTrakApp.onCreate was called");
		instance = this;		
	}
	
	/** 
	* Provides a simple mechanism to retrieve application context. 
	*/
	public static Context getContext() {
		return instance.getApplicationContext();
	}
	
	/**
	* Provides a "shortcut" to resource strings.
	*/
	public static String getString(int stringId) {
		return instance.getApplicationContext().getString(stringId);
	}
}
