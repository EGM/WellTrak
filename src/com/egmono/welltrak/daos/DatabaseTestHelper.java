package com.egmono.welltrak.daos;

import com.egmono.welltrak.WellTrakApp;
import com.egmono.welltrak.R;
import com.egmono.welltrak.daos.DatabaseHelper;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;
import android.database.Cursor;
import android.util.Log;
import android.content.*;

public class DatabaseTestHelper
{
	public static void createTestData()
	{
		SQLiteDatabase db = new DatabaseHelper().getWritableDatabase();
		String s;
		try 
		{
			InputStream in = WellTrakApp.getContext().getResources().openRawResource(R.raw.sql);
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = builder.parse(in, null);
			NodeList statements = doc.getElementsByTagName("statement");
			for (int i=0; i<statements.getLength(); i++) 
			{
				s = statements.item(i).getChildNodes().item(0).getNodeValue();
				db.execSQL(s);
			}
		} 
		catch (Throwable t) 
		{
			Toast.makeText(WellTrakApp.getContext(), t.toString(), 50000).show();
		}
	}
}
