package teamwork.goodVibrations;

import java.util.HashMap;

import teamwork.goodVibrations.functions.Function;
import teamwork.goodVibrations.triggers.Trigger;
import teamwork.goodVibrations.functions.*;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

public class GoodVibrationsService extends Service
{
	private TriggerQueue q;
	private HashMap<Trigger, Function> map;
	private Looper mServiceLooper;
	private ServiceHandler mServiceHandler;
	
	// Handler that receives messages from the thread
	private final class ServiceHandler extends Handler
	{
      public ServiceHandler(Looper looper)
      {
          super(looper);
      }
      @Override
      public void handleMessage(Message msg)
      {
        synchronized (this)
        {
          while(q.size() > 0)
          {
            try
            {
              Trigger t = q.pop();
              Function f = map.get(t);
              f.execute();
              int time = q.getSleepTimeMilli();
              Log.d("vorsth","Sleeping for " + time);
              Thread.sleep(time);
			}
            catch(InterruptedException e)
            {
              Log.d("vorsth", "INTERRUPTED");
              e.printStackTrace();
	        }
            catch(Exception e)
            {
              Log.w("vorsth","Other Error in handleMessage");
              Log.e("vorsth",e.toString());
            }
	      }
        }
        Log.d("vorsth","Stopping handle message");
        // Stop the service using the startId, so that we don't stop
        // the service in the middle of handling another job
        stopSelf(msg.arg1);
      }
	}

	@Override
	public void onCreate()
	{
		Log.d("vorsth","Calling onCreate");
		
		map = new HashMap<Trigger, Function>();
		loadTriggers();
		q = new TriggerQueue(map.keySet());
		q.initialize();
		
		// Start up the thread running the service.  Note that we create a
	    // separate thread because the service normally runs in the process's
	    // main thread, which we don't want to block.  We also make it
	    // background priority so CPU-intensive work will not disrupt our UI.
	    HandlerThread thread = new HandlerThread("ServiceStartArguments", 10);//Process.THREAD_PRIORITY_BACKGROUND);
	    thread.start();
	    
	    // Get the HandlerThread's Looper and use it for our Handler 
	    mServiceLooper = thread.getLooper();
	    mServiceHandler = new ServiceHandler(mServiceLooper);
	    Log.d("vorsth","Finished onCreate");
	}
	@Override
	public void onStart(Intent intent, int id)
	{
	  Log.d("vorsth","Starting onStart");		
      Log.d("vorsth","Finished onStart");
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
      Log.d("vorsth","Starting onStartCommand");
            
      // For each start request, send a message to start a job and deliver the
	  // start ID so we know which request we're stopping when we finish the job
	  Message msg = mServiceHandler.obtainMessage();
	  msg.arg1 = startId;
	  mServiceHandler.sendMessage(msg);
	     
	  Log.d("vorsth","Finished onStartCommand");
	  
	  // If we get killed, after returning from here, restart
	  return START_STICKY;
	}
	
	private void loadTriggers()
	{
		map.put(new Trigger("pie 2000", 2000), new RaiseFunction((AudioManager) getSystemService(Context.AUDIO_SERVICE)));
		map.put(new Trigger("pie 4000", 4000), new LowerFunction((AudioManager) getSystemService(Context.AUDIO_SERVICE)));
	}
	
	@Override
	public IBinder onBind(Intent arg0)
	{
		return null;
	}

}
