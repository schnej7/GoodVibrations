package teamwork.goodVibrations.triggers;

import java.util.ArrayList;
import java.util.Calendar;

import android.util.Log;
import teamwork.goodVibrations.Utils;
import teamwork.goodVibrations.Constants;

public class TimeTrigger implements Trigger
{
  private static String TAG = "TimeTrigger";
  
  public static enum STATE {FIRSTSTART, FIRSTSTOP, ACTIVE, INACTIVE};
  private ArrayList<Integer> startFunctionIDs; // The functions that will be executed on start
  private ArrayList<Integer> stopFunctionIDs;  // The functions that will be executed on stop
  STATE state;
  private byte daysActive;                     // Holds the days that the trigger is active 1 for Sunday through 7 for Saturday
  long startTime;                              // Number of milliseconds into the day that the trigger starts
  long stopTime;                               // Number of milliseconds into the day that the trigger ends
  
  // Constructor
  public TimeTrigger(long tStartTime, long tStopTime, byte tDaysActive)
  {
    startFunctionIDs = new ArrayList<Integer>();
    stopFunctionIDs = new ArrayList<Integer>();
    state = STATE.FIRSTSTART;
    daysActive = tDaysActive;
    startTime = tStartTime;
    stopTime = tStopTime;
    
    long currentTimeInDay = Utils.getTimeOfDayInMillis();
    if(currentTimeInDay > stopTime)
    {
      state = STATE.FIRSTSTOP;
    }
  }
  
  // Adds a functionID to either the start or stop list
  public boolean addFunction(STATE type, Integer f)
  {
    if(type == STATE.ACTIVE)
    {
      startFunctionIDs.add(f);
    }
    else if(type == STATE.INACTIVE)
    {
      stopFunctionIDs.add(f);
    }
    return true;
  }

  // Returns the next time in milliseconds that this trigger must execute
  public long getNextExecutionTime()
  {
    long currentTimeInDay = Utils.getTimeOfDayInMillis();
    
    // If current day is in daysActive
    if(canExecute())
    {
      long delay = 0;
      Log.d(TAG,"currentTime" + currentTimeInDay);
      switch(state)
      {
        case FIRSTSTART:
          delay = startTime - currentTimeInDay;
          break;
        case ACTIVE:
          delay = stopTime - currentTimeInDay;
          break;
        case INACTIVE:
          delay = Constants.dayInMillis - stopTime + startTime;
          break;
        case FIRSTSTOP:
          delay = Constants.dayInMillis - currentTimeInDay + startTime;
          break;
      }
      
      if(delay < 0)
      {
        delay = 0;
      }
      Log.d(TAG,"delay: " + delay);
      return delay;
    }
    
    return Constants.dayInMillis - currentTimeInDay;
  }
  
  public boolean canExecute()
  {
    Calendar c = Calendar.getInstance();
    
    int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
    
    byte cDayOfWeek = getDayOfWeekBitMask(dayOfWeek);
    
    // If current day is in daysActive
    if((daysActive & cDayOfWeek) != 0)
    {
      return true;
    }
    return false;
  }
  
  // Gets the functions of the specified state
  public ArrayList<Integer> getFunctions(STATE type)
  {
    if(type == STATE.ACTIVE || type == STATE.FIRSTSTART)
    {
      return startFunctionIDs;
    }
    else if(type == STATE.INACTIVE)
    {
      return stopFunctionIDs;
    }
    return null;
  }
  
  // Gets the functions of the current state
  public ArrayList<Integer> getFunctions()
  {
    if(state == STATE.INACTIVE || state == STATE.FIRSTSTART || state == STATE.FIRSTSTOP)
    {
      return startFunctionIDs;
    }
    else if(state == STATE.ACTIVE)
    {
      return stopFunctionIDs;
    }
    return null;
  }
  
  // Changes the state of the trigger.
  public void switchState()
  {
    if(state == STATE.INACTIVE || state == STATE.FIRSTSTART || state == STATE.FIRSTSTOP)
    {
      state = STATE.ACTIVE;
    }
    else if(state == STATE.ACTIVE)
    {
      state = STATE.INACTIVE;
    }
    Log.d(TAG,"STATE: " + state);
  }

  public void removeFunction(Integer id)
  {
    // TODO Auto-generated method stub
  }
  
  private byte getDayOfWeekBitMask(int dayOfWeek)
  {
    int res = 1;
    for(int i = 1; i < dayOfWeek - 1; i++)
    {
      res = res * 2;
    }
    return (byte)res;
  }
  
}