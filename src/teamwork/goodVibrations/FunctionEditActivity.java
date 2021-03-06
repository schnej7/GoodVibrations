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
  final String[] wallpaperItems = new String[] { "Select Image" };
  private ArrayAdapter<String> adapter;
  AlertDialog dialog;
  AlertDialog.Builder builder;
  Intent mIntent = new Intent();
  Uri ringtone_uri;
  Uri imageUri;
  LinearLayout llVolumeOptions;
  LinearLayout llRingtoneOptions;
  LinearLayout llWallpaperOptions;
  int returnedFromImageSelector;
  private boolean beingEdited = false;
  private Bundle savedInstance;
  private int myId;
  EditText txtName;

  public void onCreate(Bundle savedInstanceState)
  {
    adapter = new ArrayAdapter<String>(this,
        android.R.layout.select_dialog_item, wallpaperItems);
    //Allows for the edit on wallpaper
    builder = new AlertDialog.Builder(this);
    super.onCreate(savedInstanceState);
    savedInstance = getIntent().getExtras();
    //Used for sending to the service whether to create a new function or
    //edit th eold one
    beingEdited = savedInstance.getBoolean(Constants.INTENT_KEY_EDITED_BOOL);
    if (beingEdited)
    {
      myId = savedInstance.getInt(Constants.INTENT_KEY_EDITED_ID);
    }
    Log.d(TAG, "onCreate()");
    setContentView(R.layout.function_edit_menu);
  }

  protected void onStart()
  {
    super.onStart();
    Log.d(TAG, "onStart()");

    llVolumeOptions = (LinearLayout) findViewById(R.id.llFunctionVolume);
    llRingtoneOptions = (LinearLayout) findViewById(R.id.llRingTone);
    llWallpaperOptions = (LinearLayout) findViewById(R.id.llWallpaper);
    String array_spinner[];
    array_spinner = new String[3];
    array_spinner[Constants.FUNCTION_TYPE_VOLUME] = "Volume";
    array_spinner[Constants.FUNCTION_TYPE_RINGTONE] = "Ringtone";
    array_spinner[Constants.FUNCTION_TYPE_WALLPAPER] = "Wallpaper";
    returnedFromImageSelector = mIntent.getIntExtra(
        Constants.INTENT_KEY_CALLED_IMAGE_SELECTOR, 0);

    txtName = (EditText) findViewById(R.id.editTextFunctionName);
    final Spinner spinnerType = (Spinner) findViewById(R.id.typeSelect);

    final SeekBar sliderVolume = (SeekBar) findViewById(R.id.skbarVolume);
    final CheckBox chkVolumeVibrate = (CheckBox) findViewById(R.id.chkVolumeVibrate);
    final CheckBox chkRingtoneVibrate = (CheckBox) findViewById(R.id.chkRingtoneVibrate);
    // Ringvolume Checkboxes
    final CheckBox chkRingtoneVolume = (CheckBox) findViewById(R.id.chkRingtoneVol);
    final CheckBox chkMediaVolume = (CheckBox) findViewById(R.id.chkMediaVol);
    final CheckBox chkAlarmVolume = (CheckBox) findViewById(R.id.chkAlarmVol);
    final CheckBox chkNotificationVolume = (CheckBox) findViewById(R.id.chkNotificationVol);
    // Ringtone Checkboxes
    final CheckBox chkRingtoneTone = (CheckBox) findViewById(R.id.chkRingtoneTone);
    final CheckBox chkAlarmTone = (CheckBox) findViewById(R.id.chkAlarmTone);
    final CheckBox chkNotificationTone = (CheckBox) findViewById(R.id.chkNotificationTone);

    //This spinner allows switching between function types
    ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,
        R.layout.spinner_list_item, array_spinner);
    spinnerType.setAdapter(spinnerAdapter);
    spinnerType
        .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
          public void onItemSelected(AdapterView<?> adapterView, View view,
              int i, long l)
          {
            //Saves the image if you switch off that selector item
            Log.d(TAG, "rfIS = " + returnedFromImageSelector);
            if (returnedFromImageSelector == 1)
            {
              // if the activity returned from an image selector, reset the case
              // and selected view thingy.
              i = 2;
              spinnerType.setSelection(2);
            }
            returnedFromImageSelector = 0;
            mIntent.putExtra(Constants.INTENT_KEY_CALLED_IMAGE_SELECTOR, 0);
            switch (i)
            {
              //Volume menu
              case 0:
                llVolumeOptions.setVisibility(View.VISIBLE);
                llRingtoneOptions.setVisibility(View.GONE);
                llWallpaperOptions.setVisibility(View.GONE);
                break;
              //Ringtone menu
              case 1:
                llVolumeOptions.setVisibility(View.GONE);
                llRingtoneOptions.setVisibility(View.VISIBLE);
                llWallpaperOptions.setVisibility(View.GONE);
                break;
              //Wallpaper menu
              case 2:
                llVolumeOptions.setVisibility(View.GONE);
                llRingtoneOptions.setVisibility(View.GONE);
                llWallpaperOptions.setVisibility(View.VISIBLE);
                break;
              default:
                llVolumeOptions.setVisibility(View.GONE);
                llRingtoneOptions.setVisibility(View.GONE);
                llWallpaperOptions.setVisibility(View.GONE);
                break;
            }
          }

          public void onNothingSelected(AdapterView<?> adapterView)
          {
            return;
          }
        });

    //Button to select what wallpaper to use
    final Button buttonSelectWallpaper = (Button) findViewById(R.id.btn_choose);
    buttonSelectWallpaper.setOnClickListener(new View.OnClickListener()
    {

      public void onClick(View v)
      {

        builder.setTitle("Select Image");
        builder.setAdapter(adapter, new DialogInterface.OnClickListener()
        {
          public void onClick(DialogInterface dialog, int item)
          {
            if (item == 0)
            {
              Intent intent = new Intent();

              intent.setType("image/*");
              intent.setAction(Intent.ACTION_GET_CONTENT);

              startActivityForResult(
                  Intent.createChooser(intent, "Complete action using"),
                  Constants.PICK_FROM_FILE);
            }
          }
        });

        dialog = builder.create();

        Button button = (Button) findViewById(R.id.btn_choose);
        // ImageView mImageView = (ImageView) findViewById(R.id.iv_photo);

        ((Button) findViewById(R.id.btn_choose))
            .setOnClickListener(new View.OnClickListener()
            {
              public void onClick(View v)
              {
                dialog.show();
              }
            });
        button.setOnClickListener(new View.OnClickListener()
        {
          public void onClick(View v)
          {
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
      // onActivityResult function below.
      public void onClick(View v)
      {
        Intent i = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
        i.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE,
            RingtoneManager.TYPE_ALL);
        startActivityForResult(i, Constants.REQUEST_CODE_RINGTONE_PICKER);
      }
    });

    //The done button (either done editing or done adding)
    final Button buttonAdd = (Button) findViewById(R.id.buttonDoneTriggerEdit);
    buttonAdd.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View v)
      {
        //Figure out what type it is
        int i = spinnerType.getSelectedItemPosition();
        mIntent.putExtra(Constants.INTENT_KEY_EDITED_BOOL, beingEdited);
        if (beingEdited)
        {
          mIntent.putExtra(Constants.INTENT_KEY_EDITED_ID, myId);
          Log.d(TAG, "PUTTING ID: " + myId);
        }
        mIntent.putExtra(Constants.INTENT_KEY_TYPE, i);
        mIntent.putExtra(Constants.INTENT_KEY_NAME, txtName.getText()
            .toString());
        mIntent.putExtra(Constants.INTENT_TYPE, Constants.FUNCTION_TYPE);
        switch (i)
        {
          //Put extras in for volume type
          case Constants.FUNCTION_TYPE_VOLUME:
            // Use the volume fields
            // Convert 0-99 volume to 0 to MaxVol
            float progress = (float) sliderVolume.getProgress();
            int volume = new Float(progress).intValue();
            Log.d(TAG, "PROGRESS: " + progress + " VOLUME: " + volume);
            mIntent.putExtra(Constants.INTENT_KEY_TYPE,
                Constants.FUNCTION_TYPE_VOLUME);
            mIntent.putExtra(Constants.INTENT_KEY_VOLUME, volume);
            mIntent.putExtra(Constants.INTENT_KEY_VIBRATE,
                chkVolumeVibrate.isChecked());

            byte volumeTypes = (byte) 0;
            volumeTypes |= Utils.booleanToByte(chkRingtoneVolume.isChecked());
            volumeTypes |= Utils.booleanToByte(chkMediaVolume.isChecked()) << 1;
            volumeTypes |= Utils.booleanToByte(chkAlarmVolume.isChecked()) << 2;
            volumeTypes |= Utils.booleanToByte(chkNotificationVolume
                .isChecked()) << 3;

            Log.d(TAG, "VOLUMETYPES " + volumeTypes);

            mIntent.putExtra(Constants.INTENT_KEY_VOLUME_TYPES, volumeTypes);

            break;
          //Put extras in for ringtone type
          case Constants.FUNCTION_TYPE_RINGTONE:
            // Use the ring tone fields
            mIntent.putExtra(Constants.INTENT_KEY_TYPE,
                Constants.FUNCTION_TYPE_RINGTONE);
            mIntent.putExtra(Constants.INTENT_KEY_URI, ringtone_uri);
            mIntent.putExtra(Constants.INTENT_KEY_VIBRATE,
                chkRingtoneVibrate.isChecked());

            byte toneTypes = (byte) 0;
            toneTypes |= Utils.booleanToByte(chkRingtoneTone.isChecked());
            toneTypes |= Utils.booleanToByte(chkAlarmTone.isChecked()) << 1;
            toneTypes |= Utils.booleanToByte(chkNotificationTone.isChecked()) << 2;
            mIntent.putExtra(Constants.INTENT_KEY_TONE_TYPES, toneTypes);

            break;
          //Put extras in for wallpaper type
          case Constants.FUNCTION_TYPE_WALLPAPER:
            mIntent.putExtra(Constants.INTENT_KEY_NAME, txtName.getText().toString());
            mIntent.putExtra(Constants.INTENT_KEY_IMAGEURI, imageUri);
            mIntent.putExtra(Constants.INTENT_KEY_CALLED_IMAGE_SELECTOR, 1);
            mIntent.putExtra(Constants.INTENT_KEY_TYPE,
                Constants.FUNCTION_TYPE_WALLPAPER);
            break;
          default:
            // Do nothing, this should never happen
            break;
        }
        setResult(RESULT_OK, mIntent);
        finish(); // Returns to FunctionDisplayActivity.onActivityResult()
      }
    });

    //Get the name if the function is being edited
    txtName.setText(savedInstance.getString(Constants.INTENT_KEY_NAME));
    Log.d(
        TAG,
        "INTENT_KEY_NAME: "
            + savedInstance.getString(Constants.INTENT_KEY_NAME));
    //Set the fields of the form based on the intent values
    switch (savedInstance.getInt(Constants.INTENT_KEY_TYPE))
    {
      //Volume case
      case Constants.FUNCTION_TYPE_VOLUME:
        spinnerType.setSelection(0);
        sliderVolume.setProgress(savedInstance
            .getInt(Constants.INTENT_KEY_VOLUME));
        chkVolumeVibrate.setChecked(savedInstance
            .getBoolean(Constants.INTENT_KEY_VIBRATE));
        byte volumeTypes = savedInstance
            .getByte(Constants.INTENT_KEY_VOLUME_TYPES);
        if ((volumeTypes & (byte) 1) != 0)
        {
          chkRingtoneVolume.setChecked(true);
        }
        if ((volumeTypes & (byte) 2) != 0)
        {
          chkMediaVolume.setChecked(true);
        }
        if ((volumeTypes & (byte) 4) != 0)
        {
          chkAlarmVolume.setChecked(true);
        }
        if ((volumeTypes & (byte) 8) != 0)
        {
          chkNotificationVolume.setChecked(true);
        }
        break;
      //Ringtone case
      case Constants.FUNCTION_TYPE_RINGTONE:
        spinnerType.setSelection(1);
        ringtone_uri = savedInstance.getParcelable(Constants.INTENT_KEY_URI);
        chkRingtoneVibrate.setChecked(savedInstance
            .getBoolean(Constants.INTENT_KEY_VIBRATE));
        byte toneTypes = savedInstance.getByte(Constants.INTENT_KEY_TONE_TYPES);
        if ((toneTypes & (byte) 1) != 0)
        {
          chkRingtoneTone.setChecked(true);
        }
        if ((toneTypes & (byte) 2) != 0)
        {
          chkAlarmTone.setChecked(true);
        }
        if ((toneTypes & (byte) 4) != 0)
        {
          chkNotificationTone.setChecked(true);
        }
        break;
      //Wallpaper case
      case Constants.FUNCTION_TYPE_WALLPAPER:
        spinnerType.setSelection(2);
        imageUri = savedInstance.getParcelable(Constants.INTENT_KEY_IMAGEURI);
        break;
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data)
  {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == RESULT_OK)
    {
      switch (requestCode)
      {
        //This is the result of picking an image
        case Constants.PICK_FROM_FILE:
          imageUri = data.getData();
          mIntent.putExtra(Constants.INTENT_KEY_CALLED_IMAGE_SELECTOR, 1);
          Toast.makeText(this, "" + imageUri, Toast.LENGTH_LONG).show();
          
          String temp =  mIntent.getExtras().getString(Constants.INTENT_KEY_NAME);
          txtName.setText(temp.toCharArray(), 0, temp.toCharArray().length);
          break;
        //This is the result of picking a ringtone
        case Constants.REQUEST_CODE_RINGTONE_PICKER:
          mIntent.putExtra(Constants.INTENT_KEY_CALLED_IMAGE_SELECTOR, 0);
          ringtone_uri = data
              .getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
          Log.d(TAG, "uri " + ringtone_uri);
          Toast.makeText(this, "" + ringtone_uri, Toast.LENGTH_LONG).show();
        default:
          break;
      }
    }
    //If picking a file failed...
    else
    {
      Log.d(TAG, "RESULT FAIL");
      Toast.makeText(this, "Fail", Toast.LENGTH_LONG).show();
    }
  }

}
