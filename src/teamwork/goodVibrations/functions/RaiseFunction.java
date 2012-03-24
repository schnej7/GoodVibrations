package teamwork.goodVibrations.functions;

import android.media.AudioManager;
import android.util.Log;

public class RaiseFunction implements Function {

	private AudioManager AM;
	
	public void execute()
	{
      AM.adjustStreamVolume(AudioManager.STREAM_RING, AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
      Log.d("vorsth","Addtion Function ");
	}
	
	public RaiseFunction(AudioManager am)
	{
		AM = am;
	}

}
