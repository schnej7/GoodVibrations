package teamwork.goodVibrations.functions;

import android.media.AudioManager;
import android.util.Log;

public class LowerFunction extends Function
{
  private AudioManager AM;

  public LowerFunction(AudioManager am, int newID, String tName)
  {
    AM = am;
    id = newID;
    name = tName;
    type = Function.FunctionType.LOWER;
  }

  public void execute()
  {
    AM.adjustStreamVolume(AudioManager.STREAM_RING, AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
    Log.d("vorsth", "Subtraction Function ");
  }

  @Override
  public String getInternalSaveString()
  {
    // TODO Auto-generated method stub
    return null;
  }

}
