package egmono.util;

import android.util.Log;
import android.widget.Toast;
//import egmono.welltrak.*;
import java.util.Date;

public class Test
{
	private final static String TAG = "TEST";
	private static int n = 1; //Test number.
	private static int p = 0; //Tests passed.
	private static int f = 0; //Tests failed;
	private static long t = -1L;
	
	public static void a(String title, int expected, int actual)
	{
		if(expected==actual){
			p++;
			Log.v(TAG, String.format("* (%d) %s (pass)", n++, title));
		}
		else{
			f++;
			Log.v(TAG, String.format(
					"* (%d) %s (fail - expected '%d', actual value '%d')",
					n++, title, expected, actual));
		}
	}
	public static void a(String title, String expected, String actual)
	{
		if(expected.equals(actual)){
			p++;
			Log.v(TAG, String.format("* (%d) %s (pass)", n++, title));
		}
		else{
			f++;
			Log.v(TAG, String.format(
					  "* (%d) %s (fail - expected '%s', actual value '%s')",
					  n++, title, expected, actual));
		}
	}
	public static void a(String title, Date expected, Date actual)
	{
		if(expected==actual){
			p++;
			Log.v(TAG, String.format("* (%d) %s (pass)", n++, title));
		}
		else{
			f++;
			Log.v(TAG, String.format(
					  "* (%d) %s (fail - expected '%s', actual value '%s')",
					  n++, title, expected.toString(), actual.toString()));
		}
	}
	public static void a(String title, float expected, float actual)
	{
		try {
			p++;
			if(expected==actual){
				Log.v(TAG, String.format("* (%d) %s (pass)", n++, title));
			}
			else {
				f++;
				Log.v(TAG, String.format(
						  "* (%d) %s (fail - expected '%f', actual value '%f')",
						  n++, title, expected, actual));
			}
		}
		catch(Exception e){Log.e(TAG,"Error: "+e.getMessage(),e);}
	}
	public static void a(String title, long expected, long actual)
	{
		if(expected==actual){
			p++;
			Log.v(TAG, String.format("* (%d) %s (pass)", n++, title));
		}
		else{
			f++;
			Log.v(TAG, String.format(
					  "* (%d) %s (fail - expected '%d', actual value '%d')",
					  n++, title, expected, actual));
		}
	}
	public static void a(String title, double expected, double actual)
	{
		if(expected==actual){
			p++;
			Log.v(TAG, String.format("* (%d) %s (pass)", n++, title));
		}
		else{
			f++;
			Log.v(TAG, String.format(
					  "* (%d) %s (fail - expected '%f', actual value '%f')",
					  n++, title, expected, actual));
		}
	}
	public static int getPasses(){return p;}
	public static int getFails(){return f;}
	public static void displayHeader()
	{
		String s = new StringBuilder() 
				.append(String.format("**************************%n"))
				.append(String.format("*%15s%9s*%n","Tests:"," "))
				.append("**************************").toString();
		Log.v(TAG, s);
		t = System.currentTimeMillis();
	}
	public static void displayTestResults()
	{
		if(t!=-1L)t = System.currentTimeMillis() - t;
		StringBuilder sb = new StringBuilder() 
				.append(String.format("**************************%n"))
				.append(String.format("* Tests run: %3d         *%n",n-1))
				.append(String.format("* -> passed: %3d         *%n",p))
				.append(String.format("* -> failed: %3d         *%n",f));
		if(t!=-1L)sb.append(String.format("* -> completed in %04dms *%n",t));
		sb.append("**************************");
		Log.v(TAG, sb.toString());
	}
}
