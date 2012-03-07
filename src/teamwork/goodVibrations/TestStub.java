package teamwork.goodVibrations;

public class TestStub
{
	private static TriggerQueue q;
	
	public static void main(String[] args)
	{
		q = new TriggerQueue(loadTriggers());
		q.initialize();
		
		while(true)
		{
			System.out.println(q.pop());
			try
			{
				Thread.sleep(q.getSleepTimeMilli());
			}
			catch(InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}

	private static Trigger[] loadTriggers()
	{
		return new Trigger[] {new Trigger("pie 5000", 5000), new Trigger("pie 7000", 7000)};
	}
}
