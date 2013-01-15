package com.egmono.welltrak.activities;

import com.egmono.welltrak.BuildConfig;
import com.egmono.welltrak.R;
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

public class DailyActivity extends Activity
{

	DatePicker datePicker;
	EditText dateText;
	EditText totalText;
	EditText flowText;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
	{	
		final String tag = getString(R.string.app_name);
		
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dailymain);
		
		try {
			VisitVO visit = new VisitVO();
			
			datePicker = (DatePicker)findViewById(R.id.dailyDate01);
			dateText = (EditText)findViewById(R.id.dailyText01);
			totalText = (EditText)findViewById(R.id.dailyText02);
			flowText = (EditText)findViewById(R.id.dailyText03);
			
			visit.setDate(new Date(datePicker.getYear()-1900,
					datePicker.getMonth(),
					datePicker.getDayOfMonth()));
			dateText.setText(visit.getDateText());
			

			
			}
		catch(Exception e) {
			Log.d(tag, "Error", e);
		} 
    }
	
	// Initiating Menu XML file (dailymenu.xml)
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.dailymenu, menu);
        return true;
    }

    /**
     * Event Handling for Individual menu item selected
     * Identify single menu item by it's id
     * */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
		Toast.makeText(DailyActivity.this, item.getTitle().toString(), Toast.LENGTH_SHORT).show();
        switch (item.getItemId())
        {
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
