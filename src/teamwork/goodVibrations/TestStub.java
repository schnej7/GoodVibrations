package teamwork.goodVibrations;

public class TestStub
{
	private static TriggerQueue q;
	
	public static void main(String[] args)
	{
		GoodVibrationsService s = new GoodVibrationsService();
		s.onCreate();
		s.onStart(null, 0);
	}
}
