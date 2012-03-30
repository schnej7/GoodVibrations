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

public class FunctionEditActivity extends Activity{
	
	private static final String TAG = "FunctionEditActivity";
	Intent mIntent;
	Uri ringtone_uri;
  
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate()");
		setContentView(R.layout.function_edit_menu);
	}
	
	protected void onStart()
	{
		super.onStart();
		Log.d(TAG, "onStart()");
		mIntent = new Intent();
		
		String array_spinner[];
		array_spinner=new String[2];
		array_spinner[Constants.FUNCTION_TYPE_VOLUME]="Volume";
		array_spinner[Constants.FUNCTION_TYPE_RINGTONE]="Ringtone";
		
		final LinearLayout llVolumeOptions = (LinearLayout) findViewById(R.id.llFunctionVolume);
		final LinearLayout llRingtoneOptions = (LinearLayout) findViewById(R.id.llRingTone);
		
		final SeekBar sliderVolume = (SeekBar) findViewById(R.id.skbarVolume);
		final CheckBox chkVolumeVibrate = (CheckBox) findViewById(R.id.chkVolumeVibrate);
		final CheckBox chkRingtoneVibrate = (CheckBox) findViewById(R.id.chkRingtoneVibrate);
		final EditText txtName = (EditText) findViewById(R.id.editTextFunctionName);
		
		final Spinner spinnerType = (Spinner) findViewById(R.id.typeSelect);
		ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, R.layout.spinner_list_item, array_spinner);
		spinnerType.setAdapter(spinnerAdapter);
		spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
			public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) { 
		        switch(i){
		        case 0:
		        	llVolumeOptions.setVisibility(View.VISIBLE);
		        	llRingtoneOptions.setVisibility(View.GONE);
		        	break;
		        case 1:
		        	llVolumeOptions.setVisibility(View.GONE);
		        	llRingtoneOptions.setVisibility(View.VISIBLE);
		        	break;
		        default:
		        	llVolumeOptions.setVisibility(View.GONE);
		        	llRingtoneOptions.setVisibility(View.GONE);
		        	break;
		        }
		    } 

		    public void onNothingSelected(AdapterView<?> adapterView) {
		        return;
		    }
		});
		
		// The Select Ringtone button
		final Button buttonSelectRingtone = (Button)findViewById(R.id.buttonSelectRingtone);
		buttonSelectRingtone.setOnClickListener( new View.OnClickListener()
    {
		  // Start the ringtone picker on click and then get the result in the onActivityResult funciton below.
      public void onClick(View v)
      {
        Intent i = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
        startActivityForResult(i, Constants.REQUEST_CODE_RINGTONE_PICKER); 
      }
    });
		
		final Button buttonAdd = (Button) findViewById(R.id.buttonDoneTriggerEdit);
    buttonAdd.setOnClickListener(new View.OnClickListener()
    {
        public void onClick(View v)
        {
        	int i = spinnerType.getSelectedItemPosition();
        	mIntent.putExtra(Constants.INTENT_KEY_TYPE, i);
        	mIntent.putExtra(Constants.INTENT_KEY_NAME, txtName.getText().toString());
        	mIntent.putExtra(Constants.INTENT_TYPE, Constants.FUNCTION_TYPE);
        	switch(i)
        	{
		        case Constants.FUNCTION_TYPE_VOLUME:
		        	// Use the volume fields
		          // Convert 0-99 volume to 0 to MaxVol
		          float progress = (float)sliderVolume.getProgress();
		          float maxVol = (float)((AudioManager)getBaseContext().getSystemService(Context.AUDIO_SERVICE)).getStreamMaxVolume(AudioManager.STREAM_RING);
		          int volume = (int) Math.round(maxVol*(progress/100.0));
		          mIntent.putExtra(Constants.INTENT_KEY_TYPE, Constants.FUNCTION_TYPE_VOLUME);
		        	mIntent.putExtra(Constants.INTENT_KEY_VOLUME, volume);
		        	mIntent.putExtra(Constants.INTENT_KEY_VIBRATE, chkVolumeVibrate.isChecked());
		        	break;
		        case Constants.FUNCTION_TYPE_RINGTONE:
		        	//Use the ring tone fields
		          mIntent.putExtra(Constants.INTENT_KEY_TYPE, Constants.FUNCTION_TYPE_RINGTONE);
		          mIntent.putExtra(Constants.INTENT_KEY_URI, ringtone_uri);
		          mIntent.putExtra(Constants.INTENT_KEY_VIBRATE, chkRingtoneVibrate.isChecked());
		        	break;
		        default:
		        	//Do nothing, this should never happen
		        	break;
	        }
        	setResult(RESULT_OK, mIntent);
        	finish();  // Returns to FunctionDisplayActivity.onActivityResult()
        }
    });
	}
	

	
	
  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data)
  {
    super.onActivityResult(requestCode, resultCode, data);
    if(resultCode==RESULT_OK)
    {
      // If the ringtone picker was returned
      if(requestCode == Constants.REQUEST_CODE_RINGTONE_PICKER)
      {
        ringtone_uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI); 
        Log.d(TAG, "uri " + ringtone_uri);
        Toast.makeText(this, "" + ringtone_uri, Toast.LENGTH_LONG).show(); 
      }
    }
    else
    {
      Log.d(TAG, "RINGTONE RESULT FAIL");
      Toast.makeText(this, "Ringtone Fail", Toast.LENGTH_LONG).show();
    }
  }
	
}
