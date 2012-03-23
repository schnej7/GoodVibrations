package teamwork.goodVibrations;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Set;

import android.util.Log;

import teamwork.goodVibrations.triggers.Trigger;

public class TriggerQueue
{
  /*
	public PriorityQueue<Trigger> q;
	private Set<Trigger> triggers;
	
	public TriggerQueue(Set<Trigger> set)
	{
		triggers = set;
		q = new PriorityQueue<Trigger>(triggers.size(), new Comparator<Trigger>()
		{
			public int compare(Trigger t1, Trigger t2)
			{
				if(t1.getNextPoll() == t2.getNextPoll())
					return 0;
				return t1.getNextPoll() < t2.getNextPoll() ? -1 : 1;
			}
		});
	}
	public int getSleepTimeMilli()
	{
		Trigger t = q.peek();
		long ret = t.getNextPoll() - System.nanoTime();
		return (int) (ret < 0 ? 0 : ret/1000000);
	}
	public Trigger pop()
	{
      try
	  {
        Trigger t = q.poll();
        t.setNextPoll(t.getNextPoll() + t.getPollInterval()*1000000);
        q.add(t);
        return t;
      }
      catch(Exception e)
      {
        Log.w("vorsth","Queue is empty");
        return null;
      }
	}
	
	public void finishTriggerExecution()
	{
		try
		{
		  q.poll();
		}
		catch(NullPointerException e)
		{
		  Log.w("vorsth","Queue is empty");
		}
	}
	
	public void initialize()
	{
		long n = System.nanoTime();
		for(Trigger t : triggers)
		{
			t.setNextPoll(n);
			q.add(t);
		}
		triggers = null;
	}
	
	public int size()
	{
		int s = q.size();
		return s;
	}
	*/
}