package com.egmono.welltrak.dao;

import com.egmono.welltrak.WellTrakApp;

import android.content.ContentValues;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import android.util.Log;

import org.apache.commons.io.IOUtils;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.IOException;
import java.io.StringWriter;

/**
* Provides a helper class to get a readable/writable connection to
* the database, creating or upgrading tables as needed.
*/
public class DatabaseHelper extends SQLiteOpenHelper
{
	// Tag for use in logging.
	private static final String TAG = DatabaseHelper.class.getSimpleName();

	protected final static String DATABASE_NAME = "WellTrak";
	protected final static int DATABASE_VERSION = 1;

	public DatabaseHelper()
	{
		super(WellTrakApp.getContext(), DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	/** Create the database and table(s) if they don't exist. */
	@Override
	public void onCreate(SQLiteDatabase db)
	{
		// Create visits table.
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
	
	/** Method of upgrading database from one version to the next. */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer)
	{
		db.execSQL("DROP TABLE IF EXISTS " + VisitDao.TABLE);
		onCreate(db);
	}
	
	/** Count of records in a table. */
	public int getCount(SQLiteDatabase db, String tableName)
	{
		Cursor c = db.query(tableName, null, null, null, null, null, null);
		if(c.moveToFirst()) 
			return c.getCount();
		else 
			return -1;
	}

	/** 
	* Execute a set of SQL commands from a file in the assets folder. 
	*/
	public void execAssetSQL(SQLiteDatabase db, String fileName)
	{
		try 
		{
			InputStream fileStream = WellTrakApp.getContext().getAssets().open(fileName);
			StringWriter stringWriter = new StringWriter();
			IOUtils.copy(fileStream, stringWriter);
			String[] statements = stringWriter.toString().split(";");
			for(int i=0;i<statements.length;i++) {
				db.execSQL(statements[i]);
			}
		}
		
		catch(FileNotFoundException e) { 
			Log.e(TAG, "FileNotFoundException "+e.getMessage(), e); 
		}
		
		catch(IOException e) { 
			Log.e(TAG, "IOException "+e.getMessage(), e); 
		}
		
		catch(Exception e) { 
			Log.e(TAG, "Exception "+e.getMessage(), e); 
		}
	}
}
