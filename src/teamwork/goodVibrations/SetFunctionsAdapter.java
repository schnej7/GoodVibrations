package teamwork.goodVibrations;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import teamwork.goodVibrations.functions.FunctionForUI;

public class SetFunctionsAdapter extends ArrayAdapter<FunctionForUI>
{
  int resource;
  Context context;

  public SetFunctionsAdapter(Context context, int resource,
      List<FunctionForUI> items)
  {
    super(context, resource, items);
    this.resource = resource;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent)
  {
    LinearLayout funcView;
    // gets current function object
    FunctionForUI f = getItem(position);

    // inflate the view
    if (convertView == null)
    {
      funcView = new LinearLayout(getContext());
      String inflater = Context.LAYOUT_INFLATER_SERVICE;
      LayoutInflater vi;
      vi = (LayoutInflater) getContext().getSystemService(inflater);
      vi.inflate(resource, funcView, true);
    }
    else
    {
      funcView = (LinearLayout) convertView;
    }
    // get items from time_trigger_functions_items.xml
    TextView funcName = (TextView) funcView
        .findViewById(R.id.timeTriggerFunctionName);
    f.chkbx = (CheckBox) funcView
        .findViewById(R.id.timeTriggerFunctionCheckBox);

    // assign appropriate data
    funcName.setText(f.name);
    f.chkbx.setChecked(f.shouldBeChecked);

    return funcView;

  }

}
