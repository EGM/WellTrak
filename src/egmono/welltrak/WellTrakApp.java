package egmono.welltrak;
import egmono.util.Test;
import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import java.util.Date;
/**
* Global application methods and variables.
*/
public class WellTrakApp extends Application {
	
	private static final String TAG = WellTrakApp.class.getSimpleName();
	private static WellTrakApp instance;
	
	/** Called when the application is first created. */
	@Override
	public void onCreate() {
		super.onCreate();
		Log.i(TAG, "WellTrakApp.onCreate was called");
		instance = this;		
		
		Test.a(5,5,"<- successfull");
		Test.a(5,3,"<- failure");
		Test.a(53,53);
		Test.a(54,55);
		Test.a("actual","actual","success!");
		Test.a("abc","cat","failure!");
		Test.a(1.0f,1.0f,"float pass");
		Test.a(1.0f,2.0f,"float fail");
		Date d1 = new Date();
		Date d2 = new Date();
		Test.a(d1,d1);
		Test.a(d1,d2);
		Test.a(d1.getTime(),d2.getTime());
		Test.displayTestResults();
	}
	
	/** 
	* Provides a simple mechanism to retrieve application context. 
	*/
	public static Context getContext() {
		return instance.getApplicationContext();
	}
}
