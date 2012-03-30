package teamwork.goodVibrations;

import teamwork.goodVibrations.functions.Function;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class FunctionDisplayActivity extends Activity
{
  private static String TAG = "FunctionDisplayActivity";
  
	private ArrayAdapter <String> functionArrayAdapter;
	private ListView listView;
	private DataReceiver dataReceiver;

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.function_tab);


		functionArrayAdapter = new ArrayAdapter<String>(this, R.layout.function_list_item);
		listView = (ListView) findViewById(R.id.listViewFunctions);
		listView.setAdapter(functionArrayAdapter);

		final Button buttonAdd = (Button) findViewById(R.id.addFunction);
		buttonAdd.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				Intent functionEditIntent = new Intent(getApplicationContext(),FunctionEditActivity.class);
				startActivityForResult(functionEditIntent, 0);
			}
		});
		
    
    IntentFilter messageFilter;
    messageFilter = new IntentFilter(Constants.SERVICE_DATA_MESSAGE);
    dataReceiver = new DataReceiver();
    registerReceiver(dataReceiver, messageFilter);
	}

  public void onResume()
  {
    super.onResume();
    Intent i = new Intent(getApplicationContext(),GoodVibrationsService.class);
    i.putExtra(Constants.INTENT_TYPE,Constants.GET_DATA);
    i.putExtra(Constants.INTENT_KEY_TYPE, Constants.INTENT_KEY_FUNCTION_LIST);
    startService(i);
  }

  public void onDestroy()
  {
    super.onDestroy();
    unregisterReceiver(dataReceiver);
  }
  
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==RESULT_OK){
		  Bundle b = data.getExtras();
		  if(b.getInt(Constants.INTENT_TYPE) == Constants.FUNCTION_TYPE) // Should always be true but just to double check
		  {
  		  // Add name to the list of functions with a different format depending on the function type
  		  switch(b.getInt(Constants.INTENT_KEY_TYPE))
  		  {
  		    case Constants.FUNCTION_TYPE_VOLUME:
  		      functionArrayAdapter.add(b.getString(Constants.INTENT_KEY_NAME) + "  Vol: " + b.getInt(Constants.INTENT_KEY_VOLUME));
  		      break;
  		      
  		    case Constants.FUNCTION_TYPE_RINGTONE:
  		      functionArrayAdapter.add(b.getString(Constants.INTENT_KEY_NAME) + "  Tone: " + b.getParcelable(Constants.INTENT_KEY_URI));
  		      break;
  		  }
		  }
		  
			// Create the intent that gets sent to the service
		  data.setClass(this, GoodVibrationsService.class);
			startService(data); // Calls GoodVibrationsService.onStartCommand()
		}
		else
		{
			Toast.makeText(this, "Fail", Toast.LENGTH_LONG).show();
		}
	}
	
  public class DataReceiver extends BroadcastReceiver
  {
    @Override
    public void onReceive(Context context, Intent intent)//this method receives broadcast messages. Be sure to modify AndroidManifest.xml file in order to enable message receiving
    {
      Log.d(TAG,"RECIEVED BROADCAST MESSAGE");
      
      Bundle b = intent.getExtras();
      
      if(b.getInt(Constants.INTENT_KEY_NAME) == Constants.INTENT_KEY_FUNCTION_LIST)
      {
        functionArrayAdapter.clear();
        int length = b.getInt(Constants.INTENT_KEY_DATA_LENGTH);
        String[] functionNames = b.getStringArray(Constants.INTENT_KEY_FUNCTION_NAMES);
        int[] functionIDs = b.getIntArray(Constants.INTENT_KEY_FUNCTION_IDS);
        for(int i = 0; i < length; i++)
        {
          functionArrayAdapter.add("(" + functionIDs[i] + ")  " + functionNames[i]);
        }
      }
    }
  }
	
}