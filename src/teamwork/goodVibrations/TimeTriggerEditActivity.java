package teamwork.goodVibrations;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
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
    
    //name text box
    final EditText txtName = (EditText) findViewById(R.id.editTextTriggerName);
    //button to set times
    final Button buttonSetTimes = (Button)findViewById(R.id.buttonTimeTriggerSetTimes);
    buttonSetTimes.setOnClickListener(new View.
    //final Button buttonSetFunctions = (Button)findViewById(R.id.buttonTimeTriggerSetFunctions);
    final Button buttonDone = (Button)findViewById(R.id.buttonTimeTriggerDone);
    
    
    
    
    
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




