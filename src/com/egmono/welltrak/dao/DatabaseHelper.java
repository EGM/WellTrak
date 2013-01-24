package com.egmono.welltrak.dao;

import com.egmono.welltrak.WellTrakApp;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import org.apache.commons.io.IOUtils;
import java.io.*;
import java.io.InputStream;
import java.io.IOException;
import java.io.StringWriter;

public class DatabaseHelper extends SQLiteOpenHelper
{
	private static final String TAG = DatabaseHelper.class.getSimpleName();

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
			VisitDao.TABLE + "(" +
			VisitDao.columns._ID    + " INTEGER PRIMARY KEY, " +
			VisitDao.columns.DATE   + " TEXT, " +
			VisitDao.columns.TOTAL1 + " REAL, " +
			VisitDao.columns.TOTAL2 + " REAL, " +
			VisitDao.columns.CL2ENT + " REAL, " +
			VisitDao.columns.CL2REM + " REAL, " +
			VisitDao.columns.PHENT  + " REAL, " +
			VisitDao.columns.PHREM  + " REAL)"; 
		
		db.execSQL(sql);
		
		this.execAssetSQL(db, "TestData.sql");
		Log.i(TAG, new StringBuilder("Created ")
				.append(this.getCount(db, VisitDao.TABLE))
				.append(" records in table ")
				.append(VisitDao.TABLE).toString());
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer)
	{
		db.execSQL("DROP TABLE IF EXISTS " + VisitDao.TABLE);
		onCreate(db);
	}
	
	public int getCount(SQLiteDatabase db, String tableName)
	{
		Cursor c = db.query(tableName, null, null, null, null, null, null);
		if(c.moveToFirst())
		{
			return c.getCount();
		}
		else return -1;
	}

	private void execAssetSQL(SQLiteDatabase db, String fileName)
	{
		try
		{
			InputStream fileStream = WellTrakApp.getContext().getAssets().open(fileName);
			StringWriter stringWriter = new StringWriter();
			IOUtils.copy(fileStream, stringWriter);
			String[] statements = stringWriter.toString().split(";");
			for(int i=0;i<statements.length;i++)
			{
				db.execSQL(statements[i]);
			}
		}
		catch(IOException e)
		{
			Log.e(TAG, "IOException "+e.getMessage(), e);
		}
		catch(Exception e)
		{
			Log.e(TAG, "Exception "+e.getMessage(), e);
		}
	}
}
