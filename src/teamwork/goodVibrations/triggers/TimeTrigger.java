package teamwork.goodVibrations.triggers;

import java.util.Calendar;

import teamwork.goodVibrations.functions.Function;

public class TimeTrigger implements Trigger
{
    private Calendar begin;
    private Calendar end;
    
  
  public TimeTrigger()
  {
  }
  
  
  public boolean addFunction(Function f)
  {
    return true;
  }
  
  public void removeFunction()
  {
  }
  
  public void executeFunctions()
  { 
  }
  
  public void setBegin(Calendar c)
  {
    begin=c;
  }
  public void setEnd(Calendar c)
  {
    begin=c;
  }
  public Calendar getBegin()
  {
    return begin;
  }
  public Calendar getEnd()
  {
    return end;
  }
}