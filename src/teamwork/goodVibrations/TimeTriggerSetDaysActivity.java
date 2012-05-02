package teamwork.goodVibrations;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

//class for setting the days to repeat the time trigger on
public class TimeTriggerSetDaysActivity extends Activity
{
  private static String TAG = "TimeTriggerSetDaysActivity";
  Intent mIntent = new Intent();

  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.daypicker);
    Log.d(TAG, "onCreate()");
  }

  protected void onStart()
  {
    super.onStart();
    Log.d(TAG, "onStart()");

    //done button
    final Button buttonDone = (Button) findViewById(R.id.doneSetTimes);
    //check boxes for days of the week
    final CheckBox chkSunday = (CheckBox) findViewById(R.id.chkSunday);
    final CheckBox chkMonday = (CheckBox) findViewById(R.id.chkMonday);
    final CheckBox chkTuesday = (CheckBox) findViewById(R.id.chkTuesday);
    final CheckBox chkWednesday = (CheckBox) findViewById(R.id.chkWednesday);
    final CheckBox chkThursday = (CheckBox) findViewById(R.id.chkThursday);
    final CheckBox chkFriday = (CheckBox) findViewById(R.id.chkFriday);
    final CheckBox chkSaturday = (CheckBox) findViewById(R.id.chkSaturday);
    
    try
    {
   // Look for data in the intent, which means we need to repopulate the fields
      Bundle b = getIntent().getExtras();
      long startTime = b.getLong(Constants.INTENT_KEY_START_TIME);
      long endTime = b.getLong(Constants.INTENT_KEY_END_TIME);
      Log.d(TAG, "StartTime: " + startTime);
      mIntent.putExtra(Constants.INTENT_KEY_START_TIME, startTime);
      mIntent.putExtra(Constants.INTENT_KEY_END_TIME, endTime);
      boolean repeat = b.getBoolean(Constants.INTENT_KEY_REPEAT_DAYS_BOOL);
      byte daysActive = b.getByte(Constants.INTENT_KEY_REPEAT_DAYS_BYTE);
      if ((Utils.getDayOfWeekBitMask(1) & daysActive) != 0)
        chkSunday.setChecked(true);
      if ((Utils.getDayOfWeekBitMask(2) & daysActive) != 0)
        chkMonday.setChecked(true);
      if ((Utils.getDayOfWeekBitMask(3) & daysActive) != 0)
        chkTuesday.setChecked(true);
      if ((Utils.getDayOfWeekBitMask(4) & daysActive) != 0)
        chkWednesday.setChecked(true);
      if ((Utils.getDayOfWeekBitMask(5) & daysActive) != 0)
        chkThursday.setChecked(true);
      if ((Utils.getDayOfWeekBitMask(6) & daysActive) != 0)
        chkFriday.setChecked(true);
      if ((Utils.getDayOfWeekBitMask(7) & daysActive) != 0)
        chkSaturday.setChecked(true);
    }
    catch (NullPointerException e)
    {
    }

    //handle clicking done button
    buttonDone.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View v)
      {
        //put checked days and whether to repeat weekly in the intent
        
        Log.d(TAG, "Running onClick()");
        byte days = 0;
        // days = (byte) (days | 1);
        if (chkSunday.isChecked())
          days |= Utils.getDayOfWeekBitMask(1);
        if (chkMonday.isChecked())
          days |= Utils.getDayOfWeekBitMask(2);
        if (chkTuesday.isChecked())
          days |= Utils.getDayOfWeekBitMask(3);
        if (chkWednesday.isChecked())
          days |= Utils.getDayOfWeekBitMask(4);
        if (chkThursday.isChecked())
          days |= Utils.getDayOfWeekBitMask(5);
        if (chkFriday.isChecked())
          days |= Utils.getDayOfWeekBitMask(6);
        if (chkSaturday.isChecked())
          days |= Utils.getDayOfWeekBitMask(7);
        mIntent.putExtra(Constants.INTENT_KEY_REPEAT_DAYS_BOOL, false);
        Log.d(TAG, "DAYS: " + days);
        mIntent.putExtra(Constants.INTENT_KEY_REPEAT_DAYS_BYTE, days);
        setResult(RESULT_OK, mIntent);
        finish(); // Returns to TimeTriggerSetTimesActivity.onActivityResult()
      }
    });
  }
}
