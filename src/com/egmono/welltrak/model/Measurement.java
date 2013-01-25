package com.egmono.welltrak.model;

public class Measurement
{
	private float value = 0.0f;
	private String unit = "";
	public boolean isNull;
	
	public float getValue() { return this.value; }
	public void setValue(Float value) { this.value = value; }
	
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
