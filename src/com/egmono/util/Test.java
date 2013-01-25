package com.egmono.util;

import android.util.Log;
import java.util.Date;

public class Test {

	private final static String TAG = "TEST";
	
	private static int testNumber  = 0; //Test number
	private static int testsPassed = 0; //Tests passed
	private static int testsFailed = 0; //Tests failed
	
	//Convenience methods
	public static int getRan()    { return testNumber; }
	
	public static int getPassed() { return testsPassed; }
	
	public static int getFailed() { return testsFailed; }

	//Write results to log
	public static void displayTestResults() {
		StringBuilder sb = new StringBuilder()
			.append("\n* Tests ran: ").append(testNumber)
			.append("\n  -> passed: ").append(testsPassed)
			.append("\n  -> failed: ").append(testsFailed)
			.append("\n ");
		Log.v(TAG, sb.toString());
	}
	
	//Tests
	public static void a(boolean actual, boolean expected, String msg) {
		if(actual==expected) passed(msg); 
		else failed(actual, expected, msg); 
	}

	public static void a(boolean actual, boolean expected) {
		a(actual, expected, "");
	}
	
	public static void a(int actual, int expected, String msg) {
		if(actual==expected) passed(msg); 
		else failed(actual, expected, msg); 
	}
	
	public static void a(int actual, int expected) {
		a(actual, expected, "");
	}
	
	public static void a(long actual, long expected, String msg) {
		if(actual==expected) passed(msg); 
		else failed(actual, expected, msg); 
	}
	
	public static void a(long actual, long expected) {
		a(actual, expected, "");
	}
	
	public static void a(float actual, float expected, String msg) {
		if(actual==expected) passed(msg); 
		else failed(actual, expected, msg); 
	}

	public static void a(float actual, float expected) {
		a(actual, expected, "");
	}
	
	public static void a(double actual, double expected, String msg) {
		if(actual==expected) passed(msg); 
		else failed(actual, expected, msg); 
	}

	public static void a(double actual, double expected) {
		a(actual, expected, "");
	}

	public static void a(String actual, String expected, String msg) {
		if(actual.equals(expected)) passed(msg); 
		else failed(actual, expected, msg); 
	}

	public static void a(String actual, String expected) {
		a(actual, expected, "");
	}
	
	public static void a(Date actual, Date expected, String msg) {
		if(actual.getTime()==expected.getTime()) passed(msg); 
		else failed(actual, expected, msg); 
	}

	public static void a(Date actual, Date expected) {
		a(actual, expected, "");
	}
	
	//Write passing result to log, with msg if present
	private static void passed(String msg) {
		testsPassed++;
		StringBuilder sb = new StringBuilder("* Test #")
			.append(++testNumber)
			.append(" PASSED * ")
			.append(msg);
		Log.i(TAG, sb.toString());
	}

	//Write failing result to log, with msg if present
	//  - include actual and expected values
	//  - include which method called the test
	private static void failed(Object actual, Object expected, String msg) {
		int element = 2;
		StackTraceElement[] trace = new Throwable().getStackTrace();
		if(trace[element].getMethodName().equals("a")
			&& trace[element].getClassName().endsWith(".Test")) element = 3;
		testsFailed++;
		StringBuilder sb = new StringBuilder("* Test #")
			.append(++testNumber)
			.append(" FAILED * ")
			.append(msg)
			.append("\n  (Expected: [")
			.append(expected)
			.append("], Actual: [")
			.append(actual)
			.append("])\n  (Called from: [")
			.append(trace[element].getClassName())
			.append(".")
			.append(trace[element].getMethodName())
			.append("])\n  (Called from: [")
			.append(trace[element+1].getClassName())
			.append(".")
			.append(trace[element+1].getMethodName())
			.append("])\n  (Called from: [")
			.append(trace[element+2].getClassName())
			.append(".")
			.append(trace[element+2].getMethodName())
			.append("])");
		Log.e(TAG, sb.toString());
	}
}
