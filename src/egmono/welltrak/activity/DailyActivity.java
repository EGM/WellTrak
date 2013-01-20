package egmono.welltrak.activity;

import egmono.util.Test;
import egmono.welltrak.BuildConfig;
import egmono.welltrak.R;
import egmono.welltrak.dao.VisitDao;
import egmono.welltrak.vo.VisitVo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DailyActivity extends Activity {

	private static final String TAG = DailyActivity.class.getSimpleName();

	DatePicker datePicker;
	EditText dateText;
	EditText totalText;
	EditText flowText;
	
	VisitVo visitVo = new VisitVo();
	VisitDao visitDao = new VisitDao();
		
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)	{	

		super.onCreate(savedInstanceState);
		setContentView(R.layout.dailymain);
				
		// Initializing UI objects.
		datePicker = (DatePicker)findViewById(R.id.dailyDate01);
		dateText = (EditText)findViewById(R.id.dailyText01);
		totalText = (EditText)findViewById(R.id.dailyText02);
		flowText = (EditText)findViewById(R.id.dailyText03);

		// Initialize date picker (listener)
		datePicker.init(datePicker.getYear(),
				datePicker.getMonth(),
				datePicker.getDayOfMonth(),
				new DatePicker.OnDateChangedListener()
				{
					@Override
					public void onDateChanged(DatePicker dp,
							int y, int m, int d)
					{
						visitVo = visitDao.getDay(new Date(y-1900,m,d));
						updateDisplay();							
					}
				});
			
		// Force "date changed"...
		datePicker.updateDate(datePicker.getYear(),
				datePicker.getMonth(),
				datePicker.getDayOfMonth());
	}
	
	private void updateDisplay()
	{
		dateText.setText(new SimpleDateFormat("(EEE) MMM dd, yyyy")
				.format(visitVo.getDate()));
		totalText.setText(String.format("%1$.1f", visitVo.getTotal()));
		flowText.setText(String.format("%1$.3f", visitVo.getFlow()));
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
				Toast.makeText(DailyActivity.this, 
						"Bookmark is Selected", 
						Toast.LENGTH_SHORT).show();
				return true;

			case R.id.menu_save:
				Toast.makeText(DailyActivity.this, 
						"Save is Selected", 
						Toast.LENGTH_SHORT).show();
				return true;

			case R.id.menu_search:
				Toast.makeText(DailyActivity.this, 
						"Search is Selected", 
						Toast.LENGTH_SHORT).show();
				return true;

			case R.id.menu_share:
				Toast.makeText(DailyActivity.this, 
						"Share is Selected", 
						Toast.LENGTH_SHORT).show();
				return true;

			case R.id.menu_delete:
				Toast.makeText(DailyActivity.this, 
						"Delete is Selected", 
						Toast.LENGTH_SHORT).show();
				return true;

			case R.id.menu_preferences:
				Toast.makeText(DailyActivity.this, 
						"Preferences is Selected", 
						Toast.LENGTH_SHORT).show();
				return true;

			default:
				return super.onOptionsItemSelected(item);
		}
	}
}
