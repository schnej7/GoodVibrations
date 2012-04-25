package teamwork.goodVibrations;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

//Activity for setting the start and end times of a time trigger
public class TimeTriggerSetTimesActivity extends Activity
{
  private static final String TAG = "TimeTriggerSetTimeActivity";
  Intent mIntent = new Intent();
  private TimePicker startTimePicker;
  private TimePicker endTimePicker;

  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    Log.d(TAG, "onCreate()");
    setContentView(R.layout.set_times); //set correct UI

    Bundle b = getIntent().getExtras();
    mIntent.putExtra(Constants.INTENT_KEY_REPEAT_DAYS_BOOL,
        b.getBoolean(Constants.INTENT_KEY_REPEAT_DAYS_BOOL));
    mIntent.putExtra(Constants.INTENT_KEY_REPEAT_DAYS_BYTE,
        b.getByte(Constants.INTENT_KEY_REPEAT_DAYS_BYTE));

    startTimePicker = (TimePicker) findViewById(R.id.startPicker);
    endTimePicker = (TimePicker) findViewById(R.id.endPicker);

  }

  protected void onStart()
  {
    super.onStart();
    Log.d(TAG, "onStart()");

    //repeat button which opens the day picker
    Button repeatButton = (Button) findViewById(R.id.buttonRepeat);
    repeatButton.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View v)
      {
        Intent TimeTriggerDaysIntent = new Intent(getApplicationContext(),
            TimeTriggerSetDaysActivity.class);
        //have we been here before (ie editing)? - if so pass data on
        try
        {
          Bundle b = mIntent.getExtras();
          TimeTriggerDaysIntent.putExtra(Constants.INTENT_KEY_REPEAT_DAYS_BOOL,
              b.getBoolean(Constants.INTENT_KEY_REPEAT_DAYS_BOOL));
          TimeTriggerDaysIntent.putExtra(Constants.INTENT_KEY_REPEAT_DAYS_BYTE,
              b.getByte(Constants.INTENT_KEY_REPEAT_DAYS_BYTE));
          TimeTriggerDaysIntent.putExtra(Constants.INTENT_KEY_START_TIME, Utils
              .calculateTimeInMillis(startTimePicker.getCurrentHour(),
                  startTimePicker.getCurrentMinute()));
          TimeTriggerDaysIntent.putExtra(Constants.INTENT_KEY_END_TIME, Utils
              .calculateTimeInMillis(endTimePicker.getCurrentHour(),
                  endTimePicker.getCurrentMinute()));
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

    //done button
    Button doneSetTimesButton = (Button) findViewById(R.id.buttonDoneSetTimes);
    doneSetTimesButton.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View v)
      {
        //puts the start and end times in the intent
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

    // Look for bundle with time data in it - used for repopulating/editing
    try
    {
      Log.d(TAG, "Trying to get intent");
      Bundle b = getIntent().getExtras();
      long startTime = b.getLong(Constants.INTENT_KEY_START_TIME);
      long endTime = b.getLong(Constants.INTENT_KEY_END_TIME);
      startTimePicker.setCurrentHour(Utils.getHoursFromMillis(startTime));
      Log.d(TAG, "Start hour: " + Utils.getHoursFromMillis(startTime));
      startTimePicker.setCurrentMinute(Utils.getMinutesFromMillis(startTime));
      endTimePicker.setCurrentHour(Utils.getHoursFromMillis(endTime));
      endTimePicker.setCurrentMinute(Utils.getMinutesFromMillis(endTime));
      Log.d(TAG, "GOT INTENT");
    }
    catch (NullPointerException e)
    {
      //haven't been here before - no data to repopulate
      startTimePicker.setCurrentHour(Utils.getHoursFromMillis(Utils
          .getTimeOfDayInMillis()));
      startTimePicker.setCurrentHour(Utils.getMinutesFromMillis(Utils
          .getTimeOfDayInMillis()));
      endTimePicker.setCurrentHour(Utils.getHoursFromMillis(Utils
          .getTimeOfDayInMillis()));
      endTimePicker.setCurrentHour(Utils.getMinutesFromMillis(Utils
          .getTimeOfDayInMillis()));
      Log.d(TAG, "No bundle to set to");
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data)
  {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == RESULT_OK)
    {
      //handle data from repeat day picker
      if (requestCode == Constants.REQUEST_CODE_DAY_PICKER)
      {
        Log.d(TAG, "Daypicker Returned");
        Bundle b = data.getExtras();
        mIntent.putExtra(Constants.INTENT_KEY_REPEAT_DAYS_BYTE,
            b.getByte(Constants.INTENT_KEY_REPEAT_DAYS_BYTE));
        mIntent.putExtra(Constants.INTENT_KEY_REPEAT_DAYS_BOOL,
            b.getBoolean(Constants.INTENT_KEY_REPEAT_DAYS_BOOL));
        long startTime = b.getLong(Constants.INTENT_KEY_START_TIME);
        long endTime = b.getLong(Constants.INTENT_KEY_END_TIME);
        mIntent.putExtra(Constants.INTENT_KEY_START_TIME, startTime);
        mIntent.putExtra(Constants.INTENT_KEY_END_TIME, endTime);
        getIntent().putExtra(Constants.INTENT_KEY_START_TIME, startTime);
        getIntent().putExtra(Constants.INTENT_KEY_END_TIME, endTime);
      }
    }
    else
    {
      Log.d(TAG, "onActivityResult() failed");
    }
  }
}
