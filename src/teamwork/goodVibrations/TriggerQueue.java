package teamwork.goodVibrations;

import java.util.Comparator;
import java.util.PriorityQueue;

public class TriggerQueue
{
	public PriorityQueue<Trigger> q;
	private Trigger[] triggers;
	
	public TriggerQueue(Trigger[] t)
	{
		triggers = t;
		q = new PriorityQueue<Trigger>(triggers.length, new Comparator<Trigger>()
		{
			@Override
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
		Trigger t = q.poll();
		t.setNextPoll(t.getNextPoll() + t.getPollInterval()*1000000);
		q.add(t);
		return t;
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
}