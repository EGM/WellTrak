package com.egmono.welltrak.activity;

import com.egmono.util.Test; 
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
import android.widget.DatePicker;
import android.widget.DatePicker.*;
import android.widget.EditText;
import android.widget.Toast;
import android.database.sqlite.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DailyActivity extends Activity
{
	private static final String TAG = "DailyActivity";

	private DatePicker datePicker;
	private EditText editDate;
	private EditText editTotal1;
	private EditText editTotal2;
	private EditText editFlow;
	private EditText editCl2Entry;
	private EditText editCl2Remote;
	private EditText editPhEntry;
	private EditText editPhRemote;
	
	private Calendar calendar;
	
	private VisitModel visitModel;
	private VisitDao visitDao;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daily_main);
		
		datePicker = (DatePicker)findViewById(R.id.daily_datepicker);
		editDate = (EditText)findViewById(R.id.edit_date);
		editTotal1 = (EditText)findViewById(R.id.edit_meter1);
		editTotal2 = (EditText)findViewById(R.id.edit_meter2);
		editFlow = (EditText)findViewById(R.id.edit_flow);
		editCl2Entry = (EditText)findViewById(R.id.edit_cl2entry);
		editCl2Remote = (EditText)findViewById(R.id.edit_cl2remote);
		editPhEntry = (EditText)findViewById(R.id.edit_phentry);
		editPhRemote = (EditText)findViewById(R.id.edit_phremote);
		
		visitModel = new VisitModel();
		visitDao = new VisitDao();
		
		datePicker.init(datePicker.getYear(), 
				datePicker.getMonth(), 
				datePicker.getDayOfMonth(), 
				dateSetListener);
				
/* Testing...
		datePicker.updateDate(2012,7,31);
		Test.a(datePicker.getYear(), 2012, "2012");
		Test.a(datePicker.getMonth(), 7, "8 (August)");
		Test.a(datePicker.getDayOfMonth(), 31, "31st");
		
		Test.a(visitModel.pump1Total.getValue(), 4017600.0f, "T1 value");
		Test.a(visitModel.pump1Total.isNull, false, "T1 isNull");
		Test.a(visitModel.pump1Total.toString(), "4017600.0", "T1 toString");

		Test.a(visitModel.pump2Total.getValue(), 0.0f, "T2 value");
		Test.a(visitModel.pump2Total.isNull, true, "T2 isNull");
		Test.a(visitModel.pump2Total.toString(), "", "T2 toString");
		
		Test.a(visitModel.flow.getValue(), 440.0f, "Flow value");
		Test.a(visitModel.flow.isNull, false, "Flow isNull");
		Test.a(visitModel.flow.toString(), "440.0", "Flow toString");

		Test.a(visitModel.cl2Entry.getValue(), 2.0f, "Cl2E value");
		Test.a(visitModel.cl2Entry.isNull, false, "Cl2E isNull");
		Test.a(visitModel.cl2Entry.toString(), "2.0", "Cl2E toString");
		
		Test.a(visitModel.cl2Remote.getValue(), 1.5f, "Cl2R value");
		Test.a(visitModel.cl2Remote.isNull, false, "Cl2R isNull");
		Test.a(visitModel.cl2Remote.toString(), "1.5", "Cl2R toString");

		Test.a(visitModel.phEntry.getValue(), 0.0f, "pHE value");
		Test.a(visitModel.phEntry.isNull, true, "pHE isNull");
		Test.a(visitModel.phEntry.toString(), "", "pHE toString");
		
		Test.a(visitModel.phRemote.getValue(), 0.0f, "pHR value");
		Test.a(visitModel.phRemote.isNull, true, "pHR isNull");
		Test.a(visitModel.phRemote.toString(), "", "pHR toString");
		
		Test.a(visitDao.getPreviousRecordDate(visitModel.getDate()),
			new Date(2012-1900,7,29), "get previous date"); // 08-29-2012
			
		Test.a(visitDao.getNextRecordDate(visitModel.getDate()),
			   new Date(2012-1900,8,5), "get next date"); // 09-05-2012
		
		Test.a(visitDao.save(visitModel).getId(), 10L, "update");
		visitModel.setId(0L);
		Test.a(visitDao.save(visitModel).getId(), 45L, "insert");
		
		Test.displayTestResults();
*/
	}
	

	private DatePicker.OnDateChangedListener dateSetListener = new DatePicker.OnDateChangedListener() 
	{
		@Override
		public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) 
		{
			visitModel = visitDao.getDay(new Date(year-1900, monthOfYear, dayOfMonth));
			updateDisplay();
		}
	};
	
	
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
				Date previousDate = visitDao.getPreviousRecordDate(
						visitModel.getDate());
				datePicker.updateDate(previousDate.getYear()+1900,
									  previousDate.getMonth(),
									  previousDate.getDate());
				return true;
				
			case R.id.menu_next:
				Date nextDate = visitDao.getNextRecordDate(
						visitModel.getDate());
				datePicker.updateDate(nextDate.getYear()+1900,
									  nextDate.getMonth(),
									  nextDate.getDate());
				return true;
				
			case R.id.menu_save:
				Toast.makeText(DailyActivity.this, 
						visitDao.save(visitModel)?
						"Saved.":"FAILED TO SAVE", 
						Toast.LENGTH_SHORT).show();
				return true;

			case R.id.menu_send:
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
		editDate.setText(sdf.format(visitModel.getDate()));
		editTotal1.setText(visitModel.pump1Total.toString());
		editTotal2.setText(visitModel.pump2Total.toString());
		editFlow.setText(visitModel.flow.toString());
		editCl2Entry.setText(visitModel.cl2Entry.toString());
		editCl2Remote.setText(visitModel.cl2Remote.toString());
		editPhEntry.setText(visitModel.phEntry.toString());
		editPhRemote.setText(visitModel.phRemote.toString());
	}
}
