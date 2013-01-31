package com.egmono.welltrak.dao;

import com.egmono.util.Test;

import com.egmono.welltrak.model.Measurable;
import com.egmono.welltrak.model.VisitModel;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import java.util.Calendar;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import android.content.*;
import java.util.*;

public class VisitDao 
{
	private static final String TAG = VisitDao.class.getSimpleName();
	private GregorianCalendar calendar = new GregorianCalendar();
	
	/** Visit table name */
	protected final static String TABLE = "visits";

	/** Visit column names */
	protected static class colNames
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
	/** Visit column numbers */
	protected static class colNums
	{
		protected final static int _ID    = 0;
		protected final static int DATE   = 1;
		protected final static int TOTAL1 = 2;
		protected final static int TOTAL2 = 3;
		protected final static int CL2ENT = 4;
		protected final static int CL2REM = 5;
		protected final static int PHENT  = 6;
		protected final static int PHREM  = 7;
	}
	
	/** Visit selection */
	private static class select
	{		
		private final static String _id = colNames._ID + " = ?";
		private final static String previousDate = colNames.DATE + " < ?";
		private final static String previousOrCurrentDate = colNames.DATE + " <= ?";
		private final static String nextDate = colNames.DATE + " > ?";
		private final static String betweenDates = colNames.DATE 
				+ " => ? AND " + colNames.DATE + " <= ?";
	}
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	public Date getNextRecordDate(Date date)
	{
		SQLiteDatabase db = new DatabaseHelper().getReadableDatabase();
		Cursor nextRecord = getNextRecord(db, date);
		Date nextDate = new Date();
		if(nextRecord.moveToFirst())
			nextDate = getParsedDate(nextRecord);
		db.close();
		return nextDate;
	}
	
	public Date getPreviousRecordDate(Date date)
	{
		SQLiteDatabase db = new DatabaseHelper().getReadableDatabase();
		Cursor previousRecord = getPreviousRecord(db, date);
		Date previousDate = new Date();
		if(previousRecord.moveToFirst())
			previousDate = getParsedDate(previousRecord);
		db.close();
		return previousDate;
	}
	
	public boolean delete(VisitModel visit)
	{
		SQLiteDatabase db = new DatabaseHelper().getWritableDatabase();
		int i = db.delete(TABLE, select._id, 
				new String[]{Long.toString(visit.getId())});
		if(i==1) // successfully deleted record
		{
			Log.d(TAG, "Deleted record "+Long.toString(visit.getId()));
			db.close();
			visit = new VisitModel(visit.getDate());
			return true;
		}
		else
		{				
			Log.d(TAG, "Failed to delete record."+Long.toString(visit.getId()));
			db.close();
			return false;
		}
	}
	
	public boolean save(VisitModel visit)
	{
		SQLiteDatabase db = new DatabaseHelper().getWritableDatabase();
		ContentValues values = new ContentValues();
		
		values.put(colNames.DATE, sdf.format(visit.getDate()));		
		putValue(values, visit.pump1Total, colNames.TOTAL1);
		putValue(values, visit.pump2Total, colNames.TOTAL2);
		putValue(values, visit.cl2Entry,   colNames.CL2ENT);
		putValue(values, visit.cl2Remote,  colNames.CL2REM);
		putValue(values, visit.phEntry,    colNames.PHENT);
		putValue(values, visit.phRemote,   colNames.PHREM);
		
		if(visit.getId()==0) // is a new record
		{
			long new_id = db.insert(TABLE, colNames.DATE, values);
			if(new_id!=-1) // successfully inserted record
			{
				Log.d(TAG, "Inserted new record "+Long.toString(new_id));
				visit.setId(new_id);
				db.close();
				return true;
			}
			else 
			{
				Log.d(TAG, "Failed to insert new record.");
				db.close();
				return false;
			}
		}
		else // is not a new record
		{
			values.put(colNames._ID, visit.getId());
			int i = db.update(TABLE, values, select._id, 
				  		new String[]{Long.toString(visit.getId())});
			if(i==1) // successfully updated record
			{
				Log.d(TAG, "Updated record "+Long.toString(visit.getId()));
				db.close();
				return true;
			}
			else
			{				
				Log.d(TAG, "Failed to update record."+Long.toString(visit.getId()));
				db.close();
				return false;
			}
		}
	}
	
	/** Return a Visit from the database for a particular date */
	public VisitModel getDay(Date date)
	{
	 	VisitModel visit = new VisitModel();
		SQLiteDatabase db = new DatabaseHelper().getReadableDatabase();
		
		//Always show current date.
		visit.setDate(date);
		
		//Retrieve the current date or previous date record.
		Cursor currentRecord = getPreviousOrCurrentRecord(db, date);
					
		//Retrieve the record following date.
		Cursor nextRecord = getNextRecord(db, date);
					
		if(currentRecord.moveToFirst())
		{				
			if(sdf.format(date).equals(currentRecord.getString(currentRecord.getColumnIndex(colNames.DATE))))
			{
				//Returned current date so set id
				visit.setId(currentRecord.getInt(currentRecord.getColumnIndex(colNames._ID)));

				setValue(currentRecord, visit.pump1Total, colNames.TOTAL1);
				setValue(currentRecord, visit.pump2Total, colNames.TOTAL2);
				setValue(currentRecord, visit.cl2Entry,   colNames.CL2ENT);
				setValue(currentRecord, visit.cl2Remote,  colNames.CL2REM);
				setValue(currentRecord, visit.phEntry,    colNames.PHENT);
				setValue(currentRecord, visit.phRemote,   colNames.PHREM);
			}
			else
			{
				//Did not return current date so set fields to null
				visit.pump1Total.isNull(true);
				visit.pump2Total.isNull(true);
				visit.cl2Entry.isNull(true);
				visit.cl2Remote.isNull(true);
				visit.phEntry.isNull(true);
				visit.phRemote.isNull(true);
			}

			if(nextRecord.moveToFirst())
			{
					
				//Find the current or previous total
				Float totalPrevious1 = currentRecord.getFloat(
						currentRecord.getColumnIndex(colNames.TOTAL1));
				Float totalPrevious2 = currentRecord.getFloat(
						currentRecord.getColumnIndex(colNames.TOTAL2));
							
				//Find the next total
				Float totalNext1 = nextRecord.getFloat(
						nextRecord.getColumnIndex(colNames.TOTAL1));
				Float totalNext2 = nextRecord.getFloat(
						nextRecord.getColumnIndex(colNames.TOTAL2));
					
				//Calculate the difference in flow
				float totalResult1 = totalNext1 - totalPrevious1;
				float totalResult2 = totalNext2 - totalPrevious2;
				float totalResult = totalResult1 + totalResult2;
					
				Date datePrevious = getParsedDate(currentRecord);
				Date dateNext = getParsedDate(nextRecord);
					
				//Calculate the difference in days
				float dateResult = (dateNext.getTime() - 
						datePrevious.getTime()) / 86400000;
							
				//Set flow to average daily flow
				visit.flow.setValue(totalResult/dateResult);
				visit.flow.isNull(false);
			}
		}
		db.close();
		return visit;
	}
	
	public Cursor getMonth(int year, int month)
	{		
		SQLiteDatabase db = new DatabaseHelper().getReadableDatabase();
		Cursor c = null;
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month-1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.getTime();
		
		Log.d(TAG, "Year = "+calendar.get(Calendar.YEAR));
		Log.d(TAG, "Month = "+calendar.get(Calendar.MONTH));
		Log.d(TAG, "DayOfMonth = "+calendar.get(Calendar.DAY_OF_MONTH));
		
		int dayMax = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		Log.d(TAG, "MaximumDay = "+dayMax);
		
		try 
		{
			ContentValues cv = new ContentValues();
			db.execSQL("CREATE TEMP TABLE showmonth (" +
			VisitDao.colNames._ID    + " INTEGER PRIMARY KEY, " +
				VisitDao.colNames.DATE   + " TEXT, " +
				VisitDao.colNames.TOTAL1 + " REAL, " +
				VisitDao.colNames.TOTAL2 + " REAL, " +
				VisitDao.colNames.CL2ENT + " REAL, " +
				VisitDao.colNames.CL2REM + " REAL, " +
				VisitDao.colNames.PHENT  + " REAL, " +
				VisitDao.colNames.PHREM  + " REAL)"); 
			for(int i=1;i<=dayMax;i++) 
			{
				cv.put("date", String.format("%4d-%02d-%02d",year,month,i));
				db.insertOrThrow("showmonth","date",cv);
			}

			String sql = 
			"select v.*, s.date " +
			"from showmonth s " +
			"left join visits v on v.date = s.date";
			c=db.rawQuery(sql,null);
			
			if(c.moveToFirst()) 
			{
				int cnt = c.getCount();
				int colcnt = c.getColumnCount();
				Log.d(TAG, "Row Count = "+cnt+", Column Count = "+colcnt);
				for(int r=0;r<cnt;r++)
				{
					Log.d(TAG, new StringBuilder("#"+r)
						.append(", ").append(colNames._ID).append(": ")
						.append(c.getInt(colNums._ID))
						.append(", ").append(colNames.DATE).append(": ")
						.append(c.getString(colNums.DATE))
						.append(", ").append(colNames.TOTAL1).append(": ")
						.append(c.getFloat(colNums.TOTAL1))
						.append(", ").append(colNames.TOTAL2).append(": ")
						.append(c.getFloat(colNums.TOTAL2))
						.append(", ").append(colNames.CL2ENT).append(": ")
						.append(c.getFloat(colNums.CL2ENT))
						.append(", ").append(colNames.CL2REM).append(": ")
						.append(c.getFloat(colNums.CL2REM))
						.append(", ").append(colNames.PHENT).append(": ")
						.append(c.getFloat(colNums.PHENT))
						.append(", ").append(colNames.PHREM).append(": ")
						.append(c.getFloat(colNums.PHREM))
						
						.toString());
					c.moveToNext();
				}
			}

		}
		catch(Exception e)
		{
			Log.e(TAG, "getMonth "+e.getMessage(), e);
		}
		finally
		{
			return c;
		}
	}
	
	private Date getParsedDate(Cursor cursor)
	{
		Date date = new Date();
		String dateString = cursor.getString(
				cursor.getColumnIndex(colNames.DATE));
		try
		{
			date = sdf.parse(dateString);
		}
		catch(ParseException e)
		{
			Log.e(TAG, "Error parsing date '" + dateString + "':"
				+ e.getMessage());
		}
		finally
		{
			return date;
		}
	}
	
	private Cursor getPreviousRecord(SQLiteDatabase db, Date date)
	{
		return db.query(TABLE, colNames.all(), 
						select.previousDate, 
						new String[]{sdf.format(date)}, 
						null, null, colNames.DATE+" DESC", "1"  );
	}
	
	private Cursor getPreviousOrCurrentRecord(SQLiteDatabase db, Date date)
	{
		return db.query(TABLE, colNames.all(), 
				select.previousOrCurrentDate, 
				new String[]{sdf.format(date)}, 
				null, null, colNames.DATE+" DESC", "1"  );
	}
	
	private Cursor getNextRecord(SQLiteDatabase db, Date date)
	{
		return db.query(TABLE, colNames.all(), 
				select.nextDate, 
				new String[]{sdf.format(date)}, 
				null, null, colNames.DATE+" ASC", "1"  );
	}
	
	private void putValue(ContentValues values, Measurable m, String colName)
	{
		if(m.isNull())
			values.putNull(colName);
		else 
			values.put(colName, m.getValue());
	}
	
	private void setValue(Cursor cursor, Measurable m, String colName)
	{
		int colIndex = cursor.getColumnIndex(colName);
		m.isNull(cursor.isNull(colIndex));
		if(!m.isNull())
		{
			m.setValue(cursor.getFloat(colIndex));	
		}
	}
	
}
