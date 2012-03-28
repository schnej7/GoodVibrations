package teamwork.goodVibrations;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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
        startActivityForResult(TimeTriggerEditIntent,0);
      }
    });
  }
  
  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data)
  {
    super.onActivityResult(requestCode, resultCode, data);
    if(resultCode==RESULT_OK)
    {
      // If the ring tone picker was returned
      setResult(RESULT_OK, data);
      finish();  // Returns to FunctionDisplayActivity.onActivityResult()
    }
    else
    {
      Log.d(TAG, "RINGTONE RESULT FAIL");
      Toast.makeText(this, "Ringtone Fail", Toast.LENGTH_LONG).show();
    }
  }
}
