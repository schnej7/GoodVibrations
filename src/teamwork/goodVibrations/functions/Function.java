package teamwork.goodVibrations.functions;

import java.util.EnumSet;
import java.util.HashMap;

public abstract class Function
{
  private static final String delim = "%";
  public int id;
  public String name;
  protected FunctionType type;

  public abstract void execute();

  public abstract String getInternalSaveString();

  public String getSaveString()
  {
    return type.getTypeInt() + delim + getInternalSaveString();
  }

  public static Function reconstitute(String s)
  {
    if (s == null)
      return null;
    try
    {
      String[] a = s.split(delim);
      int i = Integer.valueOf(a[0]);
      return (Function) FunctionType.getType(i).getFunctionClass()
          .getConstructor(new Class<?>[] { String.class })
          .newInstance(s.substring(a[0].length()));
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return null;
  }

  public enum FunctionType
  {
    RINGTONE(0, RingtoneFunction.class), UI(1, FunctionForUI.class), LOWER(2,
        LowerFunction.class), RAISE(3, RaiseFunction.class), VOLUME(4,
        SetVolumeFunction.class);

    private static HashMap<Integer, FunctionType> lookup;

    static
    {
      for (FunctionType t : EnumSet.allOf(FunctionType.class))
      {
        lookup.put(t.type, t);
      }
    }

    private int type;
    private Class<?> c;

    private FunctionType(int i, Class<?> c)
    {
      this.type = i;
      this.c = c;
    }

    public static FunctionType getType(int i)
    {
      return lookup.get(i);
    }

    public Class<?> getFunctionClass()
    {
      return c;
    }

    public int getTypeInt()
    {
      return type;
    }
  }
}
