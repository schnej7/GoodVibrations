package teamwork.goodVibrations;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import teamwork.goodVibrations.functions.Function;
import teamwork.goodVibrations.triggers.Trigger;
import teamwork.goodVibrations.functions.*;

import android.app.PendingIntent;
import android.app.Service;
import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

public class GoodVibrationsService extends Service
{
  public static final String CUSTOM_INTENT = "teamwork.goodVibrations.custom.intent.action.TEST";
	private ArrayList<Trigger> triggers;
	private ArrayList<Function> functions;
	private AlarmManager am;
	
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
          int x = 1;
          //while(triggers.size() > 0)
          while(x < 0)
          {
            try
            {
              /*
              Trigger t = getNextTrigger(triggers);
              ArrayList<Integer> triggerFuncs = getAllFunctionIds(t);
              for(Integer f : triggerFuncs)
              {
                Function func = functions.get(f.intValue());
                func.execute();
              }
              //t.setLastExecutionTime( )
              int time = q.getSleepTimeMilli();
              Log.d("vorsth","Sleeping for " + time);
              
              
              */
              Thread.sleep(500);
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
      
	  private ArrayList<Integer> getAllFunctionIds(Trigger t)
	  {
        // TODO Auto-generated method stub
        return null;
	  }
	  
	  private Trigger getNextTrigger(ArrayList<Trigger> triggers)
	  {
        // TODO Auto-generated method stub
        return null;
	  }
	}

	@Override
	public void onCreate()
	{
		Log.d("vorsth","Calling onCreate");
		
		functions = new ArrayList<Function>();
		triggers = new ArrayList<Trigger>();
		loadTriggers();
		
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
	  Log.d("vorsth","Starting Load Triggers");
		functions.add(new RaiseFunction((AudioManager) getSystemService(Context.AUDIO_SERVICE)));
		functions.add(new LowerFunction((AudioManager) getSystemService(Context.AUDIO_SERVICE)));
		//triggers.add(new TimeTrigger());
		
		Log.d("vorsth","Added Functions");

		String alarm = Context.ALARM_SERVICE;
		AlarmManager am = ( AlarmManager ) getSystemService( alarm );

		Intent intent = new Intent( "REFRESH_THIS" );
		intent.putExtra("alarm_message", "Hello world");
		PendingIntent pi = PendingIntent.getBroadcast( this, 0, intent, PendingIntent.FLAG_ONE_SHOT );
		
		int type = AlarmManager.ELAPSED_REALTIME_WAKEUP;
		long interval = 3000;
		long triggerTime = SystemClock.elapsedRealtime() + interval;
		am.set( type, triggerTime, pi );
		
		/*
		Intent intent2 = new Intent("REFRESH_THIS2");
		intent2.putExtra("alarm_message","Goodbye world");
		PendingIntent pi2 = PendingIntent.getBroadcast(this, 0, intent2, PendingIntent.FLAG_ONE_SHOT);
		am.setRepeating(type, triggerTime + 1000, 8000, pi2);
		*/
		
    Log.d("vorsth","Finished Load Triggers");
	}
	
	@Override
	public IBinder onBind(Intent arg0)
	{
		return null;
	}

}
