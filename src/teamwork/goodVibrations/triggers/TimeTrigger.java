package teamwork.goodVibrations.triggers;

import java.util.ArrayList;
import java.util.Calendar;
import teamwork.goodVibrations.Utils;


public class TimeTrigger implements Trigger
{
  
  public static int STOP  = 0;  // Flags for adding start or stop functions
  public static int START = 1;  // or telling state of the trigger
  
  private ArrayList<Integer> startFunctionIDs; // The functions that will be executed on start
  private ArrayList<Integer> stopFunctionIDs;  // The functions that will be executed on stop
  private int state;
  private byte daysActive;                     // Holds the days that the trigger is active 1 for Sunday through 7 for Saturday
  long startTime;                              // Number of milliseconds into the day that the trigger starts
  long stopTime;                               // Number of milliseconds into the day that the trigger ends
  
  // Constructor
  public TimeTrigger(long tStartTime, long tStopTime, byte tDaysActive)
  {
    startFunctionIDs = new ArrayList<Integer>();
    stopFunctionIDs = new ArrayList<Integer>();
    state = STOP;
    daysActive = tDaysActive;
    startTime = tStartTime;
    stopTime = tStopTime;
  }
  
  // Adds a functionID to either the start or stop list
  public boolean addFunction(int type, Integer f)
  {
    if(type == START)
    {
      startFunctionIDs.add(f);
    }
    else if(type == STOP)
    {
      stopFunctionIDs.add(f);
    }
    return true;
  }

  // Returns the next time in milliseconds that this trigger must execute
  public long getNextExecutionTime()
  {
    Calendar c = Calendar.getInstance();
    
    int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
    
    byte cDayOfWeek = getDayOfWeekBitMask(dayOfWeek);
    
    // If current day is in daysActive
    if((daysActive & cDayOfWeek) != 0)
    {
      long currentTimeInDay = Utils.getTimeOfDayInMillis();
     
      if(state == START)
      {
        long delay = stopTime - currentTimeInDay;
        if(delay < 0)
        {
          return 0;
        }
        else
        {
          return delay;
        }
      }
      else if(state == STOP)
      {
        long delay = startTime - currentTimeInDay;
        if(delay < 0)
        {
          return 0;
        }
        else
        {
          return delay;
        }
      }
    }
    return 0;
  }

  // Gets the functions of the specified state
  public ArrayList<Integer> getFunctions(int type)
  {
    if(type == START)
    {
      return startFunctionIDs;
    }
    else if(type == STOP)
    {
      return stopFunctionIDs;
    }
    return null;
  }
  
  // Gets the functions of the current state
  public ArrayList<Integer> getFunctions()
  {
    if(state == START)
    {
      return stopFunctionIDs;
    }
    else if(state == STOP)
    {
      return startFunctionIDs;
    }
    return null;
  }
  
  // Changes the state of the trigger.  Should this be called automatically?
  public int switchState()
  {
    if(state == START)
    {
      state = STOP;
    }
    else if(state == STOP)
    {
      state = START;
    }
    return state;
  }

  public void removeFunction(Integer id)
  {
    // TODO Auto-generated method stub
  }

  public void removeFunction()
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