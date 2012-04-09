package teamwork.goodVibrations.persistence;

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
	private static final String teamDirPath = "teamwork";
	private static final String funcDirPath = teamDirPath + "/functions";
	private static final String trigDirPath = teamDirPath + "/triggers";
	private static final String funcListName = "Function List.txt";
	private static final String trigListName = "Trigger List.txt";
	
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
		
		for(File f : listDirectory(funcDirPath, funcList))
		{
			ret.add(Function.reconstitute(readFile(f)));
		}
		
		return ret;
	}
	public static void saveFunctions(ArrayList<Function> functions)
	{
		for(File f : listDirectory(funcDirPath, funcList))
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
			for(Function f : functions)
			{
				outputFile = new File(funcDirPath, "function" + i + ".txt");
				outputFile.createNewFile();
				out = new PrintWriter(new FileWriter(outputFile));
				out.write(f.getSaveString());
			}
			if(out != null)
			{
				out.flush();
				out.close();
			}
		}
		catch(IOException e){e.printStackTrace();}
	}
	public static ArrayList<Trigger> loadTriggers()
	{
		ArrayList<Trigger> ret = new ArrayList<Trigger>();
		
		for(File f : listDirectory(trigDirPath, trigList))
		{
			ret.add(Trigger.reconstitute(readFile(f)));
		}
		
		return ret;
	}
	public static void saveTriggers(ArrayList<Trigger> triggers)
	{
		for(File f : listDirectory(trigDirPath, trigList))
		{
			f.delete();
		}
		trigList.delete();
		try
		{
			trigList.createNewFile();
			int i = 0;
			PrintWriter out = null;
			File outputFile;
			for(Trigger t : triggers)
			{
				outputFile = new File(trigDirPath, "trigger" + i + ".txt");
				outputFile.createNewFile();
				out = new PrintWriter(new FileWriter(outputFile));
				out.write(t.getSaveString());
			}
			if(out != null)
			{
				out.flush();
				out.close();
			}
		}
		catch(IOException e){e.printStackTrace();}
	}
	private static void initStorage()
	{
		trigDir = new File(trigDirPath);
		funcDir = new File(funcDirPath);
		trigDir.mkdirs();
		funcDir.mkdirs();
		
		funcList = new File(funcDirPath, funcListName);
		trigList = new File(trigDirPath, trigListName);
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
			
			while((line = reader.readLine() ) != null )
			{
				stringBuilder.append( line );
				stringBuilder.append( ls );
			}
			reader.close();
			return stringBuilder.toString();
		}
		catch(IOException e){e.printStackTrace();}
		return null;
	}
	private static ArrayList<File> listDirectory(String dirPath, File list)
	{
		ArrayList<File> ret = new ArrayList<File>();
		
		try
		{
			BufferedReader in = new BufferedReader(new FileReader(list));
			ArrayList<String> functions = new ArrayList<String>();
			String s;
			while((s = in.readLine()) != null)
			{
				functions.add(s);
			}
			in.close();
			for(String name : functions)
			{
				ret.add(new File(dirPath, name));
			}
		}
		catch(FileNotFoundException e){e.printStackTrace();}
		catch(IOException e){e.printStackTrace();}
		
		return ret;
	}
}