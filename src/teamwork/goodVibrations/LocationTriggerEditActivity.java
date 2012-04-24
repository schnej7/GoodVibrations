package teamwork.goodVibrations;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LocationTriggerEditActivity extends Activity
{
  private static final String TAG = "LocationTriggerEditActivity";
  Intent mIntent;

  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    Log.d(TAG, "onCreate()");
    mIntent = new Intent();
    setContentView(R.layout.add_location_trigger);
  }

  protected void onStart()
  {
    super.onStart();
    Log.d(TAG, "onStart()");

    // set intent type to time trigger
    mIntent.putExtra(Constants.INTENT_TYPE, Constants.TRIGGER_TYPE);
    mIntent.putExtra(Constants.INTENT_KEY_TYPE, Constants.TRIGGER_TYPE_LOCATION);

    // name text box
    final EditText txtName = (EditText) findViewById(R.id.editTextTriggerName);
    // priority text box
    final EditText txtPriority = (EditText) findViewById(R.id.editTextPriority);
    
    // button to set times
    final Button buttonSetLocation = (Button) findViewById(R.id.buttonLocationTriggerSetLocation);
    buttonSetLocation.setOnClickListener(new View.OnClickListener()
    {

      public void onClick(View v)
      {
        // START THE MAPS API TO GET THE RESULT
        Log.d(TAG, "STARTING MAPS API TO GET LOCATION");
        Intent LocationTriggerSetLocationIntent = new Intent(getApplicationContext(), MapSelector.class);
        startActivityForResult(LocationTriggerSetLocationIntent, Constants.REQUEST_CODE_LOCATION);
      }
    });

    final Button buttonSetFunctions = (Button) findViewById(R.id.buttonLocationTriggerSetFunctions);
    buttonSetFunctions.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View v)
      {
        // Add the selected functions to the bundle so they can be automatically
        // checked
        Intent LocationTriggerSetFunctionsIntent = new Intent(getApplicationContext(), SetFunctionsActivity.class);
        try
        {
          Bundle b = mIntent.getExtras();
          LocationTriggerSetFunctionsIntent.putExtra(Constants.INTENT_KEY_FUNCTION_IDS, b.getIntArray(Constants.INTENT_KEY_FUNCTION_IDS));
        }
        catch(NullPointerException e)
        {
          // If we get a NullPointerException that means that this hasn't been
          // called so there is no data to be passed anyway.
        }
        startActivityForResult(LocationTriggerSetFunctionsIntent, Constants.REQUEST_CODE_SET_FUNCTION_IDS);
      }
    });

    final Button buttonDone = (Button) findViewById(R.id.buttonLocationTriggerDone);
    buttonDone.setOnClickListener(new View.OnClickListener()
    {

      public void onClick(View v)
      {
        // sets the name in the intent
        mIntent.putExtra(Constants.INTENT_KEY_NAME, txtName.getText().toString());
        mIntent.putExtra(Constants.INTENT_KEY_TYPE, Constants.TRIGGER_TYPE_LOCATION);
        // mIntent.putExtra(Constants.INTENT_KEY_RADIUS, 50);
        // Check the priority value to make sure it is a number
        try
        {
          String p = txtPriority.getText().toString();
          int priorityInt = new Integer(p).intValue();
          mIntent.putExtra(Constants.INTENT_KEY_PRIORITY, priorityInt);
        }
        catch(Exception e)
        {
          mIntent.putExtra(Constants.INTENT_KEY_PRIORITY, 1);
        }
        // start
        setResult(RESULT_OK, mIntent);
        finish(); // Returns to TriggerDisplayActivity.onActivityResult()
      }
    });
    
    Bundle b = getIntent().getExtras();
    boolean beingEdited = b.getBoolean(Constants.INTENT_KEY_EDITED_BOOL);
    mIntent.putExtra(Constants.INTENT_KEY_EDITED_BOOL, beingEdited);
    if(beingEdited){
      txtName.setText(b.getString(Constants.INTENT_KEY_NAME));
      mIntent.putExtra(Constants.INTENT_KEY_NAME, b.getString(Constants.INTENT_KEY_NAME));
      txtPriority.setText(new Integer(b.getInt(Constants.INTENT_KEY_PRIORITY)).toString());
      mIntent.putExtra(Constants.INTENT_KEY_PRIORITY, b.getInt(Constants.INTENT_KEY_PRIORITY));
      mIntent.putExtra(Constants.INTENT_KEY_LATITUDE, b.getDouble(Constants.INTENT_KEY_LATITUDE));
      mIntent.putExtra(Constants.INTENT_KEY_LONGITUDE, b.getDouble(Constants.INTENT_KEY_LONGITUDE));
      mIntent.putExtra(Constants.INTENT_KEY_FUNCTION_IDS, b.getIntArray(Constants.INTENT_KEY_FUNCTION_IDS));
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data)
  {
    super.onActivityResult(requestCode, resultCode, data);
    if(resultCode == RESULT_OK)
    {
      // received location from map activity
      Log.d(TAG, "Running onClick()");
      Bundle b = data.getExtras();
      if(requestCode == Constants.REQUEST_CODE_LOCATION)
      {
        mIntent.putExtra(Constants.INTENT_KEY_LATITUDE, b.getDouble(Constants.INTENT_KEY_LATITUDE));
        mIntent.putExtra(Constants.INTENT_KEY_LONGITUDE, b.getDouble(Constants.INTENT_KEY_LONGITUDE));
      }
      else if(requestCode == Constants.REQUEST_CODE_SET_FUNCTION_IDS)
      {
        mIntent.putExtra(Constants.INTENT_KEY_FUNCTION_IDS, b.getIntArray(Constants.INTENT_KEY_FUNCTION_IDS));
      }
    }
    else
    {
      Log.d(TAG, "RINGTONE RESULT FAIL");
      Toast.makeText(this, "Ringtone Fail", Toast.LENGTH_LONG).show();
    }
  }
}
