package com.egmono.welltrak.model;

import android.util.Log;

public class Measurement
{
	private final static String TAG = "Measurement";
	private float value = 0.0f;
	private String unit = "";
	public boolean isNull = true;
	
	public float getValue() { return this.value; }
	
	public void setValue(Float value) { 
		Log.d(TAG, "Old value = "+this.toString());
		this.value = value; 
		this.isNull=false;
		Log.d(TAG, "New value = "+this.toString());
	}
	
	public void setValue(String value) {
		if(value.isEmpty()) {
			this.isNull = true;
		}
		else {
			try {
				this.setValue(Float.parseFloat(value));
			}
			catch(NumberFormatException e) {
				Log.e(TAG, "Error parsing value '"+value+"'",e);
			}
		}
	}
	
	public String getUnit() { return this.unit; }
	public void setUnit(String unit) { this.unit = unit; }
	
	@Override
	public String toString() { 
		if(this.isNull) { 
			return ""; 
		}
		else {
			return String.format("%.1f",this.value); 
		}
	}
}
