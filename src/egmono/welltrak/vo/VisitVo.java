package egmono.welltrak.vo;

import java.util.Date;

public class VisitVo
{
	private static final String TAG = VisitVo.class.getSimpleName();
	
	private int   _id = 0;
	private Date  date = new Date();
	private float total = 0.0f;
	private float flow = 0.0f;
	
	public int getId(){return _id;}
	public void setId(int _id){this._id = _id;}
	
	public Date getDate(){return date;}
	public void setDate(Date date){this.date = date;}
	
	public float getTotal(){return total;}
	public void setTotal(Float total){this.total = total;}
	
	public float getFlow(){return flow;}
	public void setFlow(Float flow){this.flow = flow;}
}
