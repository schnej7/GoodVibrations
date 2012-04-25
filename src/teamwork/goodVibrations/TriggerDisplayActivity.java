package teamwork.goodVibrations;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

//displays the list of triggers
public class TriggerDisplayActivity extends Activity
{
  private static String TAG = "TriggerDisplayActivity";
  private ArrayAdapter<String> triggerArrayAdapter;
  private ListView listView;
  private DataReceiver dataReceiver;

  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.trigger_tab); //set correct UI

    //listview
    triggerArrayAdapter = new ArrayAdapter<String>(this,
        R.layout.trigger_list_item);
    listView = (ListView) findViewById(R.id.listViewTriggers);
    listView.setAdapter(triggerArrayAdapter);

    registerForContextMenu(listView);

    //add trigger button
    final Button buttonAdd = (Button) findViewById(R.id.addTrigger);
    buttonAdd.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View v)
      {
        Intent TriggerEditIntent = new Intent(getApplicationContext(),
            TriggerEditActivity.class);
        //set edited bool to false, since adding a new trigger
        TriggerEditIntent.putExtra(Constants.INTENT_KEY_EDITED_BOOL, false); 
        startActivityForResult(TriggerEditIntent, 0);
      }
    });

    IntentFilter messageFilter;
    messageFilter = new IntentFilter(Constants.SERVICE_MESSAGE);
    dataReceiver = new DataReceiver();
    registerReceiver(dataReceiver, messageFilter);
  }

  public void onResume()
  {
    super.onResume();
    Intent i = new Intent(getApplicationContext(), GoodVibrationsService.class);
    i.putExtra(Constants.INTENT_TYPE, Constants.GET_DATA);
    i.putExtra(Constants.INTENT_KEY_TYPE, Constants.INTENT_KEY_TRIGGER_LIST);
    startService(i);
  }

  public void onDestroy()
  {
    super.onDestroy();
    unregisterReceiver(dataReceiver);
  }

  @Override
  //for clicking and holding on a trigger to edit and delete
  public void onCreateContextMenu(ContextMenu menu, View v,
      ContextMenuInfo menuInfo)
  {
    AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
    menu.setHeaderTitle(triggerArrayAdapter.getItem(info.position));
    menu.add(Menu.NONE, Constants.MENU_ITEM_EDIT, Menu.NONE, "Edit"); // TODO
                                                                      // The
                                                                      // strings
                                                                      // should
                                                                      // be
                                                                      // resources
    menu.add(Menu.NONE, Constants.MENU_ITEM_DELETE, Menu.NONE, "Delete");
  }

  @Override
  //handle editing or deleting of trigger on click and hold
  public boolean onContextItemSelected(MenuItem item)
  {
    AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
        .getMenuInfo();
    int menuItemIndex = item.getItemId();
    
    //handle editing
    if (menuItemIndex == Constants.MENU_ITEM_EDIT)
    {
      Log.d(TAG, "SHOULD START EDIT ACTIVITY HERE");
      String triggerMenuName = triggerArrayAdapter.getItem(info.position);
      int endIndex = triggerMenuName.indexOf(')', 1);
      int id = Integer.parseInt(triggerMenuName.substring(1, endIndex));

      //ask service for editing data
      Intent i = new Intent(getApplicationContext(),
          GoodVibrationsService.class);
      i.putExtra(Constants.INTENT_TYPE, Constants.GET_DATA);
      i.putExtra(Constants.INTENT_KEY_TYPE, Constants.INTENT_KEY_TRIGGER);
      i.putExtra(Constants.INTENT_KEY_EDITED_ID, id);
      startService(i);
    }
    
    //handle deleting
    else if (menuItemIndex == Constants.MENU_ITEM_DELETE)
    {
      Log.d(TAG, "SHOULD DELETE TRIGGER HERE");

      String triggerMenuName = triggerArrayAdapter.getItem(info.position);
      int endIndex = triggerMenuName.indexOf(')', 1);
      int id = Integer.parseInt(triggerMenuName.substring(1, endIndex));

      //tell service to delete trigger
      Intent i = new Intent(getApplicationContext(),
          GoodVibrationsService.class);
      i.putExtra(Constants.INTENT_TYPE, Constants.DELETE_TRIGGER);
      i.putExtra(Constants.INTENT_KEY_DELETED_ID, id);
      startService(i);
      onResume();

    }

    Log.d(TAG, "MENU ITEM NAME: " + triggerArrayAdapter.getItem(info.position));
    return true;
  }

  @Override
  //send data to service
  protected void onActivityResult(int requestCode, int resultCode, Intent data)
  {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == RESULT_OK)
    {
      Bundle b = data.getExtras();
      // Add name to the list of functions with a different format depending on
      // the function type
      if (b.getInt(Constants.INTENT_TYPE) == Constants.TRIGGER_TYPE) // Should
                                                                     // always
                                                                     // be true,
                                                                     // but just
                                                                     // double
                                                                     // checking
      {
        switch (b.getInt(Constants.INTENT_KEY_TYPE))
        {
          case Constants.TRIGGER_TYPE_TIME: //handle time triggers
            triggerArrayAdapter.add((b.getString(Constants.INTENT_KEY_NAME)
                + " S:" + b.getByte(Constants.INTENT_KEY_REPEAT_DAYS_BYTE)));
            break;

          case Constants.TRIGGER_TYPE_LOCATION: //handle location triggers
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
    // this method receives broadcast messages from GoodVibrationsService.java
    @Override
    public void onReceive(Context context, Intent intent)
    {
      Log.d(TAG, "RECIEVED BROADCAST MESSAGE");

      Bundle b = intent.getExtras();

      Log.d(TAG, "KEYNAME: " + b.getInt(Constants.INTENT_KEY_NAME) + " LIST: "
          + Constants.INTENT_KEY_TRIGGER_LIST);

      //handle getting the trigger list from the service
      if (b.getInt(Constants.INTENT_TYPE) == Constants.INTENT_KEY_TRIGGER_LIST)
      {
        triggerArrayAdapter.clear();
        int length = b.getInt(Constants.INTENT_KEY_DATA_LENGTH);
        String[] triggerNames = b
            .getStringArray(Constants.INTENT_KEY_TRIGGER_NAMES);
        int[] triggerIDs = b.getIntArray(Constants.INTENT_KEY_TRIGGER_IDS);
        Log.d(TAG, "LENGTH: " + length);
        for (int i = 0; i < length; i++)
        {
          triggerArrayAdapter
              .add("(" + triggerIDs[i] + ")  " + triggerNames[i]);
        }
      }
      //handle data needed to edit a trigger
      else if (b.getInt(Constants.INTENT_TYPE) == Constants.INTENT_KEY_TRIGGER)
      {
        //puts the data in an intent to send on to the location/time edit
        Intent triggerEditIntent = new Intent(getApplicationContext(),
            TriggerEditActivity.class);
        triggerEditIntent.putExtra(Constants.INTENT_KEY_EDITED_BOOL, true);
        triggerEditIntent.putExtra(Constants.INTENT_KEY_NAME,
            b.getString(Constants.INTENT_KEY_NAME));
        triggerEditIntent.putExtra(Constants.INTENT_KEY_EDITED_ID,
            b.getInt(Constants.INTENT_KEY_EDITED_ID));
        triggerEditIntent.putExtra(Constants.INTENT_KEY_PRIORITY,
            b.getInt(Constants.INTENT_KEY_PRIORITY));
        triggerEditIntent.putExtra(Constants.INTENT_KEY_FUNCTION_IDS,
            b.getIntArray(Constants.INTENT_KEY_FUNCTION_IDS));
        int triggerType = b.getInt(Constants.INTENT_KEY_TYPE);
        triggerEditIntent.putExtra(Constants.INTENT_KEY_TYPE, triggerType);
        
        //data for location trigger
        if (triggerType == Constants.TRIGGER_TYPE_LOCATION)
        {
          triggerEditIntent.putExtra(Constants.INTENT_KEY_LATITUDE,
              b.getDouble(Constants.INTENT_KEY_LATITUDE));
          triggerEditIntent.putExtra(Constants.INTENT_KEY_LONGITUDE,
              b.getDouble(Constants.INTENT_KEY_LONGITUDE));
          triggerEditIntent.setClass(getApplicationContext(),
              LocationTriggerEditActivity.class);
          Log.d(TAG, "Going to LocationTriggerEditActivity");
        }
        //data for time trigger
        else if (triggerType == Constants.TRIGGER_TYPE_TIME)
        {
          triggerEditIntent.putExtra(Constants.INTENT_KEY_REPEAT_DAYS_BYTE,
              b.getByte(Constants.INTENT_KEY_REPEAT_DAYS_BYTE));
          triggerEditIntent.putExtra(Constants.INTENT_KEY_START_TIME,
              b.getLong(Constants.INTENT_KEY_START_TIME));
          triggerEditIntent.putExtra(Constants.INTENT_KEY_END_TIME,
              b.getLong(Constants.INTENT_KEY_END_TIME));
          triggerEditIntent.setClass(getApplicationContext(),
              TimeTriggerEditActivity.class);
        }
        startActivityForResult(triggerEditIntent, 0);
      }
    }
  }

}