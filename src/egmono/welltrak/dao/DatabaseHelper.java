package egmono.welltrak.dao;

import egmono.welltrak.WellTrakApp;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
			VisitDao.columns._ID   + " INTEGER PRIMARY KEY, " +
			VisitDao.columns.DATE  + " TEXT, " +
			VisitDao.columns.TOTAL + " REAL)"; 
		db.execSQL(sql);
		
		TestsHelper.createTestData(db);
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer)
	{
		db.execSQL("DROP TABLE IF EXISTS " + VisitDao.TABLE);
		onCreate(db);
	}
	
	private void createTestValues(SQLiteDatabase db)
	{
		ContentValues cv = new ContentValues();
		
		cv.put(VisitDao.columns.DATE, "2013-01-15");
		cv.put(VisitDao.columns.TOTAL, 1000.0f);
		db.insert(VisitDao.TABLE, VisitDao.columns.DATE, cv);
		
		cv.put(VisitDao.columns.DATE, "2013-01-17");
		cv.put(VisitDao.columns.TOTAL, 2000.0f);
		db.insert(VisitDao.TABLE, VisitDao.columns.DATE, cv);
	}
}
