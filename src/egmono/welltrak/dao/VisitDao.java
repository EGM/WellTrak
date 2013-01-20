package egmono.welltrak.dao;

import egmono.util.Test;
import egmono.welltrak.vo.VisitVo;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.Date;
import java.text.*;

public class VisitDao 
{
	private static final String TAG = VisitDao.class.getSimpleName();
	
	protected final static String TABLE = "visits";

	protected static class columns
	{
		protected final static String _ID = "_id";
		protected final static String DATE = "date";
		protected final static String TOTAL = "total";
	
		protected static String[] all() 
		{
			return new String[]{_ID, DATE, TOTAL};
		}
	}
	
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
			
	public VisitVo getDay(Date date)
	{
	 	VisitVo visit = new VisitVo();
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
					visit.setTotal(c.getFloat(c.getColumnIndex(columns.TOTAL)));
				}

				if(n.moveToFirst())
				{
					
					//Find the current or previous total
					Float totalPrevious = c.getFloat(
							c.getColumnIndex(columns.TOTAL));
							
					//Find the next total
					Float totalNext = n.getFloat(
							n.getColumnIndex(columns.TOTAL));
							
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
					visit.setFlow(totalResult/dateResult);
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
