package teamwork.goodVibrations.functions;

import android.media.AudioManager;
import android.util.Log;

public class RaiseFunction extends Function
{

  private AudioManager AM;

  public RaiseFunction(AudioManager am, int newID, String tName)
  {
    AM = am;
    id = newID;
    name = tName;
    type = Function.FunctionType.RAISE;
  }

  public void execute()
  {
    AM.adjustStreamVolume(AudioManager.STREAM_RING, AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
    Log.d("vorsth", "Addtion Function ");
  }

  @Override
  public String getInternalSaveString()
  {
    // TODO Auto-generated method stub
    return null;
  }
}
