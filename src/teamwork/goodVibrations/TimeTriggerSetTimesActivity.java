package teamwork.goodVibrations;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

public class TimeTriggerSetTimesActivity extends Activity
{
  private static final String TAG = "TimeTriggerSetTimeActivity";
  Intent mIntent;

  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    Log.d(TAG, "onCreate()");
    setContentView(R.layout.set_times);

    mIntent = new Intent();

    Bundle b = getIntent().getExtras();
    mIntent.putExtra(Constants.INTENT_KEY_REPEAT_DAYS_BOOL,
        b.getBoolean(Constants.INTENT_KEY_REPEAT_DAYS_BOOL));
    mIntent.putExtra(Constants.INTENT_KEY_REPEAT_DAYS_BYTE,
        b.getByte(Constants.INTENT_KEY_REPEAT_DAYS_BYTE));
  }

  protected void onStart()
  {
    super.onStart();
    Log.d(TAG, "onStart()");

    final TimePicker startTimePicker = (TimePicker) findViewById(R.id.startPicker);
    final TimePicker endTimePicker = (TimePicker) findViewById(R.id.endPicker);

    Button repeatButton = (Button) findViewById(R.id.buttonRepeat);
    repeatButton.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View v)
      {
        Intent TimeTriggerDaysIntent = new Intent(getApplicationContext(),
            TimeTriggerSetDaysActivity.class);
        try
        {
          Bundle b = mIntent.getExtras();
          TimeTriggerDaysIntent.putExtra(Constants.INTENT_KEY_REPEAT_DAYS_BOOL,
              b.getBoolean(Constants.INTENT_KEY_REPEAT_DAYS_BOOL));
          TimeTriggerDaysIntent.putExtra(Constants.INTENT_KEY_REPEAT_DAYS_BYTE,
              b.getByte(Constants.INTENT_KEY_REPEAT_DAYS_BYTE));
        }
        catch (NullPointerException e)
        {
          // If we get a NullPointerException that means that this hasn't been
          // called so there is no data to be passed anyway.
        }
        startActivityForResult(TimeTriggerDaysIntent,
            Constants.REQUEST_CODE_DAY_PICKER);
      }
    });

    Button doneSetTimesButton = (Button) findViewById(R.id.buttonDoneSetTimes);
    doneSetTimesButton.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View v)
      {
        mIntent.putExtra(Constants.INTENT_KEY_START_TIME, Utils
            .calculateTimeInMillis(startTimePicker.getCurrentHour(),
                startTimePicker.getCurrentMinute()));
        mIntent.putExtra(Constants.INTENT_KEY_END_TIME, Utils
            .calculateTimeInMillis(endTimePicker.getCurrentHour(),
                endTimePicker.getCurrentMinute()));

        setResult(RESULT_OK, mIntent);
        finish(); // Return to TimeTriggerEditActivity.onActivityResult()
      }
    });
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data)
  {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == RESULT_OK)
    {
      if (requestCode == Constants.REQUEST_CODE_DAY_PICKER)
      {
        Log.d(TAG, "Daypicker Returned");
        Bundle b = data.getExtras();
        mIntent.putExtra(Constants.INTENT_KEY_REPEAT_DAYS_BYTE,
            b.getByte(Constants.INTENT_KEY_REPEAT_DAYS_BYTE));
        mIntent.putExtra(Constants.INTENT_KEY_REPEAT_DAYS_BOOL,
            b.getBoolean(Constants.INTENT_KEY_REPEAT_DAYS_BOOL));
      }
    }
    else
    {
      Log.d(TAG, "onActivityResult() failed");
    }
  }
}
