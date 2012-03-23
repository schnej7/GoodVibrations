package teamwork.goodVibrations;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class FunctionDisplayActivity extends Activity {
	
	private ArrayAdapter <String> functionArrayAdapter;
	private ListView listView;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testview1);
        
        
        functionArrayAdapter = new ArrayAdapter<String>(this, R.layout.function_list_item);
        listView = (ListView) findViewById(R.id.listViewFunctions);
        listView.setAdapter(functionArrayAdapter);
        
        final Button buttonAdd = (Button) findViewById(R.id.addFunction);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	//triggerArray.add(messageString);
            	functionArrayAdapter.add("NEW FUNCTION!");
            }
        });
    }
}