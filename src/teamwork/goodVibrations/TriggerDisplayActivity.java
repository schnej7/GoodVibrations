package teamwork.goodVibrations;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class TriggerDisplayActivity extends Activity
{

  private ArrayAdapter<String> triggerArrayAdapter;
  private ListView listView;

  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.trigger_tab);

    triggerArrayAdapter = new ArrayAdapter<String>(this,
        R.layout.trigger_list_item);
    listView = (ListView) findViewById(R.id.listViewTriggers);
    listView.setAdapter(triggerArrayAdapter);

    final Button buttonAdd = (Button) findViewById(R.id.addTrigger);
    buttonAdd.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View v)
      {
        Intent TriggerEditIntent = new Intent(getApplicationContext(),
            TriggerEditActivity.class);
        startActivityForResult(TriggerEditIntent, 0);
      }
    });
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

}