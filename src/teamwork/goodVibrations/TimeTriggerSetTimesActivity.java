package teamwork.goodVibrations;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TimePicker;
import android.widget.Toast;

public class TimeTriggerSetTimesActivity extends Activity
{

  
  private static final String TAG = "TimeTriggerSetTimeActivity";
  Intent mIntent;
  
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    Log.d(TAG, "onCreate()");
    setContentView(R.layout.set_times);
  }
  
  protected void onStart()
  {
    super.onStart();
    Log.d(TAG, "onStart()");
    mIntent=new Intent();
    
   //time pickers here

  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data){
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
