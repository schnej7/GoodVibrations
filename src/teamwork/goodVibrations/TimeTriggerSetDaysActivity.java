package teamwork.goodVibrations;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class TimeTriggerSetDaysActivity extends Activity
{
  private static final String TAG = "TimeTriggerSetDaysActivity";

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
