package teamwork.goodVibrations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

import android.util.Log;

import teamwork.goodVibrations.triggers.Trigger;

public class TriggerQueue
{
  private static String TAG = "TriggerQueue";
  private ArrayList<Trigger> triggers;
  private Comparator<Trigger> comparator;

  public TriggerQueue()
  {
    comparator = new Comparator<Trigger>()
    {
      public int compare(Trigger t1, Trigger t2)
      {
        if(t1.getSleepTime() == t2.getSleepTime())
        {
          return 0;
        }
        return t1.getSleepTime() < t2.getSleepTime() ? -1 : 1;
      }
    };
    
    triggers = new ArrayList<Trigger>();
  }
  
  public void add(Trigger t)
  {
    triggers.add(t);
  }
  
  public void remove(int index)
  {
    triggers.remove(index);
  }

  public Trigger getNextTrigger()
  {
    try
    {
      return Collections.min(triggers,comparator);
    }
    catch(NoSuchElementException e) 
    {
      return null;
    }
  }
  
  public void switchState(int id)
  {
    Iterator<Trigger> iter = triggers.iterator();
    while(iter.hasNext())
    {
      Trigger t = iter.next();
      if(t.id == id)
      {
        t.switchState();
        return;
      }
    }
  }
  
  public int[] getIDs()
  {
    Iterator<Trigger> iter = triggers.iterator();
    int[] IDs = new int[triggers.size()];
    int i = 0;
    while(iter.hasNext())
    {
      IDs[i] = iter.next().id;
      i++;
    }
    Log.d(TAG,"IDS: " + i);
    return IDs;
  }
  
  public String[] getNames()
  {
    Iterator<Trigger> iter = triggers.iterator();
    String[] names = new String[triggers.size()];
    int i = 0;
    while(iter.hasNext())
    {
      names[i] = iter.next().name;
      i++;
    }
    Log.d(TAG,"Names: " + i);
    return names;
  }
  
  public int size()
  {
    return triggers.size();
  }
}

