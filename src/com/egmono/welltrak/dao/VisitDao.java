package com.egmono.welltrak.dao;

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
	private static class select
	{		
		protected final static String _id = columns._ID + " = ?";
		protected final static String previousDate = columns.DATE + " < ?";
		protected final static String previousOrCurrentDate = columns.DATE + " <= ?";
		protected final static String nextDate = columns.DATE + " > ?";
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
		
		values.put(columns.DATE, sdf.format(visit.getDate()));		
		putValue(values, visit.pump1Total, columns.TOTAL1);
		putValue(values, visit.pump2Total, columns.TOTAL2);
		putValue(values, visit.cl2Entry,   columns.CL2ENT);
		putValue(values, visit.cl2Remote,  columns.CL2REM);
		putValue(values, visit.phEntry,    columns.PHENT);
		putValue(values, visit.phRemote,   columns.PHREM);
		
		if(visit.getId()==0) // is a new record
		{
			long new_id = db.insert(TABLE, columns.DATE, values);
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
			values.put(columns._ID, visit.getId());
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
			if(sdf.format(date).equals(currentRecord.getString(currentRecord.getColumnIndex(columns.DATE))))
			{
				//Returned current date so set id
				visit.setId(currentRecord.getInt(currentRecord.getColumnIndex(columns._ID)));

				setValue(currentRecord, visit.pump1Total, columns.TOTAL1);
				setValue(currentRecord, visit.pump2Total, columns.TOTAL2);
				setValue(currentRecord, visit.cl2Entry,   columns.CL2ENT);
				setValue(currentRecord, visit.cl2Remote,  columns.CL2REM);
				setValue(currentRecord, visit.phEntry,    columns.PHENT);
				setValue(currentRecord, visit.phRemote,   columns.PHREM);
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
						currentRecord.getColumnIndex(columns.TOTAL1));
				Float totalPrevious2 = currentRecord.getFloat(
						currentRecord.getColumnIndex(columns.TOTAL2));
							
				//Find the next total
				Float totalNext1 = nextRecord.getFloat(
						nextRecord.getColumnIndex(columns.TOTAL1));
				Float totalNext2 = nextRecord.getFloat(
						nextRecord.getColumnIndex(columns.TOTAL2));
					
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
	
	private Date getParsedDate(Cursor cursor)
	{
		Date date = new Date();
		String dateString = cursor.getString(
				cursor.getColumnIndex(columns.DATE));
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
		return db.query(TABLE, columns.all(), 
						select.previousDate, 
						new String[]{sdf.format(date)}, 
						null, null, columns.DATE+" DESC", "1"  );
	}
	
	private Cursor getPreviousOrCurrentRecord(SQLiteDatabase db, Date date)
	{
		return db.query(TABLE, columns.all(), 
				select.previousOrCurrentDate, 
				new String[]{sdf.format(date)}, 
				null, null, columns.DATE+" DESC", "1"  );
	}
	
	private Cursor getNextRecord(SQLiteDatabase db, Date date)
	{
		return db.query(TABLE, columns.all(), 
				select.nextDate, 
				new String[]{sdf.format(date)}, 
				null, null, columns.DATE+" ASC", "1"  );
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
