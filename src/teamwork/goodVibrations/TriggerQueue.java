package teamwork.goodVibrations;

import java.util.ArrayList;
import java.util.Collection;
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

  // TriggerQueue
  // Constructor, defines the comparator, initializes the ArrayList
  public TriggerQueue()
  {
    //used for comparing two triggers
    comparator = new Comparator<Trigger>()
    {
      public int compare(Trigger t1, Trigger t2)
      {
        if (t1.getSleepTime() == t2.getSleepTime())
        {
          return 0;
        }
        return t1.getSleepTime() < t2.getSleepTime() ? -1 : 1;
      }
    };

    triggers = new ArrayList<Trigger>();
  }

  //collection of triggers
  public TriggerQueue(Collection<Trigger> c)
  {
    this();
    for (Trigger t : c)
    {
      add(t);
    }
  }

  // Adds a trigger to the queue
  public void add(Trigger t)
  {
    triggers.add(t);
  }

  //returns the trigger list
  public synchronized ArrayList<Trigger> getTriggers()
  {
    return triggers;
  }

  // Removes a trigger from the queue defined by index
  public void remove(int index)
  {
    for (int i = 0; i < triggers.size(); i++)
    {
      Trigger t = triggers.get(i);
      if (t.id == index)
      {
        triggers.remove(i);
      }
    }
  }

 

  // getNextTrigger
  // Returns the trigger that must execute next, determined by the comparator
  public Trigger getNextTrigger()
  {
    try
    {
      return Collections.min(triggers, comparator);
    }
    catch (NoSuchElementException e)
    {
      return null;
    }
  }

  // switchState
  // Switches the state of the trigger whose id is 'id'
  public void switchState(int id)
  {
    Iterator<Trigger> iter = triggers.iterator();
    while (iter.hasNext())
    {
      Trigger t = iter.next();
      if (t.id == id)
      {
        t.switchState();
        return;
      }
    }
  }

  //returns corresponding trigger
  public Trigger get(int id)
  {
    Iterator<Trigger> iter = triggers.iterator();
    while (iter.hasNext())
    {
      Trigger t = iter.next();
      if (t.id == id)
      {
        return t;
      }
    }
    return null;
  }

  // getIDs
  // Gets an array of all the trigger ids in the queue
  public int[] getIDs()
  {
    Iterator<Trigger> iter = triggers.iterator();
    int[] IDs = new int[triggers.size()];
    int i = 0;
    while (iter.hasNext())
    {
      IDs[i] = iter.next().id;
      i++;
    }
    Log.d(TAG, "IDS: " + i);
    return IDs;
  }

  // getNames
  // Gets a string array of all the trigger names
  public String[] getNames()
  {
    Iterator<Trigger> iter = triggers.iterator();
    String[] names = new String[triggers.size()];
    int i = 0;
    while (iter.hasNext())
    {
      names[i] = iter.next().name;
      i++;
    }
    Log.d(TAG, "Names: " + i);
    return names;
  }

  // size
  // Returns the number of triggers in the queue
  public int size()
  {
    return triggers.size();
  }
}
