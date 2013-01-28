package com.egmono.welltrak.model;

import com.egmono.welltrak.WellTrakApp;
import com.egmono.welltrak.R;
import java.text.SimpleDateFormat;
import java.util.Date;
//import android.content.res.Resources;

/** Visit Value Object */
public class VisitModel
{
	private static final String TAG = VisitModel.class.getSimpleName();
	
	private long  _id = 0;
	private Date  date = new Date();
	
	public Pumpage pump1Total = new Pumpage();
	public Pumpage pump2Total = new Pumpage();
	public Pumpage flow = new Pumpage();
	public Analyte cl2Entry = new Analyte();
	public Analyte cl2Remote = new Analyte();
	public Analyte phEntry = new Analyte();
	public Analyte phRemote = new Analyte();
	
	public VisitModel(Date date)
	{
		this.setDate(date);
		
		this.pump1Total.setUnit("Gallons");
		this.pump2Total.setUnit("Gallons");
		this.flow.setUnit("Gallons/Day");
		this.cl2Entry.setUnit("mg/L");
		this.cl2Remote.setUnit("mg/L");
		this.phEntry.setUnit("s.u.");
		this.phRemote.setUnit("s.u.");
	}
	public VisitModel()
	{
		this(new Date());
	}
	
	public long getId(){return this._id;}
	public void setId(long _id){this._id = _id;}
	
	public Date getDate(){return this.date;}
	public void setDate(Date date){this.date = date;}
		
	@Override
	public String toString() 
	{
		SimpleDateFormat sdf = new SimpleDateFormat("(EEE) MMM dd, yyyy");
		StringBuilder sb = new StringBuilder(WellTrakApp.getString(R.string.hdr_send))
			.append("\n")
		//Date
			.append(WellTrakApp.getString(R.string.label_date))
			.append(":\t")
			.append(sdf.format(date))
			.append("\n");
		//Pump #1
		if(!this.pump1Total.isNull)
			sb.append(WellTrakApp.getString(R.string.label_meter1))
			.append(":\t")
			.append(this.pump1Total.toString())
			.append("\t")
			.append(this.pump1Total.getUnit())
			.append("\n");
		//Pump #2
		if(!this.pump2Total.isNull)
			sb.append(WellTrakApp.getString(R.string.label_meter2))
			.append(":\t")
			.append(this.pump2Total.toString())
			.append("\t")
			.append(this.pump2Total.getUnit())
			.append("\n");
		//Flow
		if(!this.flow.isNull)
			sb.append(WellTrakApp.getString(R.string.label_flow))
			.append(":\t")
			.append(flow.toString())
			.append("\t")
			.append(flow.getUnit())
			.append("\n");
		//Cl2, Entry
		if(!this.cl2Entry.isNull)
			sb.append(WellTrakApp.getString(R.string.label_cl2entry))
			.append(":\t")
			.append(cl2Entry.toString())
			.append("\t")
			.append(cl2Entry.getUnit())
			.append("\n");
		//Cl2, Remote
		if(!this.cl2Remote.isNull)
			sb.append(WellTrakApp.getString(R.string.label_cl2remote))
			.append(":\t")
			.append(cl2Remote.toString())
			.append("\t")
			.append(cl2Remote.getUnit())
			.append("\n");
		//pH, Entry
		if(!this.phEntry.isNull)
			sb.append(WellTrakApp.getString(R.string.label_phentry))
			.append(":\t")
			.append(phEntry.toString())
			.append("\t")
			.append(phEntry.getUnit())
			.append("\n");
		//pH, Remote
		if(!this.phRemote.isNull)
			sb.append(WellTrakApp.getString(R.string.label_phremote))
			.append(":\t")
			.append(phRemote.toString())
			.append("\t")
			.append(phRemote.getUnit())
			.append("\n");
		return sb.toString();
	}
}
