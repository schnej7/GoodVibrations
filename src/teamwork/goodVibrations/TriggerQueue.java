package teamwork.goodVibrations;

import java.util.Comparator;
import java.util.PriorityQueue;
import android.util.Log;

import teamwork.goodVibrations.triggers.Trigger;

public class TriggerQueue
{
  public PriorityQueue<Trigger> q;
  private Comparator<Trigger> comparator;

  public TriggerQueue()
  {
    comparator = new Comparator<Trigger>()
        {
      public int compare(Trigger t1, Trigger t2)
      {
        if(t1.getNextExecutionTime() == t2.getNextExecutionTime())
        {
          return 0;
        }
        return t1.getNextExecutionTime() < t2.getNextExecutionTime() ? -1 : 1;
      }
        };
        q = new PriorityQueue<Trigger>(1, comparator);
  }

  public void push(Trigger t)
  {
    q.add(t);
  }

  public Trigger pop()
  {
    try
    {
      return q.poll();
    }
    catch(Exception e)
    {
      Log.w("vorsth","Queue is empty");
      return null;
    }
  }

  public int size()
  {
    return q.size();
  }
}

