package teamwork.goodVibrations;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.location.Location;

public class LocationTriggerEditActivity extends Activity
{
  private static final String TAG = "LocationTriggerEditActivity";
  Intent mIntent = new Intent();
  
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    Log.d(TAG, "onCreate()");
    setContentView(R.layout.add_location_trigger);
  }
  
  protected void onStart()
  {
    super.onStart();
    Log.d(TAG, "onStart()"); 
    
    //set intent type to time trigger
    mIntent.putExtra(Constants.INTENT_TYPE, Constants.TRIGGER_TYPE);
    mIntent.putExtra(Constants.INTENT_KEY_TYPE, Constants.TRIGGER_TYPE_LOCATION);
    
    //name text box
    final EditText txtName = (EditText) findViewById(R.id.editTextTriggerName);
    //button to set times
    final Button buttonSetLocation = (Button)findViewById(R.id.buttonLocationTriggerSetLocation);
    buttonSetLocation.setOnClickListener(new View.OnClickListener()
    {
      
      public void onClick(View v)
      {
        // START THE MAPS API TO GET THE RESULT
        Log.d(TAG,"STARTING MAPS API TO GET LOCATION");
        Intent LocationTriggerSetLocationIntent = new Intent(getApplicationContext(), MapSelector.class);
        startActivityForResult(LocationTriggerSetLocationIntent,Constants.REQUEST_CODE_LOCATION);
      }
    });
    
    final Button buttonSetFunctions = (Button)findViewById(R.id.buttonLocationTriggerSetFunctions);
    buttonSetFunctions.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View v)
      {
        // Add the selected functions to the bundle so they can be automatically checked
        Intent TimeTriggerSetFunctions = new Intent(getApplicationContext(), TimeTriggerSetFunctionsActivity.class);
        startActivityForResult(TimeTriggerSetFunctions,Constants.REQUEST_CODE_SET_FUNCTION_IDS);
      }
    });
    
    
    final Button buttonDone = (Button)findViewById(R.id.buttonLocationTriggerDone);
    buttonDone.setOnClickListener(new View.OnClickListener()
    {
      
      public void onClick(View v)
      {
        //sets the name in the intent
        mIntent.putExtra(Constants.INTENT_KEY_NAME, txtName.getText().toString());
        mIntent.putExtra(Constants.INTENT_KEY_TYPE, Constants.TRIGGER_TYPE_LOCATION);
        //start
        setResult(RESULT_OK, mIntent);
        finish();  // Returns to TriggerDisplayActivity.onActivityResult()
      }
    });  
  }
  
  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data){
    super.onActivityResult(requestCode, resultCode, data);
    if(resultCode==RESULT_OK)
    {
      //received location from map activity
      Log.d(TAG,"Running onClick()");
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




