package teamwork.goodVibrations;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class TriggerEditActivity extends Activity
{
  private static final String TAG  = "TriggerEditActivity";
  
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    Log.d(TAG, "onCreate()");
    setContentView(R.layout.add_trigger);
  }
  
  protected void onStart()
  {
    super.onStart();
    Log.d(TAG, "onStart()");
    final Button buttonAddTimeTrigger = (Button) findViewById(R.id.buttonAddTimeTrigger);
    buttonAddTimeTrigger.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View v)
      {
        Intent TimeTriggerEditIntent = new Intent(getApplicationContext(), TimeTriggerEditActivity.class);
        startActivityForResult(TimeTriggerEditIntent,Constants.REQUEST_CODE_TIME);
      }
    });
    
    final Button buttonAddLocationTrigger = (Button) findViewById(R.id.buttonAddLocTrigger);
    buttonAddLocationTrigger.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View v)
      {
        Intent LocationTriggerEditIntent = new Intent(getApplicationContext(), LocationTriggerEditActivity.class);
        startActivityForResult(LocationTriggerEditIntent,Constants.REQUEST_CODE_LOCATION);
      }
    });
  }
  
  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data)
  {
    super.onActivityResult(requestCode, resultCode, data);
    Log.d(TAG,"onActivityResult()");
    if(resultCode==RESULT_OK)
    {
      Bundle b = data.getExtras();
      switch(requestCode)
      {
        case Constants.REQUEST_CODE_TIME:
          Log.d(TAG,"ST: " + b.getLong(Constants.INTENT_KEY_START_TIME));
          break;
        
        case Constants.REQUEST_CODE_LOCATION:
          
          break;
      }
      // If the ring tone picker was returned
      setResult(RESULT_OK, data);
      finish();  // Returns to FunctionDisplayActivity.onActivityResult()
    }
    else
    {
      Log.d(TAG, "onActivityResult Failed");
    }
  }
}
