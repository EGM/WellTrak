package com.egmono.welltrak.model;

public class Measurement
{
	private float value = 0.0f;
	private String unit = "";
	
	public float getValue() { return this.value; }
	public void setValue(float value) { this.value = value; }
	
	public String getUnit() { return this.unit; }
	public void setUnit(String unit) { this.unit = unit; }
	
	@Override
	public String toString() { 
		return String.format("%.1f",this.value); }
		//return new StringBuilder("").append(this.value).toString(); }
		//return Float.toString(this.value); }
}
