package teamwork.goodVibrations;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class TimeTriggerEditActivity extends Activity
{
  private static final String TAG = "TimeTriggerEditActivity";
  Intent mIntent;
  
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    Log.d(TAG, "onCreate()");
    setContentView(R.layout.add_time_trigger);
  }
  
  protected void onStart()
  {
    super.onStart();
    Log.d(TAG, "onStart()");
    mIntent=new Intent();
    
    //set intent type to time trigger
    mIntent.putExtra(Constants.INTENT_TYPE, Constants.TRIGGER_TYPE);
    mIntent.putExtra(Constants.INTENT_KEY_TYPE, Constants.TRIGGER_TYPE_TIME);
    
    //name text box
    final EditText txtName = (EditText) findViewById(R.id.editTextTriggerName);
    //button to set times
    final Button buttonSetTimes = (Button)findViewById(R.id.buttonTimeTriggerSetTimes);
    buttonSetTimes.setOnClickListener(new View.OnClickListener()
    {
      
      public void onClick(View v)
      {
        Intent TimeTriggerSetTimesIntent = new Intent(getApplicationContext(), TimeTriggerSetTimesActivity.class);
        startActivityForResult(TimeTriggerSetTimesIntent,0);
      }
    });
    
    //final Button buttonSetFunctions = (Button)findViewById(R.id.buttonTimeTriggerSetFunctions);
    final Button buttonDone = (Button)findViewById(R.id.buttonTimeTriggerDone);
    buttonDone.setOnClickListener(new View.OnClickListener()
    {
      
      public void onClick(View v)
      {
        //sets the name in the intent
        mIntent.putExtra(Constants.INTENT_KEY_NAME, txtName.getText().toString());
        mIntent.putExtra(Constants.INTENT_KEY_TYPE, Constants.TRIGGER_TYPE_TIME);
        //start
        setResult(RESULT_OK, mIntent);
        finish();  // Returns to FunctionDisplayActivity.onActivityResult()
      }
    });  
  }
  
  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data){
    super.onActivityResult(requestCode, resultCode, data);
    if(resultCode==RESULT_OK)
    {
      // If the ring tone picker was returned
      Bundle b = data.getExtras();
      mIntent.putExtra(Constants.INTENT_KEY_START_TIME, b.getLong(Constants.INTENT_KEY_START_TIME));
      mIntent.putExtra(Constants.INTENT_KEY_END_TIME, b.getLong(Constants.INTENT_KEY_END_TIME));
      if(b.getBoolean(Constants.INTENT_KEY_REPEAT_DAYS_BOOL)){
        //If there is also repeat days information
        mIntent.putExtra(Constants.INTENT_KEY_REPEAT_DAYS_BOOL, b.getBoolean(Constants.INTENT_KEY_REPEAT_DAYS_BOOL));
        mIntent.putExtra(Constants.INTENT_KEY_REPEAT_DAYS_BYTE, b.getByte(Constants.INTENT_KEY_REPEAT_DAYS_BYTE));
      }
    }
    else
    {
      Log.d(TAG, "RINGTONE RESULT FAIL");
      Toast.makeText(this, "Ringtone Fail", Toast.LENGTH_LONG).show();
    }
  }
}




