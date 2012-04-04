package teamwork.goodVibrations;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class TriggerDisplayActivity extends Activity
{
  private static String TAG = "TriggerDisplayActivity";
  private ArrayAdapter<String> triggerArrayAdapter;
  private ListView listView;
  private DataReceiver dataReceiver;

  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.trigger_tab);

    triggerArrayAdapter = new ArrayAdapter<String>(this,R.layout.trigger_list_item);
    listView = (ListView) findViewById(R.id.listViewTriggers);
    listView.setAdapter(triggerArrayAdapter);

    final Button buttonAdd = (Button) findViewById(R.id.addTrigger);
    buttonAdd.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View v)
      {
        Intent TriggerEditIntent = new Intent(getApplicationContext(), TriggerEditActivity.class);
        startActivityForResult(TriggerEditIntent, 0);
      }
    });
    
    IntentFilter messageFilter;
    messageFilter = new IntentFilter(Constants.SERVICE_DATA_TRIGGER_MESSAGE);
    dataReceiver = new DataReceiver();
    registerReceiver(dataReceiver, messageFilter);
    
    /*
    if(triggerentered == 0)
    {
      // Submit a few sample triggers
      int[] startIDs = {0};
      int[] stopIDs = {1};    
      Intent i = new Intent(getApplicationContext(), GoodVibrationsService.class);
      i.putExtra(Constants.INTENT_TYPE,Constants.TRIGGER_TYPE);
      i.putExtra(Constants.INTENT_KEY_TYPE,Constants.TRIGGER_TYPE_TIME);
      i.putExtra(Constants.INTENT_KEY_NAME, "T1 ");
      i.putExtra(Constants.INTENT_KEY_REPEAT_DAYS_BOOL, true);
      i.putExtra(Constants.INTENT_KEY_REPEAT_DAYS_BYTE, (byte)255);
      i.putExtra(Constants.INTENT_KEY_START_TIME,(long)1000);
      i.putExtra(Constants.INTENT_KEY_END_TIME,(long)5000);
      i.putExtra(Constants.INTENT_KEY_START_FUNCTION_IDS, startIDs);
      i.putExtra(Constants.INTENT_KEY_STOP_FUNCTION_IDS, stopIDs);
      startService(i);
      triggerentered++;
    }
    if(triggerentered == 1)
    {
      // Submit a few sample triggers
      int[] startIDs = {2};
      int[] stopIDs = {3};    
      Intent i = new Intent(getApplicationContext(), GoodVibrationsService.class);
      i.putExtra(Constants.INTENT_TYPE,Constants.TRIGGER_TYPE);
      i.putExtra(Constants.INTENT_KEY_TYPE,Constants.TRIGGER_TYPE_TIME);
      i.putExtra(Constants.INTENT_KEY_NAME, "T2 ");
      i.putExtra(Constants.INTENT_KEY_REPEAT_DAYS_BOOL, true);
      i.putExtra(Constants.INTENT_KEY_REPEAT_DAYS_BYTE, (byte)255);
      i.putExtra(Constants.INTENT_KEY_START_TIME,(long)8000);
      i.putExtra(Constants.INTENT_KEY_END_TIME,(long)15000);
      i.putExtra(Constants.INTENT_KEY_START_FUNCTION_IDS, startIDs);
      i.putExtra(Constants.INTENT_KEY_STOP_FUNCTION_IDS, stopIDs);
      startService(i);
      triggerentered++;
    }
    */
  }
  
  public void onResume()
  {
    super.onResume();
    Intent i = new Intent(getApplicationContext(),GoodVibrationsService.class);
    i.putExtra(Constants.INTENT_TYPE,Constants.GET_DATA);
    i.putExtra(Constants.INTENT_KEY_TYPE, Constants.INTENT_KEY_TRIGGER_LIST);
    startService(i);
  }

  public void onDestroy()
  {
    super.onDestroy();
    unregisterReceiver(dataReceiver);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data)
  {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == RESULT_OK)
    {
      Bundle b = data.getExtras();
      // Add name to the list of functions with a different format depending on
      // the function type
      if(b.getInt(Constants.INTENT_TYPE) == Constants.TRIGGER_TYPE) // Should always be true, but just double checking
      {
        switch (b.getInt(Constants.INTENT_KEY_TYPE))
        {
          case Constants.TRIGGER_TYPE_TIME:
            triggerArrayAdapter.add((b.getString(Constants.INTENT_KEY_NAME) + " S:" + b.getByte(Constants.INTENT_KEY_REPEAT_DAYS_BYTE)));
            break;
  
          case Constants.TRIGGER_TYPE_LOCATION:
            triggerArrayAdapter.add((b.getString(Constants.INTENT_KEY_NAME)));
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
      
      Log.d(TAG,"KEYNAME: " + b.getInt(Constants.INTENT_KEY_NAME) + " LIST: " + Constants.INTENT_KEY_TRIGGER_LIST);
      
      if(b.getInt(Constants.INTENT_KEY_NAME) == Constants.INTENT_KEY_TRIGGER_LIST)
      {
        triggerArrayAdapter.clear();
        int length = b.getInt(Constants.INTENT_KEY_DATA_LENGTH);
        String[] triggerNames = b.getStringArray(Constants.INTENT_KEY_TRIGGER_NAMES);
        int[] triggerIDs = b.getIntArray(Constants.INTENT_KEY_TRIGGER_IDS);
        Log.d(TAG,"LENGTH: " + length);
        for(int i = 0; i < length; i++)
        {
          triggerArrayAdapter.add("(" + triggerIDs[i] + ")  " + triggerNames[i]);
        }
      }
    }
  }

}