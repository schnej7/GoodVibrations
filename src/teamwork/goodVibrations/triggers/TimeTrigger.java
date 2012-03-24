package teamwork.goodVibrations.triggers;

import java.util.ArrayList;

import teamwork.goodVibrations.functions.Function;

public class TimeTrigger implements Trigger
{
  
  public static int STOP  = 0;  // Flags for adding start or stop functions
  public static int START = 1;  // or telling state of the trigger
  
  private ArrayList<Integer> startFunctionIDs; // The functions that will be executed on start
  private ArrayList<Integer> stopFunctionIDs;  // The functions that will be executed on stop
  private int state;
  private ArrayList<int[]> daysActive;   // Holds the days that the trigger is active 1 for Sunday through 7 for Saturday
  
  // Constructor
  public TimeTrigger()
  {
    startFunctionIDs = new ArrayList<Integer>();
    stopFunctionIDs = new ArrayList<Integer>();
    state = STOP;
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
    // TODO Implement so calculate based on the schedule 
    return System.currentTimeMillis() + 5000;
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
}