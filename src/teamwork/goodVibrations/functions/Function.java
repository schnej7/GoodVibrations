package teamwork.goodVibrations.functions;

import java.util.EnumSet;
import java.util.HashMap;

import android.content.Intent;
import teamwork.goodVibrations.Constants;

public abstract class Function
{
  public int id;
  public String name;
  protected FunctionType type;
  static String TAG = "FUNCTIONS";

  public abstract Function execute();

  public abstract String getInternalSaveString();

  public abstract Intent getFunctionAsIntent();

  public String getSaveString()
  {
    return type.getTypeInt() + Constants.SAVE_STRING_DELIM
        + getInternalSaveString();
  }

  public static Function reconstitute(String s)
  {
    if (s == null)
      return null;
    try
    {
      String[] a = s.split(Constants.SAVE_STRING_DELIM);
      int i = Integer.valueOf(a[0]);
      return (Function) FunctionType.getType(i).getFunctionClass()
          .getConstructor(new Class<?>[] { String.class }).newInstance(a[1]);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return null;
  }

  public enum FunctionType
  {
    RINGTONE(0, RingtoneFunction.class), UI(1, FunctionForUI.class), RING_VOLUME(
        2, SetVolumeFunction.class), WALLPAPER(3, WallpaperFunction.class);

    private static HashMap<Integer, FunctionType> lookup = new HashMap<Integer, FunctionType>();

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
