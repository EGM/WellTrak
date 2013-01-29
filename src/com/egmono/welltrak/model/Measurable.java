package com.egmono.welltrak.model;

public interface Measurable
{
	public float getValue();
	public void setValue(float value);
	public void setValue(String value);
	
	public boolean isNull();
	public void isNull(boolean bool);
	
	public String getUnit();
	public void setUnit(String unit);
	
	public String toString();
}
