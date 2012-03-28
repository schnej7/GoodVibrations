package teamwork.goodVibrations;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class TimeTriggerSetDaysActivity extends Activity
{
  private static final String TAG = "TimeTriggerSetDaysActivity";
  Intent mIntent;

  protected void onStart(){
    final Button buttonDone = (Button)findViewById(R.id.buttonTimeTriggerDone);
    buttonDone.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View v)
      {
        //sets the name in the intent
        mIntent.putExtra(Constants.INTENT_KEY_NAME, txtName.getText().toString());
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
    }
    else
    {
      Log.d(TAG, "RINGTONE RESULT FAIL");
      Toast.makeText(this, "Ringtone Fail", Toast.LENGTH_LONG).show();
    }
  }
}
