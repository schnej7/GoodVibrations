package teamwork.goodVibrations;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Looper;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class GoodVibrationsActivity extends TabActivity
{
	private String TAG = "GoodVibrationsActivity";
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		//setContentView(R.layout.main_menu);
	    Resources res = getResources(); // Resource object to get Drawables
	    TabHost tabHost = getTabHost();  // The activity TabHost
	    TabHost.TabSpec spec;  // Resusable TabSpec for each tab
	    Intent intent;  // Reusable Intent for each tab

	    // Create an Intent to launch an Activity for the tab (to be reused)
	    intent = new Intent().setClass(this, TriggerDisplayActivity.class);

	    // Initialize a TabSpec for each tab and add it to the TabHost
	    spec = tabHost.newTabSpec("triggers").setIndicator("Triggers").setContent(intent);
	    tabHost.addTab(spec);
	    
	    // Create an Intent to launch an Activity for the tab (to be reused)
	    intent = new Intent().setClass(this, FunctionDisplayActivity.class);

	    // Initialize a TabSpec for each tab and add it to the TabHost
	    spec = tabHost.newTabSpec("functions").setIndicator("Functions").setContent(intent);
	    tabHost.addTab(spec);
	}
	
	@Override
	public void onStart()
	{
		super.onStart();

		Log.d(TAG,"Starting Service");
		startService(new Intent(this, GoodVibrationsService.class));
		Log.d(TAG,"Service Started");
	}

}