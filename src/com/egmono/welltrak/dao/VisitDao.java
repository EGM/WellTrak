package com.egmono.welltrak.dao;

import com.egmono.welltrak.model.VisitModel;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.Date;
import java.text.*;

public class VisitDao 
{
	private static final String TAG = VisitDao.class.getSimpleName();
	
	/** Visit table name */
	protected final static String TABLE = "visits";

	/** Visit column names */
	protected static class columns
	{
		protected final static String _ID = "_id";
		protected final static String DATE = "date";
		protected final static String TOTAL1 = "total1";
		protected final static String TOTAL2 = "total2";
		protected final static String CL2ENT = "cl2ent";
		protected final static String CL2REM = "cl2rem";
		protected final static String PHENT = "phent";
		protected final static String PHREM = "phrem";
		
		protected static String[] all() 
		{
			return new String[]{_ID, DATE, TOTAL1, TOTAL2, CL2ENT,
				CL2REM, PHENT, PHREM};
		}
	}
	
	/** Visit selection */
	protected static class select
	{		
		protected static String previousOrCurrent()
		{
			return columns.DATE + " <= ?";
		}
		protected static String next()
		{
			return columns.DATE + " > ?";
		}
	}
			
	/** Return a Visit from the database for a particular date */
	public VisitModel getDay(Date date)
	{
	 	VisitModel visit = new VisitModel();
		SQLiteDatabase db = new DatabaseHelper().getReadableDatabase();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		//Always show current date.
		visit.setDate(date);
		
		try
		{
			//Retrieve the current date or previous date record.
			Cursor c = db.query(TABLE, columns.all(), 
					select.previousOrCurrent(), 
					new String[]{sdf.format(date)}, 
					null, null, columns.DATE+" DESC", "1"  );
					
			//Retrieve the record following date.
			Cursor n =  db.query(TABLE, columns.all(), 
					select.next(), 
					new String[]{sdf.format(date)}, 
					null, null, columns.DATE+" ASC", "1"  );
					
			if(c.moveToFirst())
			{				
				if(sdf.format(date).equals(c.getString(c.getColumnIndex(columns.DATE))))
				{
					//Returned current date so set id and total
					visit.setId(c.getInt(c.getColumnIndex(columns._ID)));
				//	visit.setTotal(c.getFloat(c.getColumnIndex(columns.TOTAL)));
					visit.pump1Total.setValue(c.getFloat(c.getColumnIndex(columns.TOTAL1)));
					visit.pump2Total.setValue(c.getFloat(c.getColumnIndex(columns.TOTAL2)));
					visit.cl2Entry.setValue(c.getFloat(c.getColumnIndex(columns.CL2ENT)));
					visit.cl2Remote.setValue(c.getFloat(c.getColumnIndex(columns.CL2REM)));
					visit.phEntry.setValue(c.getFloat(c.getColumnIndex(columns.PHENT)));
					visit.phRemote.setValue(c.getFloat(c.getColumnIndex(columns.PHREM)));
					
				}

				if(n.moveToFirst())
				{
					
					//Find the current or previous total
					Float totalPrevious = c.getFloat(
							c.getColumnIndex(columns.TOTAL1));
							
					//Find the next total
					Float totalNext = n.getFloat(
							n.getColumnIndex(columns.TOTAL1));
							
					//Calculate the difference in flow
					float totalResult = totalNext - totalPrevious;
					
					//Find the current or previous date
					Date datePrevious = sdf.parse(c.getString(
							c.getColumnIndex(columns.DATE)));
							
					//Find the next date
					Date dateNext = sdf.parse(n.getString(
							n.getColumnIndex(columns.DATE)));
							
					//Calculate the difference in days
					float dateResult = (dateNext.getTime() - 
							datePrevious.getTime()) / 86400000;
							
					//Set flow to average daily flow
			//		visit.setFlow(totalResult/dateResult);
				}
			}
		}
		catch(Exception e)
		{
			Log.e(TAG, "(TEST) Error: "+e.getMessage(), e);
		}
		finally
		{
			return visit;
		}
	}
}
