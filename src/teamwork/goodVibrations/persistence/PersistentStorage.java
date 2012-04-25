package teamwork.goodVibrations.persistence;

import teamwork.goodVibrations.Constants;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;

import android.util.Log;

import teamwork.goodVibrations.functions.Function;
import teamwork.goodVibrations.triggers.Trigger;

public class PersistentStorage
{
  // class identifier for logging
  private static final String TAG = "PersistentStorage";
  // file that points to the 'triggers' directory
  private static File trigDir;
  // file that points to the 'functions' directory
  private static File funcDir;

  static
  {
    initStorage();
  }

  /*
   * Load all files in the 'functions' directory and send their contents to
   * Function.reconstitute to be turned into functions. Put all reconstituted
   * functions into an arraylist and return it.
   */
  public static ArrayList<Function> loadFunctions()
  {
    Log.d(TAG, "loadFunctions()");
    ArrayList<Function> ret = new ArrayList<Function>();

    // loop through files in the 'functions' directory
    for(File f : new File(Constants.FUNC_DIR_PATH).listFiles())
    {
      // create function out of save string in file and add to return collection
      ret.add(Function.reconstitute(readFile(f)));
    }

    return ret;
  }

  /*
   * Delete all currently saved functions in the 'functions' directory. Get the
   * save string of all functions in the argument list to be saved in their own
   * files in the 'functions' directory.
   */
  public static void saveFunctions(Collection<Function> functions)
  {
    Log.d(TAG, "saveFunctions()");
    // delete all existing files in the 'functions' directory
    for(File f : new File(Constants.FUNC_DIR_PATH).listFiles())
    {
      f.delete();
    }
    try
    {
      // loop through files in the argument collection
      for(Function f : functions)
      {
        // create new file to save function to
        saveFile(new File(Constants.FUNC_DIR_PATH, "function" + f.id + ".txt"),
            f.getSaveString());
      }
    }
    catch(IOException e)
    {
      e.printStackTrace();
    }
  }

  /*
   * Load all files in the 'triggers' triggers and send their contents to
   * Trigger.reconstitute to be turned into triggers. Put all reconstituted
   * triggers into an arraylist and return it.
   */
  public static ArrayList<Trigger> loadTriggers()
  {
    Log.d(TAG, "loadTriggers()");
    ArrayList<Trigger> ret = new ArrayList<Trigger>();

    // loop through files in the 'triggers' directory
    for(File f : new File(Constants.TRIG_DIR_PATH).listFiles())
    {
      // create trigger from save string and add to return collection
      ret.add(Trigger.reconstitute(readFile(f)));
    }

    return ret;
  }

  /*
   * Delete all currently saved triggers in the 'trigger' directory. Get the
   * save string of all triggers in the argument list to be saved in their own
   * files in the 'triggers' directory.
   */
  public static void saveTriggers(Collection<Trigger> triggers)
  {
    Log.d(TAG, "saveTriggers()");
    // delet all existing files in the 'triggers' directory
    for(File f : new File(Constants.TRIG_DIR_PATH).listFiles())
    {
      f.delete();
    }
    try
    {
      // loop through triggers in the arument collection
      for(Trigger t : triggers)
      {
        saveFile(new File(Constants.TRIG_DIR_PATH, "trigger" + t.id + ".txt"),
            t.getSaveString());
      }
    }
    catch(IOException e)
    {
      e.printStackTrace();
    }
  }

  /*
   * Initialize necessary file pointers and also make sure that both the
   * 'functions' and 'triggers directories exist.
   */
  private static void initStorage()
  {
    Log.d(TAG, "initStorage()");
    trigDir = new File(Constants.TRIG_DIR_PATH);
    funcDir = new File(Constants.FUNC_DIR_PATH);
    trigDir.mkdirs();
    funcDir.mkdirs();
  }

  /*
   * Generic method used by loadTriggers and loadFunctions. Reads the contents
   * of the given file nad returns the contents.
   */
  private static String readFile(File f)
  {
    Log.d(TAG, "readFile()");
    try
    {
      // open input stream on the argument file
      BufferedReader reader = new BufferedReader(new FileReader(f));
      String line = null;
      // StringBuilder will hold return string while it is being read from file
      StringBuilder ret = new StringBuilder();
      String ls = System.getProperty("line.separator");

      // read file line by line until EOF is reached
      while((line = reader.readLine()) != null)
      {
        // append line from file to return string, also append line seperator
        ret.append(line);
        ret.append(ls);
      }
      // close the input stream
      reader.close();
      return ret.toString();
    }
    catch(IOException e)
    {
      e.printStackTrace();
    }
    // return null if there was an error reading from the file
    return null;
  }

  private static void saveFile(File file, String saveString) throws IOException
  {
    // open output stream to file
    PrintWriter out = new PrintWriter(new FileWriter(file));
    // save trigger to file
    out.write(saveString);
    if(out != null)
    {
      // close the output stream
      out.close();
    }
  }
}