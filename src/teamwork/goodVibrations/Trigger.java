package teamwork.goodVibrations;

public class Trigger
{
	private long pollInterval;
	private long nextPoll;
	public String name;
	
	public Trigger(String n, int t)
	{
		name = n;
		pollInterval = t;
	}
	public long getPollInterval()
	{
		return pollInterval;
	}
	public long getNextPoll()
	{
		return nextPoll;
	}
	public void setNextPoll(long p)
	{
		nextPoll = p;
	}
	@Override
	public String toString()
	{
		return super.toString() + ": " + name + ", " + nextPoll;
	}
}
