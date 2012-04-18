package teamwork.goodVibrations.functions;

import android.widget.CheckBox;

public class FunctionForUI extends Function
{
  public CheckBox chkbx;
  public boolean shouldBeChecked;

  public FunctionForUI(int i, String n)
  {
    shouldBeChecked = false;
    id = i;
    name = n;
    type = Function.FunctionType.UI;
  }

  public void execute()
  {}

  @Override
  public String getInternalSaveString()
  {
    // TODO Auto-generated method stub
    return null;
  }
}
