package teamwork.goodVibrations.triggers;

import java.util.ArrayList;

public interface Trigger

{
  boolean addFunction(int type, Integer f);
  void removeFunction();
  long getNextExecutionTime();
  ArrayList<Integer> getFunctions(int type);
  ArrayList<Integer> getFunctions();
  int switchState();
}
