package com.egmono.welltrak.dao;

import com.egmono.welltrak.model.VisitModel;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.Calendar;
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

					visit.pump1Total.isNull = c.isNull(c.getColumnIndex(columns.TOTAL1));					
					visit.pump1Total.setValue(c.getFloat(c.getColumnIndex(columns.TOTAL1)));
					
					visit.pump2Total.isNull = c.isNull(c.getColumnIndex(columns.TOTAL2));
					if(!visit.pump2Total.isNull)
						visit.pump2Total.setValue(c.getFloat(c.getColumnIndex(columns.TOTAL2)));
					
					visit.cl2Entry.isNull = c.isNull(c.getColumnIndex(columns.CL2ENT));
					if(!visit.cl2Entry.isNull)
						visit.cl2Entry.setValue(c.getFloat(c.getColumnIndex(columns.CL2ENT)));
				
					visit.cl2Remote.isNull = c.isNull(c.getColumnIndex(columns.CL2REM));
					if(!visit.cl2Remote.isNull)
						visit.cl2Remote.setValue(c.getFloat(c.getColumnIndex(columns.CL2REM)));

					visit.phEntry.isNull = c.isNull(c.getColumnIndex(columns.PHENT));
					if(!visit.phEntry.isNull)
						visit.phEntry.setValue(c.getFloat(c.getColumnIndex(columns.PHENT)));

					visit.phRemote.isNull = c.isNull(c.getColumnIndex(columns.PHREM));
					if(!visit.phRemote.isNull)
						visit.phRemote.setValue(c.getFloat(c.getColumnIndex(columns.PHREM)));
				}
				else
				{
					//Did not return current date so set fields to null
					visit.pump1Total.isNull = true;
					visit.pump2Total.isNull = true;
					visit.cl2Entry.isNull = true;
					visit.cl2Remote.isNull = true;
					visit.phEntry.isNull = true;
					visit.phRemote.isNull = true;
				}

				if(n.moveToFirst())
				{
					
					//Find the current or previous total
					Float totalPrevious1 = c.getFloat(
							c.getColumnIndex(columns.TOTAL1));
					Float totalPrevious2 = c.getFloat(
							c.getColumnIndex(columns.TOTAL2));
							
					//Find the next total
					Float totalNext1 = n.getFloat(
							n.getColumnIndex(columns.TOTAL1));
					Float totalNext2 = n.getFloat(
						n.getColumnIndex(columns.TOTAL2));
					
					//Calculate the difference in flow
					float totalResult1 = totalNext1 - totalPrevious1;
					float totalResult2 = totalNext2 - totalPrevious2;
					float totalResult = totalResult1 + totalResult2;
					
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
					visit.flow.setValue(totalResult/dateResult);
					visit.flow.isNull = false;
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
