package teamwork.goodVibrations;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class FunctionDisplayActivity extends Activity {

	private ArrayAdapter <String> functionArrayAdapter;
	private ListView listView;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.function_tab);


		functionArrayAdapter = new ArrayAdapter<String>(this, R.layout.function_list_item);
		listView = (ListView) findViewById(R.id.listViewFunctions);
		listView.setAdapter(functionArrayAdapter);

		final Button buttonAdd = (Button) findViewById(R.id.addFunction);
		buttonAdd.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent functionEditIntent = new Intent(getApplicationContext(),FunctionEditActivity.class);
				startActivityForResult(functionEditIntent, 0);
			}
		});
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==RESULT_OK){
			functionArrayAdapter.add(data.getExtras().getString("one"));
		}
		else{
			Toast.makeText(this, "Fail", Toast.LENGTH_LONG).show();
		}
	}
}