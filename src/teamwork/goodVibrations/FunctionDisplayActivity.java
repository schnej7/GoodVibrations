package teamwork.goodVibrations;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class FunctionDisplayActivity extends Activity
{
  private static String TAG = "FunctionDisplayActivity";

  private ArrayAdapter<String> functionArrayAdapter;
  private ListView listView;
  private DataReceiver dataReceiver;

  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.function_tab);

    functionArrayAdapter = new ArrayAdapter<String>(this,
        R.layout.function_list_item);
    listView = (ListView) findViewById(R.id.listViewFunctions);
    listView.setAdapter(functionArrayAdapter);

    registerForContextMenu(listView);

    final Button buttonAdd = (Button) findViewById(R.id.addFunction);
    buttonAdd.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View v)
      {
        Intent functionEditIntent = new Intent(getApplicationContext(),
            FunctionEditActivity.class);
        functionEditIntent.putExtra(Constants.INTENT_KEY_EDITED_BOOL, false);
        startActivityForResult(functionEditIntent, 0);
      }
    });

    IntentFilter messageFilter;
    messageFilter = new IntentFilter(Constants.SERVICE_MESSAGE);
    messageFilter.addAction(Constants.SERVICE_MESSAGE);
    dataReceiver = new DataReceiver();
    registerReceiver(dataReceiver, messageFilter);
  }

  public void onResume()
  {
    super.onResume();
    Intent i = new Intent(getApplicationContext(), GoodVibrationsService.class);
    i.putExtra(Constants.INTENT_TYPE, Constants.GET_DATA);
    i.putExtra(Constants.INTENT_KEY_TYPE, Constants.INTENT_KEY_FUNCTION_LIST);
    startService(i);
  }

  public void onDestroy()
  {
    super.onDestroy();
    unregisterReceiver(dataReceiver);
  }

  @Override
  public void onCreateContextMenu(ContextMenu menu, View v,
      ContextMenuInfo menuInfo)
  {
    AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
    menu.setHeaderTitle(functionArrayAdapter.getItem(info.position));
    menu.add(Menu.NONE, Constants.MENU_ITEM_EDIT, Menu.NONE, "Edit"); // TODO
                                                                      // The
                                                                      // strings
                                                                      // should
                                                                      // be
                                                                      // resources
    menu.add(Menu.NONE, Constants.MENU_ITEM_DELETE, Menu.NONE, "Delete");
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
      String functionMenuName = functionArrayAdapter.getItem(info.position);
      int endIndex = functionMenuName.indexOf(')', 1);
      int id = Integer.parseInt(functionMenuName.substring(1, endIndex));

      Intent i = new Intent(getApplicationContext(),
          GoodVibrationsService.class);
      i.putExtra(Constants.INTENT_TYPE, Constants.GET_DATA);
      i.putExtra(Constants.INTENT_KEY_TYPE, Constants.INTENT_KEY_FUNCTION);
      i.putExtra(Constants.INTENT_KEY_EDITED_ID, id);
      startService(i);

    }
    else if (menuItemIndex == Constants.MENU_ITEM_DELETE)
    {
      Log.d(TAG, "SHOULD DELETE TRIGGER HERE");

      String functionMenuName = functionArrayAdapter.getItem(info.position);
      int endIndex = functionMenuName.indexOf(')', 1);
      int id = Integer.parseInt(functionMenuName.substring(1, endIndex));

      Intent i = new Intent(getApplicationContext(),
          GoodVibrationsService.class);
      i.putExtra(Constants.INTENT_TYPE, Constants.DELETE_FUNCTION);
      i.putExtra(Constants.INTENT_KEY_DELETED_ID, id);
      startService(i);
      onResume();
    }

    Log.d(TAG, "MENU ITEM NAME: " + functionArrayAdapter.getItem(info.position));
    return true;
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data)
  {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == RESULT_OK)
    {
      Bundle b = data.getExtras();
      // Should always be true but just to double check
      if (b.getInt(Constants.INTENT_TYPE) == Constants.FUNCTION_TYPE)
      {
        // Add name to the list of functions with a different format depending
        // on the function type
        switch (b.getInt(Constants.INTENT_KEY_TYPE))
        {
          case Constants.FUNCTION_TYPE_VOLUME:
            functionArrayAdapter.add(b.getString(Constants.INTENT_KEY_NAME)
                + "  Vol: " + b.getInt(Constants.INTENT_KEY_VOLUME));
            break;

          case Constants.FUNCTION_TYPE_RINGTONE:
            functionArrayAdapter.add(b.getString(Constants.INTENT_KEY_NAME)
                + "  Tone: " + b.getParcelable(Constants.INTENT_KEY_URI));
            break;
        }
      }

      // Create the intent that gets sent to the service
      data.setClass(this, GoodVibrationsService.class);
      startService(data); // Calls GoodVibrationsService.onStartCommand()
      onResume();
    }
    else
    {
      Toast.makeText(this, "Fail", Toast.LENGTH_LONG).show();
    }
  }

  public class DataReceiver extends BroadcastReceiver
  {
    // this method receives broadcast messages.
    @Override
    public void onReceive(Context context, Intent intent)
    {
      Log.d(TAG, "RECIEVED BROADCAST MESSAGE");

      Bundle b = intent.getExtras();

      if (b.getInt(Constants.INTENT_TYPE) == Constants.INTENT_KEY_FUNCTION_LIST)
      {
        functionArrayAdapter.clear();
        int length = b.getInt(Constants.INTENT_KEY_DATA_LENGTH);
        String[] functionNames = b
            .getStringArray(Constants.INTENT_KEY_FUNCTION_NAMES);
        int[] functionIDs = b.getIntArray(Constants.INTENT_KEY_FUNCTION_IDS);
        for (int i = 0; i < length; i++)
        {
          functionArrayAdapter.add("(" + functionIDs[i] + ")  "
              + functionNames[i]);
        }
      }
      
      if(b.getInt(Constants.INTENT_TYPE)== Constants.INTENT_KEY_FUNCTION)
      {
        Log.d(TAG, "Got INTENT_LEY_FUCNTION");
        Intent functionEditIntent = new Intent(getApplicationContext(),FunctionEditActivity.class);
        functionEditIntent.putExtra(Constants.INTENT_KEY_EDITED_BOOL, true);
        functionEditIntent.putExtra(Constants.INTENT_KEY_NAME, b.getString(Constants.INTENT_KEY_NAME));
        functionEditIntent.putExtra(Constants.INTENT_KEY_EDITED_ID,b.getInt(Constants.INTENT_KEY_EDITED_ID));
        int functionType = b.getInt(Constants.INTENT_KEY_TYPE);
        functionEditIntent.putExtra(Constants.INTENT_KEY_TYPE, functionType);
        if( functionType == Constants.FUNCTION_TYPE_VOLUME ){
          functionEditIntent.putExtra(Constants.INTENT_KEY_VOLUME, b.getInt(Constants.INTENT_KEY_VOLUME));
          functionEditIntent.putExtra(Constants.INTENT_KEY_VIBRATE, b.getBoolean(Constants.INTENT_KEY_VIBRATE));
          functionEditIntent.putExtra(Constants.INTENT_KEY_VOLUME_TYPES, b.getByte(Constants.INTENT_KEY_VOLUME_TYPES));
        }
        else if( functionType == Constants.FUNCTION_TYPE_RINGTONE ){
          functionEditIntent.putExtra(Constants.INTENT_KEY_URI, b.getParcelable(Constants.INTENT_KEY_URI));
          functionEditIntent.putExtra(Constants.INTENT_KEY_VIBRATE, b.getBoolean(Constants.INTENT_KEY_VIBRATE));
          functionEditIntent.putExtra(Constants.INTENT_KEY_TONE_TYPES, b.getByte(Constants.INTENT_KEY_TONE_TYPES));
        }
        
        else if( functionType == Constants.FUNCTION_TYPE_RINGTONE ){
          functionEditIntent.putExtra(Constants.INTENT_KEY_IMAGEURI, b.getParcelable(Constants.INTENT_KEY_IMAGEURI));
        }
        
        startActivityForResult(functionEditIntent, 0);
        
      }
    }
  }

}