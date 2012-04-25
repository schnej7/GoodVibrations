package teamwork.goodVibrations;

import java.util.Calendar;


//Utility functions
public class Utils
{
  //converts the time of day into milliseconds from midnight
  public static long getTimeOfDayInMillis()
  {
    Calendar c = Calendar.getInstance();

    return c.get(Calendar.HOUR_OF_DAY) * 3600000 + c.get(Calendar.MINUTE)
        * 60000 + c.get(Calendar.SECOND) * 1000 + c.get(Calendar.MILLISECOND);
  }

  //returns the integer hour from milliseconds from midnight
  public static int getHoursFromMillis(long millis)
  {
    return (int) (millis / 3600000);
  }
  //returns the integer minute from milliseconds from midnight
  public static int getMinutesFromMillis(long millis)
  {
    return (int) (millis % 3600000) / 60000;
  }
  //takes in an integer hour and minute and converts them to milliseconds from midnight
  public static long calculateTimeInMillis(int hour, int minute)
  {
    return hour * 3600000 + minute * 60000;
  }

  //converts the integer day of the week to a byte
  //used for storing days of week in a bitmask
  public static byte getDayOfWeekBitMask(int dayOfWeek)
  {
    int res = 1;
    for (int i = 1; i < dayOfWeek; i++)
    {
      res = res * 2;
    }
    return (byte) res;
  }

  //converts a boolean to a byte
  public static byte booleanToByte(boolean a)
  {
    if (a)
    {
      return (byte) 1;
    }
    else
    {
      return (byte) 0;
    }
  }

}
