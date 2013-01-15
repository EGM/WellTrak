package com.egmono.welltrak.daos;

import com.egmono.welltrak.vos.VisitVO;

import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;

/**
* Visit data access object to perform the CRUD 
* operations for the visit value object (VisitVO).
*/
public class VisitDAO
{	
	// Table name and field names.
	protected final static String TABLE = "visits";
	protected final static String _ID = "_id";
	protected final static String DATE = "date";
	protected final static String METER = "meter";
	protected final static String FLOW = "flow";
	
	protected SQLiteDatabase db;
	
	/** Default constructor. */
	VisitDAO()
	{
		//Do I even need this?
	}
	
	/** Retrieve single daily visit. */
	public VisitVO getDay(int year, int month, int day)
	{
		//TODO: finish this stub!
		return new VisitVO();
	}
	
	/** Retrieve an array list of visits for entire month. */
	public ArrayList<VisitVO> getMonth(int year, int month)
	{
		//TODO: finish this stub!
		ArrayList<VisitVO> list = new ArrayList<VisitVO>();
		return list;
	}
}
