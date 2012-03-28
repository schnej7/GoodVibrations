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
        if (chkSunday.isChecked()) days |= 
        setResult(RESULT_OK, mIntent);
        finish();  // Returns to FunctionDisplayActivity.onActivityResult()
      }
    }); 
  }
}
