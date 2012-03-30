package teamwork.goodVibrations.triggers;

import java.util.ArrayList;

public abstract class Trigger
{
  public int id;
  abstract public void removeFunction(Integer id);
  abstract public long getNextExecutionTime();
  abstract public ArrayList<Integer> getFunctions();
  abstract public void switchState();
  abstract public boolean canExecute();
}

