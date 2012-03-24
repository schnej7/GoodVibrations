package teamwork.goodVibrations.functions;

import android.media.AudioManager;
import android.util.Log;

public class LowerFunction implements Function {

	private AudioManager AM;
	
	public void execute() {
		AM.adjustStreamVolume(AudioManager.STREAM_RING, AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
		Log.d("vorsth","Subtraction Function ");
	}
	
	public LowerFunction(AudioManager am)
	{
		AM = am;
	}	
}
