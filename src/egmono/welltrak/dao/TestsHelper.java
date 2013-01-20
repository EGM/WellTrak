package egmono.welltrak.dao;

import egmono.welltrak.WellTrakApp;
import egmono.welltrak.R;
import egmono.welltrak.dao.DatabaseHelper;

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

/**
* Builds the database up with data to test with, 
*   with all sql statements stored in xml format
*   (res/raw/sql.xml)
*/
public class TestsHelper
{
	private static final String TAG = TestsHelper.class.getSimpleName();
	
	public static void createTestData(SQLiteDatabase db)
	{
	
		//SQLiteDatabase db = new DatabaseHelper().getWritableDatabase();
		String s;
		try 
		{
			Log.i(TAG, "(TEST) Processing sql statements.");
			InputStream in = WellTrakApp.getContext().getResources().openRawResource(R.raw.sql);
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = builder.parse(in, null);
			NodeList statements = doc.getElementsByTagName("statement");
			for (int i=0; i<statements.getLength(); i++) 
			{
				s = statements.item(i).getChildNodes().item(0).getNodeValue();
				db.execSQL(s);
			}
			Log.i(TAG, "(TEST) Finished processing sql statements.");
		} 
		catch (Throwable t) 
		{
			Log.e(TAG, "(TEST) Error: "+t.getMessage(),t);
			Toast.makeText(WellTrakApp.getContext(), t.toString(), 50000).show();
		}
		finally
		{
			//db.close();
		}
	}
}
