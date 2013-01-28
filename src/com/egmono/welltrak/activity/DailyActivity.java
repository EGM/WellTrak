package com.egmono.welltrak.activity;

import com.egmono.util.Test; 
import com.egmono.welltrak.WellTrakApp;
import com.egmono.welltrak.R;
import com.egmono.welltrak.dao.DatabaseHelper;
import com.egmono.welltrak.dao.VisitDao;
import com.egmono.welltrak.model.VisitModel;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
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
	private static final int SAVE_ERROR_DIALOG = 25;
	private static final int DISCARD_DIALOG = 30;
	private static final int HELP_DIALOG = 60;

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
		datePicker.updateDate(datePicker.getYear(), 
							  datePicker.getMonth(), 
							  datePicker.getDayOfMonth());
				
	}
	
	/** Actions to perform when the user picks a new date. */
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
	
	/** Enable/disable action items */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) 
	{
		// Disable the discard menu function if it is a new visit.
		MenuItem item= menu.findItem(R.id.menu_discard);
		item.setEnabled(visitModel.getId()!=0L);
		super.onPrepareOptionsMenu(menu);
		return true;
	}

	/**
	 * Event Handling for Individual menu item selected.
	 * Identify single menu item by it's id.
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{

		switch (item.getItemId()) {
			// Navigate to next earlier dated visit stored in db
			case R.id.menu_previous:
				Date previousDate = visitDao.getPreviousRecordDate(
						visitModel.getDate());
				datePicker.updateDate(previousDate.getYear()+1900,
									  previousDate.getMonth(),
									  previousDate.getDate());
				return true;
				
			// Navigate to next later dated visit stored in db
			case R.id.menu_next:
				Date nextDate = visitDao.getNextRecordDate(
						visitModel.getDate());
				datePicker.updateDate(nextDate.getYear()+1900,
									  nextDate.getMonth(),
									  nextDate.getDate());
				return true;
				
			// Save displayed visit to database, 
			//   either creating new record or
			//   updating existing record.
			case R.id.menu_save:
				if(visitModel.pump1Total.isNull)
				{
					showDialog(SAVE_ERROR_DIALOG);
				}
				else
				{
					Toast.makeText(DailyActivity.this, 
						visitDao.save(visitModel)?
						"Saved.":"FAILED TO SAVE", 
						Toast.LENGTH_SHORT).show();
				}
				return true;

			// Send displayed visit as text to another
			//   app of the user's choosing
			case R.id.menu_send:
				Intent sendIntent = new Intent();
				sendIntent.setAction(Intent.ACTION_SEND);
				sendIntent.putExtra(Intent.EXTRA_TEXT, visitModel.toString());
				sendIntent.setType("text/plain");
				startActivity(sendIntent);
				return true;

			// Delete this visit from database
			case R.id.menu_discard:
				showDialog(DISCARD_DIALOG);
				return true;
				
			// About
			case R.id.menu_about:
				Toast.makeText(DailyActivity.this, 
							   "About is Selected", 
							   Toast.LENGTH_SHORT).show();
				return true;
				
			// Help
			case R.id.menu_help:
				showDialog(HELP_DIALOG);
				Toast.makeText(DailyActivity.this, 
							   "Help is Selected", 
							   Toast.LENGTH_SHORT).show();
				return true;
				
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	/** Builds dialogs to display. */
	@Override
	protected Dialog onCreateDialog(int id) 
	{
		switch (id) 
		{
			case SAVE_ERROR_DIALOG:
				Builder saveErrDlgBldr = new AlertDialog.Builder(this);
				saveErrDlgBldr.setMessage(R.string.dlg_save_empty_error);
				saveErrDlgBldr.setPositiveButton(R.string.dlg_ok, new saveErrDlgClickListener());
				AlertDialog saveErrDlg = saveErrDlgBldr.create();
				saveErrDlg.show();
			break;
			
			case DISCARD_DIALOG:
				Builder discardDlgBldr = new AlertDialog.Builder(this);
				discardDlgBldr.setIcon(R.drawable.ic_menu_discard);
				discardDlgBldr.setMessage(R.string.dlg_discard);
				discardDlgBldr.setCancelable(true);
				discardDlgBldr.setPositiveButton(R.string.dlg_ok, new discardDlgClickListener());
				discardDlgBldr.setNegativeButton(R.string.dlg_cancel, new discardDlgClickListener());
				AlertDialog discardDlg = discardDlgBldr.create();
				discardDlg.show();
				break;
				
			case HELP_DIALOG:
				Builder helpDlgBldr = new AlertDialog.Builder(this);
				helpDlgBldr.setMessage("This will end the activity");
				helpDlgBldr.setCancelable(true);
				helpDlgBldr.setPositiveButton("I agree", new OkOnClickListener());
				helpDlgBldr.setNegativeButton("No, no", new CancelOnClickListener());
				AlertDialog dialog = helpDlgBldr.create();
				dialog.show();
				break;
		}
		return super.onCreateDialog(id);
	}
	
	private final class saveErrDlgClickListener implements
    DialogInterface.OnClickListener 
	{
		public void onClick(DialogInterface dialog, int which) 
		{
			//stub
		}
	} 
	
	private final class discardDlgClickListener implements
    DialogInterface.OnClickListener 
	{
		public void onClick(DialogInterface dialog, int which) 
		{
			switch (which)
			{
				case DialogInterface.BUTTON_POSITIVE:
					Toast.makeText(DailyActivity.this, 
						visitDao.delete(visitModel)?
						"Discarded.":"FAILED TO DISCARD", 
						Toast.LENGTH_SHORT).show();
					visitModel = visitDao.getDay(visitModel.getDate());
					updateDisplay();	
			}
		}
	} 
	

	private final class CancelOnClickListener implements
	DialogInterface.OnClickListener 
	{
		public void onClick(DialogInterface dialog, int which) 
		{
			Toast.makeText(getApplicationContext(), which+" Activity will continue",
						   Toast.LENGTH_LONG).show();
						   
		}
	}

	private final class OkOnClickListener implements
    DialogInterface.OnClickListener 
	{
		public void onClick(DialogInterface dialog, int which) 
		{
			DailyActivity.this.finish();
		}
	} 
	
	/** 
	* Write the contents of the model to the edit text fields
	* and invalidate the menu so it will toggle the discard option.
	*/
	private void updateDisplay()
	{
		SimpleDateFormat sdf = new SimpleDateFormat("(EEE) MMM dd, yyyy");
		editDate.setText(sdf.format(visitModel.getDate()));
		editTotal1.setText(visitModel.pump1Total.toString());
		editTotal2.setText(visitModel.pump2Total.toString());
		editFlow.setText(visitModel.flow.toString(3));
		editCl2Entry.setText(visitModel.cl2Entry.toString());
		editCl2Remote.setText(visitModel.cl2Remote.toString());
		editPhEntry.setText(visitModel.phEntry.toString());
		editPhRemote.setText(visitModel.phRemote.toString());
		
		invalidateOptionsMenu();
	}
}
