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

  // All functions implement this which is the actual execution of the function
  public abstract Function execute();

  // Gets the object as a string used for saving in persistent storage
  public abstract String getInternalSaveString();

  // Returns the function as an intent, used for editing functions
  public abstract Intent getFunctionAsIntent();

  // Returns the type and internal save string, used for persistent storage
  public String getSaveString()
  {
    return type.getTypeInt() + Constants.SAVE_STRING_DELIM
        + getInternalSaveString();
  }

  // Recreates the function object as based on the string representation from persistent storage
  public static Function reconstitute(String s)
  {
    if (s == null)
      return null;
    try
    {
      String[] a = s.split(Constants.SAVE_STRING_DELIM);
      int i = Integer.valueOf(a[0]);
      // Calls the correct constructor for the function type based on the class that is should be.
      // This class is identified as the first element in the save string
      return (Function) FunctionType.getType(i).getFunctionClass()
          .getConstructor(new Class<?>[] { String.class }).newInstance(a[1]);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return null;
  }

  // An enumeration to determine the function type
  public enum FunctionType
  {
    RINGTONE(0, RingtoneFunction.class),
    UI(1, FunctionForUI.class),
    RING_VOLUME(2, SetVolumeFunction.class),
    WALLPAPER(3, WallpaperFunction.class);

    // A quick map to find the integer value associated with the function type
    private static HashMap<Integer, FunctionType> lookup = new HashMap<Integer, FunctionType>();

    // Runs once, initialize the HashMap
    static
    {
      for (FunctionType t : EnumSet.allOf(FunctionType.class))
      {
        lookup.put(t.type, t);
      }
    }

    // The type of the instance of the enum
    private int type;
    private Class<?> c;

    // Constructor
    private FunctionType(int i, Class<?> c)
    {
      this.type = i;
      this.c = c;
    }

    // Gets the FunctionType for the given int
    public static FunctionType getType(int i)
    {
      return lookup.get(i);
    }

    // Returns the current class type
    public Class<?> getFunctionClass()
    {
      return c;
    }

    // Returns the integer of the given type
    public int getTypeInt()
    {
      return type;
    }

  }
}
