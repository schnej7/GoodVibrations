package teamwork.goodVibrations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import android.util.Log;

import teamwork.goodVibrations.functions.Function;

public class FunctionList
{
  private static String TAG = "FunctionList";
  ArrayList<Function> functions;

  public FunctionList()
  {
    Log.d(TAG, "FunctionList()");
    functions = new ArrayList<Function>();
  }

  public FunctionList(Collection<Function> c)
  {
    this();
    for (Function f : c)
    {
      add(f);
    }
  }

  public void add(Function f)
  {
    functions.add(f);
  }

  public void remove(int id)
  {
    for (int i = 0; i < functions.size(); i++)
    {
      if (functions.get(i).id == id)
      {
        functions.remove(i);
      }
    }
  }

  public int size()
  {
    return functions.size();
  }

  public Function get(int i)
  {
    Iterator<Function> iter = functions.iterator();
    while (iter.hasNext())
    {
      Function f = iter.next();
      if (f.id == i)
      {
        return f;
      }
    }
    return null;
  }

  public int[] getIDs()
  {
    int[] fullIDs = new int[functions.size()];
    int i = 0;
    for (Function f : functions)
    {
      if (f.id > 0)
      {
        fullIDs[i] = f.id;
        i++;
      }
    }
    Log.d(TAG, "IFUNCTIONS.SIZE: " + functions.size() + " " + i);
    int[] IDs = new int[i];
    for (int j = 0; j < i; j++)
    {
      IDs[j] = fullIDs[j];
    }

    return IDs;
  }

  public String[] getNames()
  {
    String[] fullNames = new String[functions.size()];
    int i = 0;
    for (Function f : functions)
    {
      if (f.id > 0)
      {
        fullNames[i] = f.name;
        i++;
      }
    }
    Log.d(TAG, "NFUNCTIONS.SIZE: " + functions.size() + " " + i);
    String[] names = new String[i];
    for (int j = 0; j < i; j++)
    {
      names[j] = fullNames[j];
    }

    return names;
  }

}
