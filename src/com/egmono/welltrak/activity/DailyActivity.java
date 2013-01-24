package com.egmono.welltrak.activity;

import com.egmono.welltrak.WellTrakApp;
import com.egmono.welltrak.R;
import com.egmono.welltrak.dao.DatabaseHelper;
import com.egmono.welltrak.dao.VisitDao;
import com.egmono.welltrak.model.VisitModel;

import android.app.Activity;
import android.content.Intent;
import android.database.*;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import android.database.sqlite.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DailyActivity extends Activity
{
	private static final String TAG = "DailyActivity";

	private EditText editDate;
	private EditText editTotal1;
	private EditText editTotal2;
	private EditText editFlow;
	private EditText editCl2Entry;
	private EditText editCl2Remote;
	private EditText editPhEntry;
	private EditText editPhRemote;
	
	private VisitModel visitModel;
	private VisitDao visitDao;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daily_main);
		
		editDate = (EditText)findViewById(R.id.edit_date);
		editTotal1 = (EditText)findViewById(R.id.edit_meter1);
		editTotal2 = (EditText)findViewById(R.id.edit_meter2);
		editCl2Entry = (EditText)findViewById(R.id.edit_cl2entry);
		editCl2Remote = (EditText)findViewById(R.id.edit_cl2remote);
		editPhEntry = (EditText)findViewById(R.id.edit_phentry);
		editPhRemote = (EditText)findViewById(R.id.edit_phremote);
		
		visitModel = new VisitModel();
		visitDao = new VisitDao();
		
		//Test...
		Date testDate = new Date(2012-1900,7,31);
		visitModel = visitDao.getDay(testDate);
		updateDisplay();
	}
	
	/** Initiating Menu XML file (menu.xml) */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{

		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.menu, menu);
		return true;
	}

	/**
	 * Event Handling for Individual menu item selected.
	 * Identify single menu item by it's id.
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		/** TODO: Make menu DO stuff! :) */
		switch (item.getItemId()) {
			case R.id.menu_previous:
				Toast.makeText(DailyActivity.this, 
							   "Previous is Selected", 
							   Toast.LENGTH_SHORT).show();
				return true;
				
			case R.id.menu_next:
				Toast.makeText(DailyActivity.this, 
							   "Next is Selected", 
							   Toast.LENGTH_SHORT).show();
				return true;
				
			case R.id.menu_save:
				Toast.makeText(DailyActivity.this, 
							   "Save is Selected", 
							   Toast.LENGTH_SHORT).show();
				return true;

			case R.id.menu_send:
				Toast.makeText(DailyActivity.this, 
							   "Send is Selected", 
							   Toast.LENGTH_SHORT).show();
							   
				Intent sendIntent = new Intent();
				sendIntent.setAction(Intent.ACTION_SEND);
				sendIntent.putExtra(Intent.EXTRA_TEXT, visitModel.toString());
				sendIntent.setType("text/plain");
				startActivity(sendIntent);
				return true;

			case R.id.menu_discard:
				Toast.makeText(DailyActivity.this, 
							   "Discard is Selected", 
							   Toast.LENGTH_SHORT).show();
				return true;
				
			case R.id.menu_about:
				Toast.makeText(DailyActivity.this, 
							   "About is Selected", 
							   Toast.LENGTH_SHORT).show();
				return true;
				
			case R.id.menu_help:
				Toast.makeText(DailyActivity.this, 
							   "Help is Selected", 
							   Toast.LENGTH_SHORT).show();
				return true;
				
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	private void updateDisplay()
	{
		SimpleDateFormat sdf = new SimpleDateFormat("(EEE) MMM dd, yyyy");
		try
		{
			editDate.setText(sdf.format(visitModel.getDate()));
			editTotal1.setText(visitModel.pump1Total.toString());
			editTotal2.setText(visitModel.pump2Total.toString());
		//	editFlow.setText(visitModel.flow.toString());
			editCl2Entry.setText(visitModel.cl2Entry.toString());
			editCl2Remote.setText(visitModel.cl2Remote.toString());
			editPhEntry.setText(visitModel.phEntry.toString());
			editPhRemote.setText(visitModel.phRemote.toString());
		}
		catch(Exception e)
		{
			Log.e(TAG, "Error: "+e.getMessage(),e);
		}
	}
	
}
