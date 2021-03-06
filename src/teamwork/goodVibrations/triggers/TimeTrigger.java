package teamwork.goodVibrations.triggers;

import java.util.ArrayList;
import java.util.Calendar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import teamwork.goodVibrations.Utils;
import teamwork.goodVibrations.Constants;

public class TimeTrigger extends Trigger
{
  private static String TAG = "TimeTrigger";

  //various states for time triggers
  //FIRSTSTART is for when a trigger was created befor the end time, FIRSTSTOP is
  //for when a trigger was created after the end time, ACTIVE is for when a trigger is between 
  //start and end times, and inactive is for when a trigger is off
  public static enum STATE
  {
    FIRSTSTART, FIRSTSTOP, ACTIVE, INACTIVE
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

  // TimeTrigger
  // Constructor for making a time trigger from the GUI
  public TimeTrigger(Bundle b, int newID)
  {
    Log.d(TAG, "TimeTrigger()");
    if (b.getBoolean(Constants.INTENT_KEY_EDITED_BOOL))
    {
      id = b.getInt(Constants.INTENT_KEY_EDITED_ID);
    }
    else
    {
      id = newID;
    }
    //pull data out of that lovely construct known as a bundle
    name = b.getString(Constants.INTENT_KEY_NAME);
    startFunctionIDs = new ArrayList<Integer>();
    stopFunctionIDs = new ArrayList<Integer>();
    state = STATE.FIRSTSTART;
    daysActive = b.getByte(Constants.INTENT_KEY_REPEAT_DAYS_BYTE);
    startTime = b.getLong(Constants.INTENT_KEY_START_TIME);
    stopTime = b.getLong(Constants.INTENT_KEY_END_TIME);
    priority = b.getInt(Constants.INTENT_KEY_PRIORITY);
    type = Trigger.TriggerType.TIME;
    // int[] startIDs = b.getIntArray(Constants.INTENT_KEY_START_FUNCTION_IDS);
    int[] startIDs = b.getIntArray(Constants.INTENT_KEY_FUNCTION_IDS);
    //associate required functions with the trigger
    for (int i = 0; i < startIDs.length; i++)
    {
      startFunctionIDs.add(new Integer(startIDs[i]));
    }

    long currentTimeInDay = Utils.getTimeOfDayInMillis();
    //set state to FIRSTSTOP if trigger was created after the stop time
    //(during the inactive phase)
    if (currentTimeInDay > stopTime)
    {
      state = STATE.FIRSTSTOP;
    }
  }

  // TimeTrigger
  // Constructor for making a time trigger from the persistent storage
  public TimeTrigger(String s)
  {
    startFunctionIDs = new ArrayList<Integer>();
    stopFunctionIDs = new ArrayList<Integer>();

    type = Trigger.TriggerType.TIME;

    //split string and fill in member variables
    String[] categories = s.split(Constants.CATEGORY_DELIM);

    name = categories[0];
    id = new Integer(categories[1]).intValue();

    String[] startIDsString = categories[2].split(Constants.LIST_DELIM);
    //If there are no function ids, then there is a blank string
    //blank strings can't be represented as an integer
    if (!startIDsString[0].equals(""))
    {
      for (String stringID : startIDsString)
      {
        startFunctionIDs.add(new Integer(stringID).intValue());
      }
    }

    String[] stopIDsString = categories[3].split(Constants.LIST_DELIM);
    if (!stopIDsString[0].equals(""))
    {
      for (String stringID : stopIDsString)
      {
        stopFunctionIDs.add(new Integer(stringID).intValue());
      }
    }

    startTime = new Long(categories[4]).longValue();
    stopTime = new Long(categories[5]).longValue();
    daysActive = new Byte(categories[6]).byteValue();
    priority = new Integer(categories[7]).intValue();

    state = STATE.FIRSTSTART;
    long currentTimeInDay = Utils.getTimeOfDayInMillis();
    if (currentTimeInDay > stopTime)
    {
      state = STATE.FIRSTSTOP;
    }
  }

  // addFunction
  // Adds a functionID to either the start or stop list
  public void addFunction(Integer fid, boolean isInverse)
  {
    if (isInverse)
    {
      stopFunctionIDs.add(fid);
    }
    else
    {
      startFunctionIDs.add(fid);
    }
  }

  // getSleepTime
  // Returns the next time in milliseconds that this trigger must execute
  public long getSleepTime()
  {
    long currentTimeInDay = Utils.getTimeOfDayInMillis();

    // If current day is in daysActive
    if (canExecute())
    {
      long delay = 0;
      switch (state)
      {
        //sleep time needs to be calculated differently based on which statethe trigger is in
        case FIRSTSTART:
          //sleep from now until the start time
          delay = startTime - currentTimeInDay;
          break;
        case ACTIVE:
          //sleep from now until the end time
          delay = stopTime - currentTimeInDay;
          break;
        case INACTIVE:
          //sleep for the remainder of the day, and then sleep until the start time
          delay = Constants.dayInMillis - stopTime + startTime;
          break;
        case FIRSTSTOP:
          //sleep until the end of the dayand then until the start time
          //basicall the same as INACTIVE, except trigger could be at a 
          //later time than it would be for INACTIVE
          delay = Constants.dayInMillis - currentTimeInDay + startTime;
          break;
      }

      if (delay < 0)
      {
        delay = 0;
      }
      return delay;
    }

    // Cannot run today so sleep until tomorrow
    return Constants.dayInMillis - currentTimeInDay;
  }

  //isStarting
  //if the state is inactive or first start, then trigger should be counted as starting up 
  public boolean isStarting()
  {
    if (state == STATE.INACTIVE || state == STATE.FIRSTSTART)
    {
      return true;
    }
    else
    {
      return false;
    }
  }

  // canExecute
  // Determines if the trigger can execute based on a given priority
  public boolean canExecute(int priority)
  {
    Log.d(TAG, "Priority: " + this.priority + " MaxPriority: " + priority);
    if (this.priority > priority)
      return false;

    return true;
  }

  //canExecute
  // Determines if the trigger can execute on the current day of the week
  public boolean canExecute()
  {
    Calendar c = Calendar.getInstance();

    int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

    byte cDayOfWeek = Utils.getDayOfWeekBitMask(dayOfWeek);

    // If current day is in daysActive
    if ((daysActive & cDayOfWeek) != 0)
    {
      return true;
    }
    return false;
  }

  // getFunctions
  // Gets the functions of the specified state
  public ArrayList<Integer> getFunctions(STATE type)
  {
    if (type == STATE.ACTIVE || type == STATE.FIRSTSTART)
    {
      return startFunctionIDs;
    }
    else if (type == STATE.INACTIVE)
    {
      return stopFunctionIDs;
    }
    return null;
  }

  // getFunctions
  // Gets the functions of the current state
  public ArrayList<Integer> getFunctions()
  {
    if (state == STATE.INACTIVE || state == STATE.FIRSTSTART
        || state == STATE.FIRSTSTOP)
    {
      return startFunctionIDs;
    }
    else if (state == STATE.ACTIVE)
    {
      return stopFunctionIDs;
    }
    return null;
  }

  // switchState
  // Changes the state of the trigger.
  public void switchState()
  {
    if (state == STATE.INACTIVE || state == STATE.FIRSTSTART)
    {
      state = STATE.ACTIVE;
    }
    else if (state == STATE.ACTIVE || state == STATE.FIRSTSTOP)
    {
      state = STATE.INACTIVE;
    }
    Log.d(TAG, "STATE: " + state);
  }

  // removeFunction
  // Removes the function with id 'id' from the trigger
  public void removeFunction(Integer id)
  {
    for (int i = 0; i < startFunctionIDs.size(); i++)
    {
      if (startFunctionIDs.get(i).equals(id))
      {
        startFunctionIDs.remove(i);
        return;
      }
    }
  }

  // getInternalSaveString
  // Builds the save string for persistent storage
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
    saveString += id + Constants.CATEGORY_DELIM;

    // Save the start function ids
    for (Integer i : startFunctionIDs)
    {
      saveString += i.toString() + Constants.LIST_DELIM;
    }
    saveString += Constants.CATEGORY_DELIM;

    // Save the stop function ids
    for (Integer i : stopFunctionIDs)
    {
      saveString += i.toString() + Constants.LIST_DELIM;
    }
    saveString += Constants.CATEGORY_DELIM;

    saveString += Long.toString(startTime) + Constants.CATEGORY_DELIM;
    saveString += Long.toString(stopTime) + Constants.CATEGORY_DELIM;
    saveString += Byte.toString(daysActive);
    saveString += Constants.CATEGORY_DELIM;
    saveString += Integer.toString(priority);
    saveString += Constants.CATEGORY_DELIM;

    return saveString;
  }

  @Override
  public Intent getTriggerAsIntent()
  {
    Intent i = new Intent(Constants.SERVICE_MESSAGE);

    i.putExtra(Constants.INTENT_KEY_NAME, name);
    i.putExtra(Constants.INTENT_KEY_EDITED_ID, id);
    i.putExtra(Constants.INTENT_KEY_EDITED_BOOL, true);
    i.putExtra(Constants.INTENT_KEY_PRIORITY, priority);
    i.putExtra(Constants.INTENT_KEY_TYPE, Constants.TRIGGER_TYPE_TIME);
    i.putExtra(Constants.INTENT_KEY_REPEAT_DAYS_BYTE, daysActive);
    i.putExtra(Constants.INTENT_KEY_START_TIME, startTime);
    i.putExtra(Constants.INTENT_KEY_END_TIME, stopTime);
    int[] IDs = new int[startFunctionIDs.size()];
    for (int a = 0; a < startFunctionIDs.size(); a++)
    {
      IDs[a] = startFunctionIDs.get(a);
    }
    i.putExtra(Constants.INTENT_KEY_FUNCTION_IDS, IDs);

    return i;
  }

}