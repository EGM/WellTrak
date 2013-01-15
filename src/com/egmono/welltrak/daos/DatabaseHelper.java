package com.egmono.welltrak.daos;

import com.egmono.welltrak.WellTrakApp;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
* Assists in the creation (if required) 
* or upgrade (if required) of the database.
*/
public class DatabaseHelper extends SQLiteOpenHelper
{
	protected final static String DATABASE_NAME = "WellTrak";
	protected final static int DATABASE_VERSION = 1;

	/** Public constructor. */
	public DatabaseHelper()
	{
		super(WellTrakApp.getContext(), DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	/** Creates table structure(s) when creating database. */
	@Override
	public void onCreate(SQLiteDatabase db)
	{		
		final String sql = "CREATE TABLE " + 
				VisitDAO.TABLE + "(" +
				VisitDAO._ID   + " INTEGER PRIMARY KEY, " +
				VisitDAO.DATE  + " TEXT, " +
				VisitDAO.METER + " REAL, " +
				VisitDAO.FLOW  + " REAL)"; 
		db.execSQL(sql);
		
		// Load table data to perform testing on. 
		DatabaseTestHelper.createTestData();
	}
	
	/** Deletes table(s) (if they exist), then calls onCreate. */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		db.execSQL("DROP TABLE IF EXISTS " + VisitDAO.TABLE);
		onCreate(db);
	}
}
