package com.egmono.welltrak.model;

import com.egmono.util.Test;

public class Measurement
{
	private float value = 0.0f;
	private String unit = "";
	public boolean isNull = true;
	
	public float getValue() { return this.value; }
	public void setValue(Float value) { this.value = value; }
	
	public String getUnit() { return this.unit; }
	public void setUnit(String unit) { this.unit = unit; }
	
	@Override
	public String toString() { 
//		Test.a(this.isNull, false, "*.isNull=false, value="+Float.toString(value));
		if(this.isNull) { 
			return ""; 
		}
		else {
			return String.format("%.1f",this.value); 
		}
	}
}
