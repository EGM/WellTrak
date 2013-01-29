package com.egmono.welltrak.model;

import android.util.Log;

public class Measurement implements Measurable
{
	private final static String TAG = "Measurement";
	private float value = 0.0f;
	private String unit = "";
	private boolean isNull = true;

	public float getValue() { return this.value; }
	
	public void setValue(float value) { 
		this.value = value; 
		this.isNull=false;
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

	public boolean isNull() { return this.isNull; }
	public void isNull(boolean bool) { this.isNull = bool; }
	
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
