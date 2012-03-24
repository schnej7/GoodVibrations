package teamwork.goodVibrations.triggers;

import java.util.ArrayList;
import java.util.Calendar;

public class TimeTrigger implements Trigger
{
  
  private ArrayList<Integer> functionIDs;
  private ArrayList<int[]> daysActive;   // Holds the days that the trigger is active 1 for Sunday through 7 for Saturday
  
  
  public TimeTrigger()
  {
    functionIDs = new ArrayList<Integer>();
    functionIDs.add(new Integer(0));
    functionIDs.add(new Integer(1));
  }
  
  public boolean addFunction(Integer f)
  {
    return true;
  }

  public long getNextExecutionTime()
  {
    return System.currentTimeMillis() + 5000;
  }

  public ArrayList<Integer> getFunctions()
  {
    return functionIDs;
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