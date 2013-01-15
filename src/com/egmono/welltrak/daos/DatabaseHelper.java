package com.egmono.welltrak.daos;

import com.egmono.welltrak.WellTrakApp;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper
{
	protected final static String DATABASE_NAME = "WellTrak";
	protected final static int DATABASE_VERSION = 1;

	public DatabaseHelper()
	{
		super(WellTrakApp.getContext(), DATABASE_NAME, null, DATABASE_VERSION);
	}
		
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
		DatabaseTestHelper.createTestData();
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		db.execSQL("DROP TABLE IF EXISTS " + VisitDAO.TABLE);
		onCreate(db);
	}
}
