package teamwork.goodVibrations;

import java.util.HashMap;

import teamwork.goodVibrations.functions.Function;
import teamwork.goodVibrations.triggers.Trigger;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class GoodVibrationsService extends Service
{
	private TriggerQueue q;
	private HashMap<Trigger, Function> map;

	@Override
	public void onCreate()
	{
		loadTriggers();
		q = new TriggerQueue(map.keySet());
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
	private void loadTriggers()
	{
		//TODO
		map.put(new Trigger("pie 4000", 4000), spamFunction());
		map.put(new Trigger("pie 7000", 7000), spamFunction());
	}
	@Override
	public IBinder onBind(Intent arg0)
	{
		return null;
	}
	private Function spamFunction()
	{
		return null;
	}
}
