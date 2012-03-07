package teamwork.goodVibrations;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class GoodVibrationsService extends Service
{
	private TriggerQueue q;

	@Override
	public void onCreate()
	{
		q = new TriggerQueue(loadTriggers());
	}
	@Override
	public void onStart(Intent intent, int id)
	{
		q.initialize();
		
		for(int i = 0; i < 10; i++)
		{
			Log.d("Croftd2", q.pop().toString());
			try
			{
				Thread.sleep(q.getSleepTimeMilli());
			}
			catch(InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}
	private Trigger[] loadTriggers()
	{
		//TODO
		return new Trigger[] {new Trigger("pie 4000", 4000), new Trigger("pie 7000", 7000)};
	}
	@Override
	public IBinder onBind(Intent arg0)
	{
		return null;
	}
}
