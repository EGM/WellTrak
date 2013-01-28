package com.egmono.welltrak.model;
import android.util.*;

public class Pumpage extends Measurement
{
	private float rate = 1.0f;
	
	public float getRate() { return this.rate; }
	public void setRate(float rate) { this.rate = rate; }
	
	public String toString(int decimalPlaces)
	{
		return String.format("%." 
			+ Integer.toString(decimalPlaces)
			+ "f",this.getValue()); 
	}
	@Override
	public String toString() 
	{ 
			return super.toString(); 
	}
}
