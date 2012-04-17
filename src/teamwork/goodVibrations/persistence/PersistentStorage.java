package teamwork.goodVibrations.persistence;

import teamwork.goodVibrations.Constants;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import teamwork.goodVibrations.functions.Function;
import teamwork.goodVibrations.triggers.Trigger;

public class PersistentStorage
{
  private static File trigDir;
  private static File funcDir;
  private static File funcList;
  private static File trigList;

  static
  {
    initStorage();
  }

  public static ArrayList<Function> loadFunctions()
  {
    ArrayList<Function> ret = new ArrayList<Function>();

    for(File f : listDirectory(Constants.FUNC_DIR_PATH, funcList))
    {
      ret.add(Function.reconstitute(readFile(f)));
    }

    return ret;
  }

  public static void saveFunctions(ArrayList<Function> functions)
  {
    for(File f : listDirectory(Constants.FUNC_DIR_PATH, funcList))
    {
      f.delete();
    }
    funcList.delete();
    try
    {
      funcList.createNewFile();
      int i = 0;
      PrintWriter out = null;
      File outputFile;
      PrintWriter outList = new PrintWriter(funcList);
      for(Function f : functions)
      {
        outputFile = new File(Constants.FUNC_DIR_PATH, "function" + i + ".txt");
        outList.write("function" + i + ".txt");
        outputFile.createNewFile();
        out = new PrintWriter(new FileWriter(outputFile));
        out.write(f.getSaveString());
      }
      if(out != null)
      {
        out.close();
        outList.close();
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

    for(File f : listDirectory(Constants.TRIG_DIR_PATH, trigList))
    {
      ret.add(Trigger.reconstitute(readFile(f)));
    }

    return ret;
  }

  public static void saveTriggers(ArrayList<Trigger> triggers)
  {
    for(File f : listDirectory(Constants.TRIG_DIR_PATH, trigList))
    {
      f.delete();
    }
    trigList.delete();
    try
    {
      trigList.createNewFile();
      int i = 0;
      PrintWriter out = null;
      PrintWriter outList = new PrintWriter(trigList);
      File outputFile;
      for(Trigger t : triggers)
      {
        outputFile = new File(Constants.TRIG_DIR_PATH, "trigger" + i + ".txt");
        outList.write("trigger" + i + ".txt");
        outputFile.createNewFile();
        out = new PrintWriter(new FileWriter(outputFile));
        out.write(t.getSaveString());
      }
      if(out != null)
      {
        out.close();
        outList.close();
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

    funcList = new File(Constants.FUNC_DIR_PATH, Constants.FUNC_LIST_NAME);
    trigList = new File(Constants.TRIG_DIR_PATH, Constants.TRIG_LIST_NAME);
    try
    {
      if(!funcList.exists())
        funcList.createNewFile();
      if(!trigList.exists())
        trigList.createNewFile();
    }
    catch(IOException e)
    {
      e.printStackTrace();
    }
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

  private static ArrayList<File> listDirectory(String dirPath, File list)
  {
    ArrayList<File> ret = new ArrayList<File>();

    try
    {
      BufferedReader in = new BufferedReader(new FileReader(list));
      ArrayList<String> names = new ArrayList<String>();
      String s;
      while((s = in.readLine()) != null)
      {
        names.add(s);
      }
      in.close();
      for(String name : names)
      {
        ret.add(new File(dirPath, name));
      }
    }
    catch(FileNotFoundException e)
    {
      e.printStackTrace();
    }
    catch(IOException e)
    {
      e.printStackTrace();
    }

    return ret;
  }
}