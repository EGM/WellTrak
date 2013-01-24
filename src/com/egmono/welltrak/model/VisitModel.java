package com.egmono.welltrak.model;

import com.egmono.welltrak.WellTrakApp;
import com.egmono.welltrak.R;
import java.util.Date;
import android.content.res.Resources;

/** Visit Value Object */
public class VisitModel
{
	private static final String TAG = VisitModel.class.getSimpleName();
	
	private int   _id = 0;
	private Date  date = new Date();
	
	public Pumpage pump1Total = new Pumpage();
	public Pumpage pump2Total = new Pumpage();
	public Pumpage flow = new Pumpage();
	public Analyte cl2Entry = new Analyte();
	public Analyte cl2Remote = new Analyte();
	public Analyte phEntry = new Analyte();
	public Analyte phRemote = new Analyte();
	
	public VisitModel()
	{
		pump1Total.setUnit("Gallons");
		pump2Total.setUnit("Gallons");
		flow.setUnit("Gallons/Day");
		cl2Entry.setUnit("mg/L");
		cl2Remote.setUnit("mg/L");
		phEntry.setUnit("s.u.");
		phRemote.setUnit("s.u.");
	}
	
	public int getId(){return _id;}
	public void setId(int _id){this._id = _id;}
	
	public Date getDate(){return date;}
	public void setDate(Date date){this.date = date;}
		
	@Override
	public String toString() 
	{
		return new StringBuilder("Well Visit:\n")
		//Date
			.append(WellTrakApp.getString(R.string.label_date))
			.append("\t")
			.append(date.toLocaleString())
			.append("\n")
		//Pump #1
			.append(WellTrakApp.getString(R.string.label_meter1))
			.append("\t")
			.append(pump1Total.toString())
			.append("\t")
			.append(pump1Total.getUnit())
			.append("\n")
		//Pump #2
			.append(WellTrakApp.getString(R.string.label_meter2))
			.append("\t")
			.append(pump2Total.toString())
			.append("\t")
			.append(pump2Total.getUnit())
			.append("\n")
		//Flow
			.append(WellTrakApp.getString(R.string.label_flow))
			.append("\t")
			.append(flow.toString())
			.append("\t")
			.append(flow.getUnit())
			.append("\n")
		//Cl2, Entry
			.append(WellTrakApp.getString(R.string.label_cl2entry))
			.append("\t")
			.append(cl2Entry.toString())
			.append("\t")
			.append(cl2Entry.getUnit())
			.append("\n")
		//Cl2, Remote
			.append(WellTrakApp.getString(R.string.label_cl2remote))
			.append("\t\t")
			.append(cl2Remote.toString())
			.append("\t")
			.append(cl2Remote.getUnit())
			.append("\n")
		//pH, Entry
			.append(WellTrakApp.getString(R.string.label_phentry))
			.append("\t")
			.append(phEntry.toString())
			.append("\t")
			.append(phEntry.getUnit())
			.append("\n")
		//pH, Remote
			.append(WellTrakApp.getString(R.string.label_phremote))
			.append("\t")
			.append(phRemote.toString())
			.append("\t")
			.append(phRemote.getUnit())
			.append("\n")
			.toString();
	}
}
