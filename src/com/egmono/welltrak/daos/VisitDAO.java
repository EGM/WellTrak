package com.egmono.welltrak.daos;

import com.egmono.welltrak.WellTrakApp;
import com.egmono.welltrak.vos.VisitVO;

import android.database.Cursor;  
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import android.renderscript.*;

/**
* Visit data access object to perform the CRUD 
* operations for the visit value object (VisitVO).
*/
public class VisitDAO
{
	private static final String TAG = VisitDAO.class.getSimpleName();
	
	// Table name and field names.
	protected final static String TABLE = "visits";
	protected final static String _ID = "_id";
	protected final static String DATE = "date";
	protected final static String METER = "meter";
	protected final static String FLOW = "flow";
	
	protected SQLiteDatabase db;
	
	/** Default constructor. */
	public VisitDAO()
	{
		Log.i(TAG, "(?)Empty constructor");
	}
	
	/** Retrieve single daily visit. */
	public VisitVO getDay(int year, int month, int day)
	{
		//TODO: finish this stub!
		String[] args = new String[]{ 
				String.format("%4d-%2d-%2d", year, month, day) };
				
		Log.i(TAG, "(?)Retrieving visit for date '"+args[0]+"'");
		
		Cursor c;
		try{
			Log.i(TAG, "(?)Opening database.");
			db = new DatabaseHelper().getWritableDatabase();
			Log.i(TAG, "(?)Opened database for reading.");
			c = db.rawQuery(whereClause(), args);
		}
		catch(Exception e){
			Log.e(TAG, "(?)Error: "+e.getMessage(), e);
			return new VisitVO();
		}
		
		Log.i(TAG, String.format("(?)Retrieved %d record(s).", 
				new Integer[]{c.getCount()}));
		
		if(c.moveToFirst())
		{
			Log.i(TAG, "(?)Setting visit to retrieved record.");
			
			VisitVO v = new VisitVO();
			
			v.setId(c.getInt(c.getColumnIndex(_ID)));
			Log.i(TAG, String.format("(?)visit._id = %d", new Integer[]{v.getId()}));
			
			try
			{
				v.setDate(formatToDate(c.getString(c.getColumnIndex(DATE))));
			}
			catch(ParseException pe)
			{ 
				Log.d(TAG, "(?)Error parsing date string.", pe); 
			}
			Log.i(TAG, "(?)visit.date = "+v.getDateText());
			
			//TODO: since this only retrieves STORED flows, need a method to calculate them...
			v.setFlowMeter(c.getFloat(c.getColumnIndex(METER)));
			Log.i(TAG, "(?)visit.meter = "+v.getFlowMeterText());
			
			if(c.isNull(c.getColumnIndex(FLOW))) {
				Log.i(TAG, "(?)visit.flow is NULL");
			}
			else {
				Log.i(TAG, "(?)visit.flow is not NULL");
			}
			v.setFlowAverageDaily(c.getFloat(c.getColumnIndex(FLOW)));
			Log.i(TAG, "(?)visit.flow = "+v.getFlowAverageDailyText());
			
			return v;
		}
		return new VisitVO();
	}
	
	/** Retrieve an array list of visits for entire month. */
	public ArrayList<VisitVO> getMonth(int year, int month)
	{
		//TODO: finish this stub!
		ArrayList<VisitVO> list = new ArrayList<VisitVO>();
		return list;
	}
		
	private Date formatToDate(String dateString) throws ParseException
	{
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-DD"); 
		return f.parse(dateString);
	}
	
	private String whereClause()
	{
		return new StringBuilder()
				.append("SELECT ").append(_ID)
				.append(",").append(DATE)
				.append(",").append(METER)
				.append(",").append(FLOW)
				.append(" FROM ").append(TABLE)
				.append(" WHERE ").append(DATE)
				.append(" = ?").toString();
	}
	
	private String whereClauseNext()
	{
		return new StringBuilder()
			.append("SELECT MIN(").append(_ID).append(")")
			.append(",").append(DATE)
			.append(",").append(METER)
			.append(",").append(FLOW)
			.append(" FROM ").append(TABLE)
			.append(" WHERE ").append(DATE)
			.append(" > ?").toString();
	}
	
	private String whereClausePrevious()
	{
		return new StringBuilder()
			.append("SELECT MAX(").append(_ID).append(")")
			.append(",").append(DATE)
			.append(",").append(METER)
			.append(",").append(FLOW)
			.append(" FROM ").append(TABLE)
			.append(" WHERE ").append(DATE)
			.append(" < ?").toString();
	}
}
