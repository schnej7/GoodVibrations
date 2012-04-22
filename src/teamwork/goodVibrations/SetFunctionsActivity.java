package teamwork.goodVibrations;

import java.util.ArrayList;

import teamwork.goodVibrations.functions.FunctionForUI;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class SetFunctionsActivity extends Activity
{
  private static String TAG = "TimeTriggerSetFunctionsActivity";
  ListView lview;
  SetFunctionsAdapter arrayAdapter;
  ArrayList<FunctionForUI> funcs = null;
  DataReceiver dataReceiver;
  Intent mIntent = new Intent();

  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.time_trigger_functions);
    // initialize the list view
    lview = (ListView) findViewById(R.id.timetriggerfunctionlview);

    // initialize array list
    funcs = new ArrayList<FunctionForUI>();
    // initialize array adapter
    arrayAdapter = new SetFunctionsAdapter(SetFunctionsActivity.this, R.layout.time_trigger_functions_items, funcs);

    // Set the above adapter as the adapter of choice for the list
    lview.setAdapter(arrayAdapter);

    // register data receiver to get functions
    IntentFilter messageFilter;
    messageFilter = new IntentFilter(Constants.SERVICE_DATA_FUNCTION_MESSAGE);
    dataReceiver = new DataReceiver();
    registerReceiver(dataReceiver, messageFilter);

    final Button buttonDone = (Button) findViewById(R.id.doneSetTimeTriggerFunctions);

    buttonDone.setOnClickListener(new View.OnClickListener()
    {

      public void onClick(View v)
      {
        Log.d(TAG, "Running onClick()");
        int count = arrayAdapter.getCount();
        ArrayList<Integer> chks = new ArrayList<Integer>();
        for(int i = 0; i < count; i++)
        {
          FunctionForUI f = arrayAdapter.getItem(i);
          if(f.chkbx.isChecked())
          {
            chks.add(f.id);
          }
        }
        // convert to int [] from ArrayList<Integer>
        int[] intChks = new int[chks.size()];
        for(int j = 0; j < chks.size(); j++)
        {
          intChks[j] = chks.get(j).intValue();
        }
        mIntent.putExtra(Constants.INTENT_KEY_FUNCTION_IDS, intChks);
        setResult(RESULT_OK, mIntent);
        finish();
      }
    });

  }
  
  Bundle b;

  public void onStart()
  {
    super.onStart();
    Intent i = new Intent(getApplicationContext(), GoodVibrationsService.class);
    i.putExtra(Constants.INTENT_TYPE, Constants.GET_DATA);
    i.putExtra(Constants.INTENT_KEY_TYPE, Constants.INTENT_KEY_FUNCTION_LIST);
    startService(i);
    
    try{
      b = getIntent().getExtras();
      int[] intFunctionIDs = b.getIntArray(Constants.INTENT_KEY_FUNCTION_IDS);
      Log.d( TAG, "intFunctionIDs.length = " + intFunctionIDs.length );
    }
    catch( NullPointerException e){
      Log.e( TAG, "NullPointerException onStart", e);
    }
  }

  public void onDestroy()
  {
    super.onDestroy();
    unregisterReceiver(dataReceiver);
  }

  class DataReceiver extends BroadcastReceiver
  {
    @Override
    public void onReceive(Context context, Intent intent)// this method receives
                                                         // broadcast messages.
                                                         // Be sure to modify
                                                         // AndroidManifest.xml
                                                         // file in order to
                                                         // enable message
                                                         // receiving
    {
      Log.d(TAG, "RECIEVED BROADCAST MESSAGE");

      Bundle b2 = intent.getExtras();

      if(b2.getInt(Constants.INTENT_KEY_NAME) == Constants.INTENT_KEY_FUNCTION_LIST)
      {
        String[] functionNames = b2.getStringArray(Constants.INTENT_KEY_FUNCTION_NAMES);
        int[] functionIDs = b2.getIntArray(Constants.INTENT_KEY_FUNCTION_IDS);
        for(int i = 0; i < functionNames.length; i++)
        {
          FunctionForUI fTemp = new FunctionForUI(functionIDs[i], functionNames[i]);
          funcs.add(fTemp);
        }
        // notify array adapter of changes
        arrayAdapter.notifyDataSetChanged();
      }
      //repopulate fields if we have been here before
      Log.d( TAG, "Trying to get intent for existing function ids");
      
      try{
        Log.d( TAG, "funcs.size() = " + funcs.size());
        
        int[] intFunctionIDs = b.getIntArray(Constants.INTENT_KEY_FUNCTION_IDS);
        Log.d( TAG, "intFunctionIDs.length = " + intFunctionIDs.length );
        
        int size = arrayAdapter.getCount();
        for( int i = 0; i < size; i++){
          for( int j = 0; j < intFunctionIDs.length; j++ ){
            if( funcs.get(i).id == intFunctionIDs[j] )
            {
              FunctionForUI ffui = arrayAdapter.getItem(i);
              if( ffui == null){
                Log.d(TAG, "funcs.get(i) returned null");
              }
              else{
                ffui.shouldBeChecked = true;
              }
            }
          }
        }
        arrayAdapter.notifyDataSetChanged();
      }
      catch(NullPointerException e){
        Log.d( TAG, "Null pointer exception dude!");
      }
      
    }
  }
}
