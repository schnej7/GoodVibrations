package teamwork.goodVibrations.triggers;

import java.util.ArrayList;

public interface Trigger

{
  void removeFunction(Integer id);
  long getNextExecutionTime();
  ArrayList<Integer> getFunctions();
  void switchState();
  boolean canExecute();
}

