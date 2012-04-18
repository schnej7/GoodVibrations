package teamwork.goodVibrations;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

public class FunctionEditActivity extends Activity
{

  private static final String TAG = "FunctionEditActivity";
  final String [] wallpaperItems = new String [] {"Select from Gallery", "Select from File"};
  private ArrayAdapter<String> adapter;
  AlertDialog dialog;
  AlertDialog.Builder builder;
  Intent mIntent;
  Uri ringtone_uri;
  Uri imageUri;
  //final String [] items = new String [] {"Select from Wallpapers", "Select from Gallery"};

  public void onCreate(Bundle savedInstanceState)
  {
    adapter = new ArrayAdapter<String> (this, android.R.layout.select_dialog_item, wallpaperItems);
    builder = new AlertDialog.Builder(this);
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
    array_spinner = new String[3];
    array_spinner[Constants.FUNCTION_TYPE_VOLUME] = "Volume";
    array_spinner[Constants.FUNCTION_TYPE_RINGTONE] = "Ringtone";
    array_spinner[Constants.FUNCTION_TYPE_WALLPAPER] = "Wallpaper";

    final LinearLayout llVolumeOptions = (LinearLayout) findViewById(R.id.llFunctionVolume);
    final LinearLayout llRingtoneOptions = (LinearLayout) findViewById(R.id.llRingTone);
    final LinearLayout llWallpaperOptions = (LinearLayout) findViewById(R.id.llWallpaper);

    final SeekBar sliderVolume = (SeekBar) findViewById(R.id.skbarVolume);
    final CheckBox chkVolumeVibrate = (CheckBox) findViewById(R.id.chkVolumeVibrate);
    final CheckBox chkRingtoneVibrate = (CheckBox) findViewById(R.id.chkRingtoneVibrate);
    final EditText txtName = (EditText) findViewById(R.id.editTextFunctionName);
    // Ringvolume Checkboxes
    final CheckBox chkRingtoneVolume = (CheckBox) findViewById(R.id.chkRingtoneVol);
    final CheckBox chkMediaVolume = (CheckBox) findViewById(R.id.chkMediaVol);
    final CheckBox chkAlarmVolume = (CheckBox) findViewById(R.id.chkAlarmVol);
    final CheckBox chkNotificationVolume = (CheckBox) findViewById(R.id.chkNotificationVol);
    // Ringtone Checkboxes
    final CheckBox chkRingtoneTone = (CheckBox) findViewById(R.id.chkRingtoneTone);
    final CheckBox chkAlarmTone = (CheckBox) findViewById(R.id.chkAlarmTone);
    final CheckBox chkNotificationTone = (CheckBox) findViewById(R.id.chkNotificationTone);

    final Spinner spinnerType = (Spinner) findViewById(R.id.typeSelect);
    ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, R.layout.spinner_list_item, array_spinner);
    spinnerType.setAdapter(spinnerAdapter);
    spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
    {
      public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
      {
        switch(i)
        {
          case 0:
            llVolumeOptions.setVisibility(View.VISIBLE);
            llRingtoneOptions.setVisibility(View.GONE);
            llWallpaperOptions.setVisibility(View.GONE);
            break;
          case 1:
            llVolumeOptions.setVisibility(View.GONE);
            llRingtoneOptions.setVisibility(View.VISIBLE);
            llWallpaperOptions.setVisibility(View.GONE);
            break;
          case 2:
            llVolumeOptions.setVisibility(View.GONE);
            llRingtoneOptions.setVisibility(View.GONE);
            llWallpaperOptions.setVisibility(View.VISIBLE);
          default:
            llVolumeOptions.setVisibility(View.GONE);
            llRingtoneOptions.setVisibility(View.GONE);
            //This is weird, IDK whats up with this...
            llWallpaperOptions.setVisibility(View.VISIBLE);
            break;
        }
      }

      public void onNothingSelected(AdapterView<?> adapterView)
      {
        return;
      }
    });
    
    final Button buttonSelectWallpaper = (Button) findViewById(R.id.btn_choose);
    buttonSelectWallpaper.setOnClickListener(new View.OnClickListener()
    {

      public void onClick(View v)
      {
        
        
        builder.setTitle("Select Image");
        builder.setAdapter( adapter, new DialogInterface.OnClickListener() {
           public void onClick( DialogInterface dialog, int item ) { 
             if (item == 0) {
               Intent intent = new Intent();
             
               intent.setType("image/*");
               intent.setAction(Intent.ACTION_GET_CONTENT);
       
               startActivityForResult(Intent.createChooser(intent, "Complete action using"), Constants.PICK_FROM_FILE); 
             } 
           }
        } );
        
       dialog = builder.create();
       
       Button button   = (Button) findViewById(R.id.btn_choose);
       //ImageView mImageView  = (ImageView) findViewById(R.id.iv_photo);
       
       ((Button) findViewById(R.id.btn_choose)).setOnClickListener(new View.OnClickListener() {
           public void onClick(View v) {
               dialog.show();
           }
       });
       button.setOnClickListener(new View.OnClickListener() {
         public void onClick(View v) {
          dialog.show();
         }
      });

      }
      
    });

    // The Select Ringtone button
    final Button buttonSelectRingtone = (Button) findViewById(R.id.buttonSelectRingtone);
    buttonSelectRingtone.setOnClickListener(new View.OnClickListener()
    {
      // Start the ringtone picker on click and then get the result in the
      // onActivityResult funciton below.
      public void onClick(View v)
      {
        Intent i = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
        i.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE,RingtoneManager.TYPE_ALL);
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
            float progress = (float) sliderVolume.getProgress();
            int volume = new Float(progress).intValue();
            Log.d(TAG,"PROGRESS: " + progress + " VOLUME: " + volume);
            mIntent.putExtra(Constants.INTENT_KEY_TYPE, Constants.FUNCTION_TYPE_VOLUME);
            mIntent.putExtra(Constants.INTENT_KEY_VOLUME, volume);
            mIntent.putExtra(Constants.INTENT_KEY_VIBRATE, chkVolumeVibrate.isChecked());
            
            byte volumeTypes = (byte)0;
            volumeTypes |= Utils.booleanToByte(chkRingtoneVolume.isChecked());
            volumeTypes |= Utils.booleanToByte(chkMediaVolume.isChecked()) << 1;
            volumeTypes |= Utils.booleanToByte(chkAlarmVolume.isChecked()) << 2;
            volumeTypes |= Utils.booleanToByte(chkNotificationVolume.isChecked()) << 3;
            
            Log.d(TAG,"VOLUMETYPES " + volumeTypes);
            
            mIntent.putExtra(Constants.INTENT_KEY_VOLUME_TYPES, volumeTypes);
            
            break;
          case Constants.FUNCTION_TYPE_RINGTONE:
            // Use the ring tone fields
            mIntent.putExtra(Constants.INTENT_KEY_TYPE, Constants.FUNCTION_TYPE_RINGTONE);
            mIntent.putExtra(Constants.INTENT_KEY_URI, ringtone_uri);
            mIntent.putExtra(Constants.INTENT_KEY_VIBRATE, chkRingtoneVibrate.isChecked());
            
            byte toneTypes = (byte)0;
            toneTypes |= Utils.booleanToByte(chkRingtoneTone.isChecked());
            toneTypes |= Utils.booleanToByte(chkAlarmTone.isChecked()) << 1;
            toneTypes |= Utils.booleanToByte(chkNotificationTone.isChecked()) << 2;
            mIntent.putExtra(Constants.INTENT_KEY_TONE_TYPES, toneTypes);
            
            break;
          case Constants.FUNCTION_TYPE_WALLPAPER:
            mIntent.putExtra(Constants.INTENT_KEY_IMAGEURI, imageUri);
            break;
          default:
            // Do nothing, this should never happen
            break;
        }
        setResult(RESULT_OK, mIntent);
        finish(); // Returns to FunctionDisplayActivity.onActivityResult()
      }
    });
  }


  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data)
  {
    super.onActivityResult(requestCode, resultCode, data);
    if(resultCode == RESULT_OK)
    {
      switch (requestCode) {
        case Constants.PICK_FROM_FILE:
          //This is witchcraft... I have no idea...
          imageUri = data.getData();
          Toast.makeText(this, "" + imageUri, Toast.LENGTH_LONG).show();
          break;
        case Constants.REQUEST_CODE_RINGTONE_PICKER:
          ringtone_uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
          Log.d(TAG, "uri " + ringtone_uri);
          Toast.makeText(this, "" + ringtone_uri, Toast.LENGTH_LONG).show();
        default:
          break;
       }
    }
    else
    {
      Log.d(TAG, "RINGTONE RESULT FAIL");
      Toast.makeText(this, "Ringtone Fail", Toast.LENGTH_LONG).show();
    }
  }

}
