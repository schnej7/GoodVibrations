package teamwork.goodVibrations;

import java.util.ArrayList;

import teamwork.goodVibrations.functions.Function;
import teamwork.goodVibrations.triggers.TimeTrigger;
import teamwork.goodVibrations.triggers.Trigger;
import teamwork.goodVibrations.functions.*;

import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

public class GoodVibrationsService extends Service
{
  private static String TAG = "GoodVibrationsService";
  
	private TriggerQueue triggers;          // Queue that holds all of the triggers
	private ArrayList<Function> functions;  // List that holds all of the functions
	
	private Looper mServiceLooper;          // Looper to handle the messages
	private ServiceHandler mServiceHandler;
		
	private SettingsChanger changer;
	
	// Handler that receives messages from the thread
	private final class ServiceHandler extends Handler
	{
      public ServiceHandler(Looper looper)
      {
          super(looper);
      }
      
      // Handles the message to add a trigger or a function
      @Override
      public void handleMessage(Message msg)
      {
        synchronized (this)
        {
          synchronized(triggers)
          {
            changer.interrupt();
            if(msg.arg2 == Constants.TRIGGER_TYPE)
            {
              triggers.push((Trigger)msg.obj);
            }
            else
            {
              functions.add((Function)msg.obj);
            }
          }
          try
          {
            changer.join();
          }
          catch(InterruptedException e)
          {
          }
          changer = new SettingsChanger();
          changer.start();
        }
        Log.d(TAG,"Stopping handle message");
        // Stop the service using the startId, so that we don't stop
        // the service in the middle of handling another job
        stopSelf(msg.arg1);
      }
	}
	
	private class SettingsChanger extends Thread
	{
    public void run()
    {
      Trigger t = null;
      boolean executed = false;
      while(!currentThread().isInterrupted())
      {
        try
        {
          synchronized(triggers)
          {
            // Grab the next trigger that will execute
            t = triggers.pop();
          }
          if(t != null) // Make sure we have a trigger to process
          {
            // Sleep for the time until the trigger will execute
            Thread.sleep(t.getNextExecutionTime());
            // Execute all of the functions for this trigger
            if(t.canExecute())
            {
              executed = true;
              for(Integer fID : t.getFunctions() )
              {
                functions.get(fID.intValue()).execute();
              }
            }
            // Re-insert the trigger so that it gets executed again
            synchronized(triggers)
            {
              if(executed)
              {
                t.switchState();
              }
              triggers.push(t);
            }
          }
          else // t is null because no trigger are in system
          {
            try
            {
              Thread.sleep(10000); 
            }
            catch(InterruptedException e)
            {         
            }
          }
        }
        catch(InterruptedException e)
        {
        }
      } // End While
      
      // If we were interrupted, re-insert the trigger so it is not lost
      synchronized(triggers)
      {
        if(t != null)  // Only push if a trigger was popped off earlier
        {
          triggers.push(t);
        }
      }
    } // End run()
	}

	@Override
	public void onCreate()
	{
		Log.d(TAG,"Calling onCreate");
		
		triggers  = new TriggerQueue();
		functions = new ArrayList<Function>();
		
		// Only samples, need to be removed
		/*
		functions.add(new LowerFunction((AudioManager) getSystemService(Context.AUDIO_SERVICE)));
		functions.add(new RaiseFunction((AudioManager) getSystemService(Context.AUDIO_SERVICE)));
		functions.add(new SetVolumeFunction((AudioManager) getSystemService(Context.AUDIO_SERVICE),"THREE",3));
		functions.add(new SetVolumeFunction((AudioManager) getSystemService(Context.AUDIO_SERVICE),"FIVE",5));
		functions.add(new SetVolumeFunction((AudioManager) getSystemService(Context.AUDIO_SERVICE),"ONE",1));
    functions.add(new SetVolumeFunction((AudioManager) getSystemService(Context.AUDIO_SERVICE),"SEVEN",7));
	  */
		
		Log.d(TAG,"Added Function");
		
		// Start up the thread running the service.  Note that we create a
	  // separate thread because the service normally runs in the process's
	  // main thread, which we don't want to block.  We also make it
	  // background priority so CPU-intensive work will not disrupt our UI.
	  HandlerThread thread = new HandlerThread("ServiceStartArguments", 10);//Process.THREAD_PRIORITY_BACKGROUND);
	  thread.start();
	  
	  Log.d(TAG,"Handler Started");
	  
	  changer = new SettingsChanger();
	  changer.start();
	  
	  Log.d(TAG,"Settings Changer Started");
	  
	  // Get the HandlerThread's Looper and use it for our Handler 
	  mServiceLooper = thread.getLooper();
	  mServiceHandler = new ServiceHandler(mServiceLooper);
	  Log.d(TAG,"Finished onCreate");
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
    Log.d(TAG,"Starting onStartCommand");
    // For each start request, send a message to start a job and deliver the
	  // start ID so we know which request we're stopping when we finish the job
    
	  Message msg = mServiceHandler.obtainMessage();
	  msg.arg1 = startId;
	  
	  Log.d(TAG,"Messaged recieved");
	  
	  Bundle b = intent.getExtras();
	  final int intentType = b.getInt(Constants.INTENT_TYPE);
	  final int type = b.getInt(Constants.INTENT_KEY_TYPE);

	  Log.d(TAG,"Bundle Created");
	  
	  if(intentType == Constants.FUNCTION_TYPE)
	  {
	    switch(type)
	    {
	      // Add a new volume function
	      case Constants.FUNCTION_TYPE_VOLUME:
	        functions.add( new SetVolumeFunction((AudioManager) getSystemService(Context.AUDIO_SERVICE),b) );
	        break;
	        
	      // Add a new ring tone function 
	      case Constants.FUNCTION_TYPE_RINGTONE:
	        Log.d(TAG, "New Ringtone Function");
	        functions.add( new RingtoneFunction(getApplicationContext(),b) );
	        break;
	        
	      default:
	        Log.d(TAG, "Default Function");
	        break;
	    }
	  }
	  else if(intentType == Constants.TRIGGER_TYPE)
	  {
	    switch(type)
      {   
        case Constants.TRIGGER_TYPE_TIME:
          break;
          
        case Constants.TRIGGER_TYPE_LOCATION:
          break;
          
        default:
          //Should never happen
      }
	    
	    //msg.arg2 = Constants.TRIGGER_TYPE;
	    //mServiceHandler.sendMessage(msg);
	  }
	  	  
	  
	  if(functions.size() == 2)
	  {
	    Log.d(TAG,"Adding Trigger");
	    // Making a trigger
	    // TODO Build trigger from parsed message
	    TimeTrigger t = new TimeTrigger(5000,15000,(byte)127);
	    t.addFunction(TimeTrigger.STATE.ACTIVE,   new Integer(0));
	    t.addFunction(TimeTrigger.STATE.INACTIVE, new Integer(1));
	    msg.obj = t;
	    msg.arg2 = Constants.TRIGGER_TYPE;
      mServiceHandler.sendMessage(msg);
	  }
	  /*
	  else
	  {
	    TimeTrigger t = new TimeTrigger(15000,20000,(byte)127);
      t.addFunction(TimeTrigger.STATE.ACTIVE,   new Integer(2));
      t.addFunction(TimeTrigger.STATE.INACTIVE, new Integer(3));
      msg.obj = t;
		  //TODO: fix this
	    //msg.obj = new NULLFunction();  
	  }
	  */
    
	  Log.d(TAG,"onStartCommand() Finished");
	  
	  // If we get killed, after returning from here, restart
	  return START_STICKY;
	}
	
	@Override
	public IBinder onBind(Intent arg0)
	{
		return null;
	}

}
