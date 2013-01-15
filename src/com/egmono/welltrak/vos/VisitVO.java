package com.egmono.welltrak.vos;

import java.util.Date;
import java.text.SimpleDateFormat;

/** 
* Models a visit to a well;
*/
public class VisitVO
{
	/** Database record id */
	private int id;
	
	public int getId(){ return id; }

	public void setId(int id){ this.id = id; } 
	
	/** Date of visit */
	private Date date = new Date();
	
	public Date getDate(){ return date;}
	
	public String getDateText()
	{
		SimpleDateFormat format = new SimpleDateFormat("EEE MMM DD, yyyy");
		return format.format(date);
	}

	public void setDate(Date date){ this.date = date; }

	/** Flow meter reading (flow totalizer) */
	private float flowMeter = 0.0f;
	
	public float getFlowMeter(){ return flowMeter; }
	
	public String getFlowMeterText(){ 
		return String.format("%.1f",new Object[]{flowMeter});
	}
	
	public void setFlowMeter(float flowMeter){ 
		this.flowMeter = flowMeter; 
	}

	/** Average daily flow */
	private float flowAverageDaily = 0.0f;
	
	public float getFlowAverageDaily(){ return flowAverageDaily; }
	
	public String getFlowAverageDailyText(){
		return String.format("%.3f",new Object[]{flowAverageDaily});
	}
	
	public void setFlowAverageDaily(float flowAverageDaily){ 
		this.flowAverageDaily = flowAverageDaily;
	}
}

