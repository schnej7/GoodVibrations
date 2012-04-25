package teamwork.goodVibrations.triggers;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;

import android.content.Intent;
import teamwork.goodVibrations.Constants;

//Abstract class for triggers. Triggers are used to let the service know when a 
//phone setting should be changed.
public abstract class Trigger
{
  @SuppressWarnings("unused")
  private static final String TAG = "Trigger";

  //Member variables
  public int id;
  public String name;
  //type is an enum used for persistent storage
  protected TriggerType type;
  //priority is used when a user wants one trigger to override another
  public int priority;

  //functions to be implemented in the instance of the class
  
  //removes a function from a trigger's list of functions
  abstract public void removeFunction(Integer id);

  //calculates how long the service should sleep before the trigger should be executed
  abstract public long getSleepTime();

  //gets all functions that the trigger should execute
  abstract public ArrayList<Integer> getFunctions();

  //switches between various states associated with a trigger
  abstract public void switchState();

  //returns a trigger as an intent. Used to reinitialize a menu when editing a function
  public abstract Intent getTriggerAsIntent();

  //says whether a trigger should execute or not
  abstract public boolean canExecute();

  //can execute is overridden for priority
  abstract public boolean canExecute(int priority);

  //gets a savestring used for persistent storage
  abstract String getInternalSaveString();

  //adds a function into the list of functions associated with the trigger
  public abstract void addFunction(Integer fid, boolean isInverse);

  public abstract boolean isStarting();

  //adds a header onto the internal save string so the persistent storage can parse it 
  public String getSaveString()
  {
    return type.getTypeInt() + Constants.SAVE_STRING_DELIM
        + getInternalSaveString();
  }

  //builds a trigger from an internal save string
  public static Trigger reconstitute(String s)
  {
    if (s == null)
      return null;
    try
    {
      //the first string is what kind of trigger the string represents
      //the secondis the trigger information itself
      String[] a = s.split(Constants.SAVE_STRING_DELIM);
      int i = Integer.valueOf(a[0]);
      return (Trigger) TriggerType.getType(i).getTriggerClass()
          .getConstructor(new Class<?>[] { String.class }).newInstance(a[1]);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return null;
  }

  //enum data for persistent storage
  public enum TriggerType
  {
    TIME(0, TimeTrigger.class), LOCATION(1, LocationTrigger.class);

    private static HashMap<Integer, TriggerType> lookup = new HashMap<Integer, TriggerType>();

    static
    {
      for (TriggerType t : EnumSet.allOf(TriggerType.class))
      {
        lookup.put(t.type, t);
      }
    }

    private int type;
    private Class<?> c;

    private TriggerType(int i, Class<?> c)
    {
      this.type = i;
      this.c = c;
    }

    public static TriggerType getType(int i)
    {
      return lookup.get(i);
    }

    public Class<?> getTriggerClass()
    {
      return c;
    }

    public int getTypeInt()
    {
      return type;
    }
  }

}
