package teamwork.goodVibrations.persistence;

import teamwork.goodVibrations.Constants;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import android.util.Log;

import teamwork.goodVibrations.functions.Function;
import teamwork.goodVibrations.triggers.Trigger;

public class PersistentStorage
{
  private static final String TAG = "PersistentStorage";
  private static File trigDir;
  private static File funcDir;

  static
  {
    initStorage();
  }

  public static ArrayList<Function> loadFunctions()
  {
    Log.d(TAG,"loadFunction()");
    ArrayList<Function> ret = new ArrayList<Function>();

    for(File f : new File(Constants.FUNC_DIR_PATH).listFiles())
    {
      ret.add(Function.reconstitute(readFile(f)));
    }

    return ret;
  }

  public static void saveFunctions(ArrayList<Function> functions)
  {
    for(File f : new File(Constants.FUNC_DIR_PATH).listFiles())
    {
      f.delete();
    }
    try
    {
      int i = 0;
      PrintWriter out = null;
      File outputFile;
      for(Function f : functions)
      {
        outputFile = new File(Constants.FUNC_DIR_PATH, "function" + i + ".txt");
        outputFile.createNewFile();
        out = new PrintWriter(new FileWriter(outputFile));
        out.write(f.getSaveString());
      }
      if(out != null)
      {
        out.close();
      }
    }
    catch(IOException e)
    {
      e.printStackTrace();
    }
  }

  public static ArrayList<Trigger> loadTriggers()
  {
    ArrayList<Trigger> ret = new ArrayList<Trigger>();

    for(File f : new File(Constants.TRIG_DIR_PATH).listFiles())
    {
      ret.add(Trigger.reconstitute(readFile(f)));
    }

    return ret;
  }

  public static void saveTriggers(ArrayList<Trigger> triggers)
  {
    for(File f : new File(Constants.TRIG_DIR_PATH).listFiles())
    {
      f.delete();
    }
    try
    {
      int i = 0;
      PrintWriter out = null;
      File outputFile;
      for(Trigger t : triggers)
      {
        outputFile = new File(Constants.TRIG_DIR_PATH, "trigger" + i + ".txt");
        outputFile.createNewFile();
        out = new PrintWriter(new FileWriter(outputFile));
        out.write(t.getSaveString());
      }
      if(out != null)
      {
        out.close();
      }
    }
    catch(IOException e)
    {
      e.printStackTrace();
    }
  }

  private static void initStorage()
  {
    trigDir = new File(Constants.TRIG_DIR_PATH);
    funcDir = new File(Constants.FUNC_DIR_PATH);
    trigDir.mkdirs();
    funcDir.mkdirs();

    Log.d(TAG,Constants.FUNC_DIR_PATH);
  }

  private static String readFile(File f)
  {
    try
    {
      BufferedReader reader = new BufferedReader(new FileReader(f));
      String line = null;
      StringBuilder stringBuilder = new StringBuilder();
      String ls = System.getProperty("line.separator");

      while((line = reader.readLine()) != null)
      {
        stringBuilder.append(line);
        stringBuilder.append(ls);
      }
      reader.close();
      return stringBuilder.toString();
    }
    catch(IOException e)
    {
      e.printStackTrace();
    }
    return null;
  }
}