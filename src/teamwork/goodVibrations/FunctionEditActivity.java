package teamwork.goodVibrations;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
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

public class FunctionEditActivity extends Activity{
	
	private static final String TAG = "FunctionEditActivity";
	Intent mIntent;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate()");
		setContentView(R.layout.function_edit_menu);
	}
	
	protected void onStart(){
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
		final CheckBox chkVibrate = (CheckBox) findViewById(R.id.chkVibrate);
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
		
		final Button buttonAdd = (Button) findViewById(R.id.buttonDoneTriggerEdit);
	    buttonAdd.setOnClickListener(new View.OnClickListener()
	    {
	        public void onClick(View v)
	        {
	        	int i = spinnerType.getSelectedItemPosition();
	        	mIntent.putExtra(Constants.INTENT_KEY_TYPE, i);
	        	mIntent.putExtra(Constants.INTENT_KEY_NAME, txtName.getText().toString());
	        	switch(i)
	        	{
  		        case 0:
  		        	// Use the volume fields
  		          // Convert 0-99 volume to 0 to MaxVol
  		          float progress = (float)sliderVolume.getProgress();
  		          float maxVol = (float)((AudioManager)getBaseContext().getSystemService(Context.AUDIO_SERVICE)).getStreamMaxVolume(AudioManager.STREAM_RING);
  		          int volume = (int) Math.round(maxVol*(progress/100.0));
  		        	mIntent.putExtra(Constants.INTENT_KEY_VOLUME, volume);
  		        	mIntent.putExtra(Constants.INTENT_KEY_VIBRATE, chkVibrate.isChecked());
  		        	break;
  		        case 1:
  		        	//Use the tone fields
  		          mIntent.putExtra(Constants.INTENT_KEY_TYPE, Constants.FUNCTION_TYPE_RINGTONE);
  		        	break;
  		        default:
  		        	//Do nothing, this should never happen
  		        	break;
		        }
	        	setResult(RESULT_OK, mIntent);
	        	finish();
	        }
	    });
	}
}
