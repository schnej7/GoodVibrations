package teamwork.goodVibrations.functions;

import teamwork.goodVibrations.Constants;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
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
    vibrate = b.getBoolean(Constants.INTENT_KEY_VIBRATE);
    name = b.getString(Constants.INTENT_KEY_NAME);
    AM = tAM;
    id = newID;
  }
  
  @Override
  public void execute()
  {
    AM.setStreamVolume(AudioManager.STREAM_RING, volume, AudioManager.FLAG_SHOW_UI);
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
