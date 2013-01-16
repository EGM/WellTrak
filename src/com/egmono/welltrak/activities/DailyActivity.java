package com.egmono.welltrak.activities;

import com.egmono.welltrak.BuildConfig;
import com.egmono.welltrak.R;
import com.egmono.welltrak.daos.VisitDAO;
import com.egmono.welltrak.vos.VisitVO;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

public class DailyActivity extends Activity {

	private static final String TAG = DailyActivity.class.getSimpleName();

	DatePicker datePicker;
	EditText dateText;
	EditText totalText;
	EditText flowText;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)	{	

		super.onCreate(savedInstanceState);
		setContentView(R.layout.dailymain);
		
		try {
			VisitVO visit = new VisitVO();
			
			// Initializing UI objects.
			datePicker = (DatePicker)findViewById(R.id.dailyDate01);
			dateText = (EditText)findViewById(R.id.dailyText01);
			totalText = (EditText)findViewById(R.id.dailyText02);
			flowText = (EditText)findViewById(R.id.dailyText03);
			
			/*
			* TODO:
			* Setup an onDateChangedListener for datePicker to
			*   fetch a record from the database that corresponds
			*   to the new date, update 'visit', and reflect that
			*   update to the interface.
			*/
			
			// Testing stuff...
//			visit.setDate(new Date(datePicker.getYear()-1900,
//					datePicker.getMonth(),
//					datePicker.getDayOfMonth()));
//			dateText.setText(visit.getDateText());

			Log.i(TAG, "(?)Instantiating new VisitDAO");
			VisitDAO visitDao = new VisitDAO();
			
			Log.i(TAG, "(?)Fetching visit from database.");
			visit = visitDao.getDay(2012,12,28);
			
			Log.i(TAG, "(?)Updating display.");
			dateText.setText(visit.getDateText());
			totalText.setText(visit.getFlowMeterText());
			flowText.setText(visit.getFlowAverageDailyText());
			
		}
		catch(Exception e) {
			Log.d(TAG, "(?)Error: "+e.getMessage(), e);
		} 
	}
	
	//TODO: Consider changing 'Menu' to 'ActionBar'
	/** Initiating Menu XML file (dailymenu.xml) */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.dailymenu, menu);
		return true;
	}

	/**
	* Event Handling for Individual menu item selected.
	* Identify single menu item by it's id.
	*/
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	
		// Display menu item title (was testing something...)
		/* Toast.makeText(DailyActivity.this, item.getTitle().toString(), 
						Toast.LENGTH_SHORT).show();
		*/
		
		/** TODO: Make menu DO stuff! :) */
		switch (item.getItemId()) {
		
			case R.id.menu_compass:
				// Single menu item is selected do something
				// Ex: launching new activity/screen or show alert message
				Toast.makeText(DailyActivity.this, "Bookmark is Selected", Toast.LENGTH_SHORT).show();
				return true;

			case R.id.menu_save:
				Toast.makeText(DailyActivity.this, "Save is Selected", Toast.LENGTH_SHORT).show();
				return true;

			case R.id.menu_search:
				Toast.makeText(DailyActivity.this, "Search is Selected", Toast.LENGTH_SHORT).show();
				return true;

			case R.id.menu_share:
				Toast.makeText(DailyActivity.this, "Share is Selected", Toast.LENGTH_SHORT).show();
				return true;

			case R.id.menu_delete:
				Toast.makeText(DailyActivity.this, "Delete is Selected", Toast.LENGTH_SHORT).show();
				return true;

			case R.id.menu_preferences:
				Toast.makeText(DailyActivity.this, "Preferences is Selected", Toast.LENGTH_SHORT).show();
				return true;

			default:
				return super.onOptionsItemSelected(item);
		}
	}
}
