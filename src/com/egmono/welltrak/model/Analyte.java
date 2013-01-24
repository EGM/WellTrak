package com.egmono.welltrak.model;

public class Analyte extends Measurement
{
	private char qualifier = '\u0000';
	
	public char getQualifier() { return this.qualifier; }
	public void setQualifier(char qualifier) { this.qualifier = qualifier; }
}
