package teamwork.goodVibrations.functions;

import teamwork.goodVibrations.Constants;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;

public class SetVolumeFunction extends Function
{

  private static String TAG = "SetVolumeFunction";

  private AudioManager AM;
  private int volume;
  private boolean vibrate;

  public SetVolumeFunction(AudioManager tAM, Bundle b,int newID)
  {
    volume = b.getInt(Constants.INTENT_KEY_VOLUME);
    Log.d(TAG,"Volume: " + volume);
    vibrate = b.getBoolean(Constants.INTENT_KEY_VIBRATE);
    name = b.getString(Constants.INTENT_KEY_NAME);
    AM = tAM;
    id = newID;
  }
  
  @Override
  public void execute()
  {
    AM.setStreamVolume(AudioManager.STREAM_RING, volume, AudioManager.FLAG_SHOW_UI | AudioManager.FLAG_ALLOW_RINGER_MODES | AudioManager.FLAG_VIBRATE);
    if(volume > 0)
    {
      AM.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
    }
    if(volume == 0 && vibrate)
    {
      AM.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
    }
    /*
    // TODO Implement a silent setting
    if(volume == 0 && silent)
    {
      AM.setRingerMode(AudioManager.RINGER_MODE_SILENT);
    }
    */
    if(vibrate)
    {
      AM.setVibrateSetting(AudioManager.STREAM_RING,AudioManager.VIBRATE_SETTING_ON);
    }
    else
    {
      AM.setVibrateSetting(AudioManager.STREAM_RING, AudioManager.VIBRATE_SETTING_OFF);
    }

    Log.d(TAG,"execute() - Setting to " + volume);
  }

}
