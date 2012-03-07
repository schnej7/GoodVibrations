package teamwork.goodVibrations;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;


public class GoodVibrationsActivity extends Activity
{
	public TextView textView;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		textView = new TextView(this);
		textView.setText("pie");
		setContentView(textView);
	}
	
	@Override
	public void onStart()
	{
		super.onStart();
		startService(new Intent(this, GoodVibrationsService.class));
	}
}