package teamwork.goodVibrations.functions;

import teamwork.goodVibrations.Constants;
import teamwork.goodVibrations.GoodVibrationsService;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

public class RingtoneFunction extends Function
{
  private static String TAG = "RingtoneFunction";

  private Uri mUri;
  private boolean vibrate;
  private AudioManager AM;
  private byte toneTypes;
  Context mC;

  // RingtoneFunction
  // Constructor for making ringtone functions through GUI
  public RingtoneFunction(Bundle b, int newID)
  {
    Log.d(TAG, "RingtoneFunction() Constructor");
    mC = GoodVibrationsService.c;
    AM = (AudioManager) GoodVibrationsService.c.getSystemService(Context.AUDIO_SERVICE);
    mUri = b.getParcelable(Constants.INTENT_KEY_URI);
    vibrate = b.getBoolean(Constants.INTENT_KEY_VIBRATE);
    name = b.getString(Constants.INTENT_KEY_NAME);
    toneTypes = b.getByte(Constants.INTENT_KEY_TONE_TYPES);
    id = newID;
    type = Function.FunctionType.RINGTONE;
  }
  
  // RingtoneFunction
  // Constructor for making ringtones from persistent storage
  public RingtoneFunction(String s)
  {
    mC = GoodVibrationsService.c;
    AM = (AudioManager) GoodVibrationsService.c.getSystemService(Context.AUDIO_SERVICE);
    type = Function.FunctionType.RINGTONE;
    String [] categories = s.split(Constants.CATEGORY_DELIM);
    name = categories[0];
    id = new Integer(categories[1]).intValue();
    mUri = Uri.parse(categories[2]);
    vibrate = new Boolean(categories[3]).booleanValue();
    toneTypes = new Byte(categories[4]).byteValue();
    type = Function.FunctionType.RINGTONE;
    
    Log.d(TAG,"NAME: " + name);
    Log.d(TAG,"ID: " + id);
  }

  // execute
  // Does the changing of the tones
  public RingtoneFunction execute()
  {
    Log.d(TAG, "execute() - Setting Ringtone to " + mUri);
    
    RingtoneFunction inverse = getInverse();
  
    // If the Ringtone was selected
    if((toneTypes & (byte)1) != 0)
    {
      RingtoneManager.setActualDefaultRingtoneUri(mC, RingtoneManager.TYPE_RINGTONE, mUri);
    }
    // If the Alarm tone was selected
    if((toneTypes & (byte)2) != 0)
    {
      RingtoneManager.setActualDefaultRingtoneUri(mC, RingtoneManager.TYPE_ALARM, mUri);
    }
    // If the notification tone was selected
    if((toneTypes & (byte)4) != 0)
    {
      RingtoneManager.setActualDefaultRingtoneUri(mC, RingtoneManager.TYPE_NOTIFICATION, mUri);
    }

    // If the vibrate checkbox was checked
    if(vibrate)
    {
      AM.setVibrateSetting(AudioManager.STREAM_RING, AudioManager.VIBRATE_SETTING_ON);
    }
    else
    {
      AM.setVibrateSetting(AudioManager.STREAM_RING, AudioManager.VIBRATE_SETTING_OFF);
    }
    
    return inverse;
  }
  
  private RingtoneFunction getInverse()
  {
    Bundle b = new Bundle();
        
    Uri ringtone = RingtoneManager.getActualDefaultRingtoneUri(mC, RingtoneManager.TYPE_RINGTONE);
    int currentVibrate = AM.getVibrateSetting(AudioManager.STREAM_RING);
    boolean currentVibrateBool = false;
    currentVibrateBool = (currentVibrate == AudioManager.VIBRATE_SETTING_ON);
    
    b.putParcelable(Constants.INTENT_KEY_URI, ringtone);
    b.putBoolean(Constants.INTENT_KEY_VIBRATE, currentVibrateBool);
    b.putString(Constants.INTENT_KEY_NAME, name + "inv");
    b.putByte(Constants.INTENT_KEY_TONE_TYPES, toneTypes);
    
    RingtoneFunction inverse = new RingtoneFunction(b,-1*id);
    return inverse;
  }

  // getInternalSaveString
  // Builds the string for persistent storage
  @Override
  public String getInternalSaveString()
  {
    String saveString = new String();
    saveString = name + Constants.CATEGORY_DELIM;
    saveString += id  + Constants.CATEGORY_DELIM;
    saveString += mUri.toString();
    saveString += Constants.CATEGORY_DELIM;    
    saveString += new Boolean(vibrate).toString();
    saveString += Constants.CATEGORY_DELIM; 
    saveString += new Byte(toneTypes);
    saveString += Constants.CATEGORY_DELIM;
    
    return saveString;
  }

  @Override
  public Intent getFunctionAsIntent()
  {
    Intent i = new Intent(Constants.SERVICE_DATA_FUNCTION_MESSAGE);
    
    i.putExtra(Constants.INTENT_KEY_TYPE, Constants.FUNCTION_TYPE_RINGTONE);
    i.putExtra(Constants.INTENT_KEY_URI, mUri);
    i.putExtra(Constants.INTENT_KEY_VIBRATE, vibrate);
    i.putExtra(Constants.INTENT_KEY_TONE_TYPES, toneTypes);
    
    return i;
  }

}
