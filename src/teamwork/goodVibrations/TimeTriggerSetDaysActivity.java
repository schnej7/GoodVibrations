package teamwork.goodVibrations;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

public class TimeTriggerSetDaysActivity extends Activity
{
  Intent mIntent = new Intent();

  protected void onStart(){
    final Button buttonDone = (Button)findViewById(R.id.buttonTimeTriggerDone);
    final CheckBox chkSunday = (CheckBox) findViewById(R.id.chkSunday);
    final CheckBox chkMonday = (CheckBox) findViewById(R.id.chkMonday);
    final CheckBox chkTuesday = (CheckBox) findViewById(R.id.chkTuesday);
    final CheckBox chkWednesday = (CheckBox) findViewById(R.id.chkWednesday);
    final CheckBox chkThursday = (CheckBox) findViewById(R.id.chkThursday);
    final CheckBox chkFriday = (CheckBox) findViewById(R.id.chkFriday);
    final CheckBox chkSaturday = (CheckBox) findViewById(R.id.chkSaturday);
    
    buttonDone.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View v)
      {
        byte days = 0;
        //days = (byte) (days | 1);
        if (chkSunday.isChecked()) days |= Utils.getDayOfWeekBitMask(1);
        if (chkMonday.isChecked()) days |= Utils.getDayOfWeekBitMask(2);
        if (chkTuesday.isChecked()) days |= Utils.getDayOfWeekBitMask(3);
        if (chkWednesday.isChecked()) days |= Utils.getDayOfWeekBitMask(4);
        if (chkThursday.isChecked()) days |= Utils.getDayOfWeekBitMask(5);
        if (chkFriday.isChecked()) days |= Utils.getDayOfWeekBitMask(6);
        if (chkSaturday.isChecked()) days |= Utils.getDayOfWeekBitMask(7);
        mIntent.putExtra(Constants.INTENT_KEY_REPEAT_DAYS_BYTE, days);
        setResult(RESULT_OK, mIntent);
        finish();  // Returns to FunctionDisplayActivity.onActivityResult()
      }
    }); 
  }
}
