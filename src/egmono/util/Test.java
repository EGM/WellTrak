package egmono.util;

import android.util.Log;
import android.widget.Toast;
import java.util.Date;

public class Test
{
	private final static String TAG = "TEST";
	private static int testNumber  = 0; //Test number
	private static int testsPassed = 0; //Tests passed
	private static int testsFailed = 0; //Tests failed
	
	private static void passed(String msg) {
		testsPassed++;
		StringBuilder sb = new StringBuilder("[Test #")
				.append(++testNumber).append(" PASSED] ").append(msg);
		Log.i(TAG, sb.toString());
	}
	
	private static void failed(Object actual, Object expected, String msg) {
		testsFailed++;
		StringBuilder sb = new StringBuilder("[Test #")
			.append(++testNumber)
			.append(" FAILED][Expected: ").append(expected)
			.append("][Actual: ").append(actual).append("] ")
			.append(msg);
		Log.e(TAG, sb.toString());
	}
	
	public static void a(int actual, int expected, String msg) {
		if(actual==expected) { passed(msg); }
		else { failed(actual, expected, msg); }
	}
	
	public static void a(int actual, int expected) {
		a(actual, expected, "");
	}
	
	public static void a(long actual, long expected, String msg) {
		if(actual==expected) { passed(msg); }
		else { failed(actual, expected, msg); }
	}
	
	public static void a(long actual, long expected) {
		a(actual, expected, "");
	}
	
	public static void a(float actual, float expected, String msg) {
		if(actual==expected) { passed(msg); }
		else { failed(actual, expected, msg); }
	}

	public static void a(float actual, float expected) {
		a(actual, expected, "");
	}
	
	public static void a(double actual, double expected, String msg) {
		if(actual==expected) { passed(msg); }
		else { failed(actual, expected, msg); }
	}

	public static void a(double actual, double expected) {
		a(actual, expected, "");
	}

	public static void a(String actual, String expected, String msg) {
		if(actual.equals(expected)) { passed(msg); }
		else { failed(actual, expected, msg); }
	}

	public static void a(String actual, String expected) {
		a(actual, expected, "");
	}
	public static int getTestsRan(){return testNumber;}
	public static int getTestsPassed(){return testsPassed;}
	public static int getTestsFailed(){return testsFailed;}

	public static void displayTestResults()
	{
		String s = new StringBuilder() 
				.append(String.format("******************%n"))
				.append(String.format("* Tests run: %3d *%n",testNumber))
				.append(String.format("* -> passed: %3d *%n",testsPassed))
				.append(String.format("* -> failed: %3d *%n",testsFailed))
		   .append("******************")
		   .toString();
		Log.v(TAG, s);
	}
}
