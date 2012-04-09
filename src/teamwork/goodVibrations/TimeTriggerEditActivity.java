package teamwork.goodVibrations;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class TimeTriggerEditActivity extends Activity
{
  private static final String TAG = "TimeTriggerEditActivity";
  Intent mIntent;

  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    Log.d(TAG, "onCreate()");
    setContentView(R.layout.add_time_trigger);
    mIntent = new Intent();
    // Set defaults in case the user does not click on "Repeat?" button
    mIntent.putExtra(Constants.INTENT_KEY_REPEAT_DAYS_BOOL, false);
    mIntent.putExtra(Constants.INTENT_KEY_REPEAT_DAYS_BYTE, (byte) 0);
    // Set defaults in case user does not select start or end functions
    int[] emptyInts = new int[0];
    // mIntent.putExtra(Constants.INTENT_KEY_START_FUNCTION_IDS, emptyInts);
    // mIntent.putExtra(Constants.INTENT_KEY_STOP_FUNCTION_IDS, emptyInts);
    mIntent.putExtra(Constants.INTENT_KEY_FUNCTION_IDS, emptyInts);
  }

  protected void onStart()
  {
    super.onStart();
    Log.d(TAG, "onStart()");

    // set intent type to time trigger
    mIntent.putExtra(Constants.INTENT_TYPE, Constants.TRIGGER_TYPE);
    mIntent.putExtra(Constants.INTENT_KEY_TYPE, Constants.TRIGGER_TYPE_TIME);

    // name text box
    final EditText txtName = (EditText) findViewById(R.id.editTextTriggerName);
    // button to set times
    final Button buttonSetTimes = (Button) findViewById(R.id.buttonTimeTriggerSetTimes);
    buttonSetTimes.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View v)
      {
        Intent TimeTriggerSetTimesIntent = new Intent(getApplicationContext(),
            TimeTriggerSetTimesActivity.class);
        try
        {
          Bundle b = mIntent.getExtras();
          TimeTriggerSetTimesIntent.putExtra(
              Constants.INTENT_KEY_REPEAT_DAYS_BOOL,
              b.getBoolean(Constants.INTENT_KEY_REPEAT_DAYS_BOOL));
          TimeTriggerSetTimesIntent.putExtra(
              Constants.INTENT_KEY_REPEAT_DAYS_BYTE,
              b.getByte(Constants.INTENT_KEY_REPEAT_DAYS_BYTE));
        }
        catch (NullPointerException e)
        {
          // If we get a NullPointerException that means that this hasn't been
          // called so there is no data to be passed anyway.
        }

        startActivityForResult(TimeTriggerSetTimesIntent,
            Constants.REQUEST_CODE_SET_TIMES_ACTIVITY);
      }
    });

    final Button buttonSetFunction = (Button) findViewById(R.id.buttonTimeTriggerSetFunctions);
    buttonSetFunction.setOnClickListener(new View.OnClickListener()
    {

      public void onClick(View v)
      {
        // Add the selected functions to the bundle so they can be automatically
        // checked
        Intent TimeTriggerSetFunctions = new Intent(getApplicationContext(),
            SetFunctionsActivity.class);
        startActivityForResult(TimeTriggerSetFunctions,
            Constants.REQUEST_CODE_SET_FUNCTION_IDS);
      }
    });

    // final Button buttonSetFunctions =
    // (Button)findViewById(R.id.buttonTimeTriggerSetFunctions);
    final Button buttonDone = (Button) findViewById(R.id.buttonTimeTriggerDone);
    buttonDone.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View v)
      {
        // sets the name in the intent
        mIntent.putExtra(Constants.INTENT_KEY_NAME, txtName.getText()
            .toString());
        mIntent
            .putExtra(Constants.INTENT_KEY_TYPE, Constants.TRIGGER_TYPE_TIME);
        // start
        setResult(RESULT_OK, mIntent);
        finish(); // Returns to FunctionDisplayActivity.onActivityResult()
      }
    });
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data)
  {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == RESULT_OK)
    {
      Log.d(TAG, "onActivityResult()");
      // If the TimeTriggerSetTimesActivity was returned
      if (requestCode == Constants.REQUEST_CODE_SET_TIMES_ACTIVITY)
      {
        Bundle b = data.getExtras();
        mIntent.putExtra(Constants.INTENT_KEY_START_TIME,
            b.getLong(Constants.INTENT_KEY_START_TIME));
        mIntent.putExtra(Constants.INTENT_KEY_END_TIME,
            b.getLong(Constants.INTENT_KEY_END_TIME));
        // If there is also repeat days information
        mIntent.putExtra(Constants.INTENT_KEY_REPEAT_DAYS_BOOL,
            b.getBoolean(Constants.INTENT_KEY_REPEAT_DAYS_BOOL));
        mIntent.putExtra(Constants.INTENT_KEY_REPEAT_DAYS_BYTE,
            b.getByte(Constants.INTENT_KEY_REPEAT_DAYS_BYTE));
      }
      else if (requestCode == Constants.REQUEST_CODE_SET_FUNCTION_IDS)
      {
        Bundle b = data.getExtras();
        mIntent.putExtra(Constants.INTENT_KEY_FUNCTION_IDS,
            b.getIntArray(Constants.INTENT_KEY_FUNCTION_IDS));
      }
    }
    else
    {
      Log.d(TAG, "onActivityResult() Failed");
    }
  }
}
