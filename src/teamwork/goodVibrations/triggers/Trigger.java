package teamwork.goodVibrations.triggers;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;

import teamwork.goodVibrations.Constants;
import teamwork.goodVibrations.functions.Function;

public abstract class Trigger
{

  public int id;
  public String name;
  protected TriggerType type;

  abstract public void removeFunction(Integer id);

  abstract public long getSleepTime();

  abstract public ArrayList<Integer> getFunctions();

  abstract public void switchState();

  abstract public boolean canExecute();

  abstract String getInternalSaveString();
  
  public abstract void addFunction(Integer fid, boolean isInverse);
  
  public abstract boolean isStarting();

  public String getSaveString()
  {
    return type.getTypeInt() + Constants.SAVE_STRING_DELIM + getInternalSaveString();
  }

  public static Trigger reconstitute(String s)
  {
    if(s == null)
      return null;
    try
    {
      String[] a = s.split(Constants.SAVE_STRING_DELIM);
      int i = Integer.valueOf(a[0]);
      return (Trigger) TriggerType.getType(i).getTriggerClass().getConstructor(new Class<?>[] {String.class}).newInstance(a[1]);
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
    return null;
  }

  public enum TriggerType
  {
    TIME(0, TimeTrigger.class),
    LOCATION(1, LocationTrigger.class);

    private static HashMap<Integer, TriggerType> lookup = new HashMap<Integer,TriggerType>();

    static
    {
      for(TriggerType t : EnumSet.allOf(TriggerType.class))
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
