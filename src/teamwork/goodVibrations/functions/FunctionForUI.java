package teamwork.goodVibrations.functions;

import android.content.Intent;
import android.widget.CheckBox;

// This class is used in the UI to group function data with checkboxes
public class FunctionForUI extends Function
{
  public CheckBox chkbx;
  public boolean shouldBeChecked;

  // Constructor
  public FunctionForUI(int i, String n)
  {
    shouldBeChecked = false;
    id = i;
    name = n;
    type = Function.FunctionType.UI;
  }

  // No execute is needed
  public FunctionForUI execute()
  {
    return null;
  }

  // No save string is needed as this is never saved to persistent storage
  @Override
  public String getInternalSaveString()
  {
    return null;
  }

  // Do not need to rebuild this function as an intent.
  @Override
  public Intent getFunctionAsIntent()
  {
    return null;
  }
}
