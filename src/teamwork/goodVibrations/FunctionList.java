package teamwork.goodVibrations;

import java.util.ArrayList;
import java.util.Iterator;

import android.util.Log;


import teamwork.goodVibrations.functions.Function;

public class FunctionList
{
  private static String TAG = "FunctionList";
  ArrayList<Function> functions;
  
  public FunctionList()
  {
    functions = new ArrayList<Function>();
  }
  
  public void add(Function f)
  {
    functions.add(f);
  }
  
  public int size()
  {
    return functions.size();
  }
  
  public Function get(int i)
  {
    return functions.get(i);
  }
  
  public int[] getIDs()
  {
    Iterator<Function> iter = functions.iterator();
    int[] IDs = new int[functions.size()];
    int i = 0;
    while(iter.hasNext())
    {
      IDs[i] = iter.next().id;
      i++;
    }
    return IDs;
  }
  
  public String[] getNames()
  {
    Iterator<Function> iter = functions.iterator();
    String[] names = new String[functions.size()];
    int i = 0;
    while(iter.hasNext())
    {
      names[i] = iter.next().name;
      Log.d(TAG, "N: " + names[i]);
      i++;
    }
    return names;
  }
  
}
