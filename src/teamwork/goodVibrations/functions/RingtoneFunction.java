package teamwork.goodVibrations.functions;

import teamwork.goodVibrations.Constants;
import android.content.ContentValues;
import android.content.Context;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

public class RingtoneFunction implements Function
{
  private static String TAG = "RingtoneFunction";
  
  public ContentValues values;
  private Uri mUri;
  private boolean vibrate;
  private AudioManager AM;
  Context mC;
    
  public RingtoneFunction(Context c, Bundle b)
  {
    Log.d(TAG,"RingtoneFunction() Constructor");
    
    mC = c;
    AM = (AudioManager) mC.getSystemService(Context.AUDIO_SERVICE);
    //Uri ruri = Uri.parse("");
    mUri = b.getParcelable(Constants.INTENT_KEY_URI);
    vibrate = b.getBoolean(Constants.INTENT_KEY_VIBRATE);
  }
  
  public void execute()
  {
    
    Log.d(TAG,"execute() - Setting Ringtone to " + mUri);
    
    //Toast.makeText(mC, "executing()", Toast.LENGTH_LONG).show();
    
    try
    {  
      //RingtoneManager.getRingtone(mC, mUri).play();
      //Log.d(TAG,"Ringtone playing");
      
      RingtoneManager.setActualDefaultRingtoneUri(mC, RingtoneManager.TYPE_RINGTONE, mUri);
    }
    catch (Exception e)
    {
      Log.d(TAG,"Error executing set ringtone");
      // error handling goes here -- also, use something other than Throwable 
    }
    
    if(vibrate)
    {
      AM.setVibrateSetting(AudioManager.STREAM_RING,AudioManager.VIBRATE_SETTING_ON);
    }
    else
    {
      AM.setVibrateSetting(AudioManager.STREAM_RING, AudioManager.VIBRATE_SETTING_OFF);
    }
    
    /*
    try
    {
      RingtoneManager.setActualDefaultRingtoneUri(c, RingtoneManager.TYPE_NOTIFICATION, nuri); 
    }
    catch (Throwable t)
    { 
      // error handling goes here -- also, use something other than Throwable 
    }
    */
    
    /*
    AudioManager AM = (AudioManager)c.getSystemService(Context.AUDIO_SERVICE); 
    AM.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
    for (int volumeType: (new int[] { AudioManager.STREAM_SYSTEM,AudioManager.STREAM_RING,AudioManager.STREAM_NOTIFICATION, AudioManager.STREAM_ALARM } ))
    { 
      //int maxVolume = AM.getStreamMaxVolume(volumeType);
      int vol = AM.getStreamVolume(AudioManager.STREAM_RING);
      AM.setStreamVolume(volumeType, vol, AudioManager.FLAG_PLAY_SOUND | AudioManager.FLAG_VIBRATE); 
      AM.setStreamMute(volumeType, false); 
      AM.setVibrateSetting(volumeType,AudioManager.VIBRATE_SETTING_ON);
    }
    */
  }

}
