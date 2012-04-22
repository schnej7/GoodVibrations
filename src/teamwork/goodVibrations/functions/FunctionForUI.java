package teamwork.goodVibrations.functions;

import android.content.Intent;
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

  public FunctionForUI execute()
  {
    return null;
  }

  @Override
  public String getInternalSaveString()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Intent getFunctionAsIntent()
  {
    // TODO Auto-generated method stub
    return null;
  }
}
