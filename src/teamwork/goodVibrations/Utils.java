package teamwork.goodVibrations;

import java.util.Calendar;

public class Utils
{  
  public static long getTimeOfDayInMillis()
  {
    Calendar c = Calendar.getInstance();
    
    return //c.get(Calendar.HOUR_OF_DAY)*3600000 +
           //c.get(Calendar.MINUTE)*60000 +
           (c.get(Calendar.SECOND)*1000 +
           c.get(Calendar.MILLISECOND))%30000;
  }
  
  
}