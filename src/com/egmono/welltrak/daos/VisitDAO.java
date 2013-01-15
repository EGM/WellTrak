package com.egmono.welltrak.daos;

import com.egmono.welltrak.vos.VisitVO;

import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;

public class VisitDAO
{	
	protected final static String TABLE = "visits";
	protected final static String _ID = "_id";
	protected final static String DATE = "date";
	protected final static String METER = "meter";
	protected final static String FLOW = "flow";
	
	protected SQLiteDatabase db;
	
	VisitDAO()
	{
	}
	
	public VisitVO getDay(int year, int month, int day)
	{
		return new VisitVO();
	}
	
	public ArrayList<VisitVO> getMonth(int year, int month)
	{
		ArrayList<VisitVO> list = new ArrayList<VisitVO>();
		return list;
	}

}
