package com.egmono.welltrak.activity;

import com.egmono.welltrak.R;
import com.egmono.welltrak.dao.VisitDao;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.text.*;

public class MonthlyListActivity extends ListActivity
{
	private final String TAG = "MonthlyListActivity"; 
	private GregorianCalendar calendar = new GregorianCalendar();;
	private Cursor cursor;
	private ListAdapter adapter;
	int listYear;
	int listMonth;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.monthly_main);
		
		// Use year from intent extras, or default to current year.
		calendar.set(Calendar.YEAR, 
				getIntent().getIntExtra("VIEW_YEAR", 
				Calendar.getInstance().get(Calendar.YEAR)));
				
		// Use month from intent extras, or default to current month.
		calendar.set(Calendar.MONTH,
				getIntent().getIntExtra("VIEW_MONTH",
				Calendar.getInstance().get(Calendar.MONTH)));
				
		calendar.getTime();
		
		Log.d(TAG, "Year = "+calendar.get(Calendar.YEAR));
		Log.d(TAG, "Month = "+calendar.get(Calendar.MONTH));
		Log.d(TAG, "DayOfMonth = "+calendar.get(Calendar.DAY_OF_MONTH));
		
		VisitDao visitDao = new VisitDao();
		
		cursor = visitDao.getMonth(calendar.get(Calendar.YEAR),
				calendar.get(Calendar.MONTH));
				
		adapter = new SimpleCursorAdapter(
			this, 
			R.layout.monthly_list_item, 
			cursor, 
			new String[] {"date", "total1", "total2", "cl2ent", "cl2rem", "phent", "phrem"}, 
			new int[] {	R.id.textDate, 
						R.id.textPump1, 	R.id.textPump2, 
						R.id.textCl2Entry,	R.id.textCl2Remote,
						R.id.textPhEntry, 	R.id.textPhRemote });
		setListAdapter(adapter);
		
	}
	
	/** Initiating Menu XML file (menu.xml) */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.menu, menu);
		return true;
	}
	/** Enable/disable menu action items */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) 
	{
		// Disable Previous action.
		menu.findItem(R.id.menu_previous).setEnabled(false);
		
		// Disable Next action.
		menu.findItem(R.id.menu_next).setEnabled(false);
		
		// Disable Save action.
		menu.findItem(R.id.menu_save).setEnabled(false);
		
		// Disable Discard action.
		menu.findItem(R.id.menu_discard).setEnabled(false);
		
		// Disable Show List action.
		menu.findItem(R.id.menu_list).setEnabled(false);

		// Disable Help action.
		menu.findItem(R.id.menu_help).setEnabled(false);

		super.onPrepareOptionsMenu(menu); 
		return true;
	}
	
	public void onListItemClick(ListView parent, View view, int position, long id) 
	{
    	Intent intent = new Intent(this, DailyActivity.class);
    	Cursor cursor = (Cursor) adapter.getItem(position);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = cursor.getString(cursor.getColumnIndex("date"));
		try
		{
			calendar.setTime(sdf.parse(dateString));
		}
		catch(ParseException e)
		{
			Log.e(TAG,"Error parsing date "+dateString, e);
		}
		
		intent.putExtra("VIEW_YEAR", calendar.get(Calendar.YEAR));
		intent.putExtra("VIEW_MONTH", calendar.get(Calendar.MONTH));
		intent.putExtra("VIEW_DAY_OF_MONTH", calendar.get(Calendar.DAY_OF_MONTH));
		
    	startActivity(intent);
    }
	
}
