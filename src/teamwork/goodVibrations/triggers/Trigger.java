package teamwork.goodVibrations.triggers;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;

public abstract class Trigger
{
  private static final String delim = "%";

  public int id;
  public String name;
  protected TriggerType type;

  abstract public void removeFunction(Integer id);

  abstract public long getSleepTime();

  abstract public ArrayList<Integer> getFunctions();

  abstract public void switchState();

  abstract public boolean canExecute();

  abstract String getInternalSaveString();

  public String getSaveString()
  {
    return type.getTypeInt() + delim + getInternalSaveString();
  }

  public static Trigger reconstitute(String s)
  {
    if (s == null)
      return null;
    try
    {
      String[] a = s.split("%");
      int i = Integer.valueOf(a[0]);
      return (Trigger) TriggerType.getType(i).getTriggerClass()
          .getConstructor(new Class<?>[] { String.class })
          .newInstance(s.substring(a[0].length()));
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return null;
  }

  public enum TriggerType
  {
    TIME(0, TimeTrigger.class), LOCATION(1, LocationTrigger.class);

    private static HashMap<Integer, TriggerType> lookup;

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
