package teamwork.goodVibrations;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
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
		array_spinner[0]="Volume";
		array_spinner[1]="Ringtone";
		
		final LinearLayout llVolumeOptions = (LinearLayout) findViewById(R.id.llFunctionVolume);
		final LinearLayout llRingtoneOptions = (LinearLayout) findViewById(R.id.llRingTone);
		
		final Spinner spinnerType = (Spinner) findViewById(R.id.typeSelect);
		ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, R.layout.function_list_item, array_spinner);
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
	    buttonAdd.setOnClickListener(new View.OnClickListener() {
	        public void onClick(View v) {
	        	//TODO: add form data to the bundle
	        	mIntent.putExtra("one", "number one");
	    		setResult(RESULT_OK, mIntent);
	    		finish();
	        }
	    });
	}

}