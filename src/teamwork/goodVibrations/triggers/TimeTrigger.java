package teamwork.goodVibrations.triggers;

import java.util.ArrayList;
import java.util.Calendar;

import android.os.Bundle;
import android.util.Log;
import teamwork.goodVibrations.Utils;
import teamwork.goodVibrations.Constants;

public class TimeTrigger extends Trigger
{
  private static String TAG = "TimeTrigger";

  public static enum STATE
  {
    FIRSTSTART,
    FIRSTSTOP,
    ACTIVE,
    INACTIVE
  };

  private ArrayList<Integer> startFunctionIDs; // The functions that will be
                                               // executed on start
  private ArrayList<Integer> stopFunctionIDs; // The functions that will be
                                              // executed on stop
  STATE state;
  private byte daysActive; // Holds the days that the trigger is active 1 for
                           // Sunday through 7 for Saturday
  long startTime; // Number of milliseconds into the day that the trigger starts
  long stopTime; // Number of milliseconds into the day that the trigger ends

  // Constructor
  public TimeTrigger(Bundle b, int newID)
  {
    Log.d(TAG,"TimeTrigger()");
    id = newID;
    name = b.getString(Constants.INTENT_KEY_NAME);
    startFunctionIDs = new ArrayList<Integer>();
    stopFunctionIDs = new ArrayList<Integer>();
    state = STATE.FIRSTSTART;
    daysActive = b.getByte(Constants.INTENT_KEY_REPEAT_DAYS_BYTE);
    startTime = b.getLong(Constants.INTENT_KEY_START_TIME);
    stopTime = b.getLong(Constants.INTENT_KEY_END_TIME);
    Log.d(TAG,"BEFORE");
    type = Trigger.TriggerType.TIME;
    Log.d(TAG,"AFTER");
    // int[] startIDs = b.getIntArray(Constants.INTENT_KEY_START_FUNCTION_IDS);
    int[] startIDs = b.getIntArray(Constants.INTENT_KEY_FUNCTION_IDS);
    // int[] stopIDs = b.getIntArray(Constants.INTENT_KEY_STOP_FUNCTION_IDS);
    int[] stopIDs = {0}; // Hard coded for Product stakeholder review 1
    for(int i = 0; i < startIDs.length; i++)
    {
      startFunctionIDs.add(new Integer(startIDs[i]));
    }
    for(int i = 0; i < stopIDs.length; i++)
    {
      stopFunctionIDs.add(new Integer(stopIDs[i]));
    }

    long currentTimeInDay = Utils.getTimeOfDayInMillis();
    if(currentTimeInDay > stopTime)
    {
      state = STATE.FIRSTSTOP;
    }
  }
  
  public TimeTrigger(String s)
  {
    startFunctionIDs = new ArrayList<Integer>();
    stopFunctionIDs = new ArrayList<Integer>();
    
    type = Trigger.TriggerType.TIME;
    
    String[] categories = s.split(Constants.CATEGORY_DELIM);
    
    name = categories[0];
    id = new Integer(categories[1]).intValue();
    
    String[] startIDsString = categories[2].split(Constants.LIST_DELIM);
    for(String stringID : startIDsString)
    {
      startFunctionIDs.add(new Integer(stringID).intValue());
    }
    
    String[] stopIDsString = categories[3].split(Constants.LIST_DELIM);
    for(String stringID : stopIDsString)
    {
      stopFunctionIDs.add(new Integer(stringID).intValue());
    }
    
    startTime = new Long(categories[4]).longValue();
    stopTime =  new Long(categories[5]).longValue();
    daysActive = new Byte(categories[6]).byteValue(); 
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
  public long getSleepTime()
  {
    long currentTimeInDay = Utils.getTimeOfDayInMillis();

    // If current day is in daysActive
    if(canExecute())
    {
      long delay = 0;
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
      return delay;
    }

    // Cannot run today so sleep until tomorrow
    return Constants.dayInMillis - currentTimeInDay;
  }

  public boolean canExecute()
  {
    Calendar c = Calendar.getInstance();

    int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

    byte cDayOfWeek = Utils.getDayOfWeekBitMask(dayOfWeek);

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
    Log.d(TAG, "STATE: " + state);
  }

  public void removeFunction(Integer id)
  {
    // TODO Auto-generated method stub
  }

  @Override
  String getInternalSaveString()
  {
    // Name
    // id
    // Start FunctionIDs
    // Stop FunctionIDs
    // StartTime
    // EndTime
    // Daysrepeated
    
    String saveString = new String();
    saveString = name + Constants.CATEGORY_DELIM;
    saveString += id  + Constants.CATEGORY_DELIM;
    
    // Save the start function ids
    for(Integer i : startFunctionIDs)
    {
      saveString += i.toString() + Constants.LIST_DELIM;
    }
    saveString += Constants.CATEGORY_DELIM;
    
    // Save the stop function ids
    for(Integer i : stopFunctionIDs)
    {
      saveString += i.toString() + Constants.LIST_DELIM;
    }
    saveString += Constants.CATEGORY_DELIM;
    
    saveString += Long.toString(startTime) + Constants.CATEGORY_DELIM;
    saveString += Long.toString(stopTime)  + Constants.CATEGORY_DELIM;
    saveString += Byte.toString(daysActive);
        
    return saveString;
  }

}