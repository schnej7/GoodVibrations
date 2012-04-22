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

    triggerArrayAdapter = new ArrayAdapter<String>(this, R.layout.trigger_list_item);
    listView = (ListView) findViewById(R.id.listViewTriggers);
    listView.setAdapter(triggerArrayAdapter);
    
    registerForContextMenu(listView);
    
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
    messageFilter = new IntentFilter(Constants.SERVICE_DATA_TRIGGER_LIST_MESSAGE);
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
  public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo)
  {
      AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
      menu.setHeaderTitle(triggerArrayAdapter.getItem(info.position));
      menu.add(Menu.NONE,Constants.MENU_ITEM_EDIT,Menu.NONE,"Edit");    // TODO The strings should be resources
      menu.add(Menu.NONE,Constants.MENU_ITEM_DELETE,Menu.NONE,"Delete");
  }

  @Override
  public boolean onContextItemSelected(MenuItem item)
  {
    AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
        .getMenuInfo();
    int menuItemIndex = item.getItemId();
    if (menuItemIndex == Constants.MENU_ITEM_EDIT)
    {
      Log.d(TAG, "SHOULD START EDIT ACTIVITY HERE");
    }
    else if (menuItemIndex == Constants.MENU_ITEM_DELETE)
    {
      Log.d(TAG, "SHOULD DELETE TRIGGER HERE");

      String triggerMenuName = triggerArrayAdapter.getItem(info.position);
      int endIndex = triggerMenuName.indexOf(')', 1);
      int id = Integer.parseInt(triggerMenuName.substring(1, endIndex));

      Intent i = new Intent(getApplicationContext(),
          GoodVibrationsService.class);
      i.putExtra(Constants.INTENT_TYPE, Constants.DELETE_TRIGGER);
      i.putExtra(Constants.INTENT_KEY_DELETED_ID, id);
      startService(i);
      onResume();

    }

    Log.d(TAG,"MENU ITEM NAME: " + triggerArrayAdapter.getItem(info.position));
    return true;
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data)
  {
    super.onActivityResult(requestCode, resultCode, data);
    if(resultCode == RESULT_OK)
    {
      Bundle b = data.getExtras();
      // Add name to the list of functions with a different format depending on
      // the function type
      if(b.getInt(Constants.INTENT_TYPE) == Constants.TRIGGER_TYPE) // Should always be true, but just double checking
      {
        switch(b.getInt(Constants.INTENT_KEY_TYPE))
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
    // this method receives broadcast messages from GoodVibrationsService.java
    @Override
    public void onReceive(Context context, Intent intent)
    {
      Log.d(TAG, "RECIEVED BROADCAST MESSAGE");

      Bundle b = intent.getExtras();

      Log.d(TAG, "KEYNAME: " + b.getInt(Constants.INTENT_KEY_NAME) + " LIST: " + Constants.INTENT_KEY_TRIGGER_LIST);

      if(b.getInt(Constants.INTENT_TYPE) == Constants.INTENT_KEY_TRIGGER_LIST)
      {
        triggerArrayAdapter.clear();
        int length = b.getInt(Constants.INTENT_KEY_DATA_LENGTH);
        String[] triggerNames = b.getStringArray(Constants.INTENT_KEY_TRIGGER_NAMES);
        int[] triggerIDs = b.getIntArray(Constants.INTENT_KEY_TRIGGER_IDS);
        Log.d(TAG, "LENGTH: " + length);
        for(int i = 0; i < length; i++)
        {
          triggerArrayAdapter.add("(" + triggerIDs[i] + ")  " + triggerNames[i]);
        }
      }
    }
  }

}