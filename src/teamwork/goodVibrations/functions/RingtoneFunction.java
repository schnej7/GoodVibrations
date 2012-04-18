package teamwork.goodVibrations.functions;

import teamwork.goodVibrations.Constants;
import android.content.Context;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class RingtoneFunction extends Function
{
  private static String TAG = "RingtoneFunction";

  private Uri mUri;
  private boolean vibrate;
  private AudioManager AM;
  private byte toneTypes;
  Context mC;

  public RingtoneFunction(Context c, Bundle b, int newID)
  {
    Log.d(TAG, "RingtoneFunction() Constructor");
    mC = c;
    AM = (AudioManager) mC.getSystemService(Context.AUDIO_SERVICE);
    mUri = b.getParcelable(Constants.INTENT_KEY_URI);
    vibrate = b.getBoolean(Constants.INTENT_KEY_VIBRATE);
    name = b.getString(Constants.INTENT_KEY_NAME);
    toneTypes = b.getByte(Constants.INTENT_KEY_TONE_TYPES);
    id = newID;
    type = Function.FunctionType.RINGTONE;
  }
  
  public RingtoneFunction(Context c, String s)
  {
    // TODO Redo string parsing
    mC = c;
    type = Function.FunctionType.RINGTONE;
    String [] categories = s.split(Constants.CATEGORY_DELIM);
    name = categories[0];
    id = new Integer(categories[1]).intValue();
    mUri = Uri.parse(categories[2]);
    vibrate = new Boolean(categories[3]).booleanValue();
    toneTypes = new Byte(categories[4]).byteValue();
    type = Function.FunctionType.RINGTONE;
  }

  public void execute()
  {

    Log.d(TAG, "execute() - Setting Ringtone to " + mUri);

    // Toast.makeText(mC, "executing()", Toast.LENGTH_LONG).show();

    try
    {
      // RingtoneManager.getRingtone(mC, mUri).play();
      // Log.d(TAG,"Ringtone playing");
      if((toneTypes & (byte)1) != 0)
      {
        RingtoneManager.setActualDefaultRingtoneUri(mC, RingtoneManager.TYPE_RINGTONE, mUri);
      }
      if((toneTypes & (byte)2) != 0)
      {
        RingtoneManager.setActualDefaultRingtoneUri(mC, RingtoneManager.TYPE_ALARM, mUri);
      }
      if((toneTypes & (byte)4) != 0)
      {
        RingtoneManager.setActualDefaultRingtoneUri(mC, RingtoneManager.TYPE_NOTIFICATION, mUri);
      }
    }
    catch(Exception e)
    {
      Log.d(TAG, "Error executing set ringtone");
      // error handling goes here -- also, use something other than Throwable
    }

    if(vibrate)
    {
      AM.setVibrateSetting(AudioManager.STREAM_RING, AudioManager.VIBRATE_SETTING_ON);
    }
    else
    {
      AM.setVibrateSetting(AudioManager.STREAM_RING, AudioManager.VIBRATE_SETTING_OFF);
    }

  }

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
    
    return saveString;
  }

}
