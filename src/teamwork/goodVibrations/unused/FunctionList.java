package teamwork.goodVibrations.unused;

import java.util.ArrayList;
import java.util.Iterator;

import teamwork.goodVibrations.functions.Function;

//A class used to store a list of functions
public class FunctionList
{
  //List of functions stored internally
  ArrayList<Function> functions;
  
  //Constructor
  public FunctionList()
  {
    functions = new ArrayList<Function>();
  }
  
  //Add a new function to the list
  public void add_function(Function function_to_add)
  {
    functions.add(function_to_add);
  }
  
  //Get the size of the list
  public int size()
  {
    return functions.size();
  }
  
  //Get the function with the id id_to_find
  public Function get_function_from_id(int id_to_find)
  {
    Iterator<Function> function_list_iterator = functions.iterator();
    Function function_to_return = null;
    while(function_list_iterator.hasNext() && function_to_return == null)
    {
      Function function_to_compare = function_list_iterator.next();
      function_to_return = function_equals_id( function_to_compare, id_to_find );
    }
    return function_to_return;
  }
  
  //return a_function if the id of that function equals an_id
  //otherwise returns null
  private Function function_equals_id( Function a_function, int an_id ){
    if(a_function.id == an_id){
      return a_function;
    }
    return null;
  }
  
  //Returns an array of ints populated with all of the function ids
  public int[] get_int_array_of_ids()
  {
    int[] IDs_to_return = new int[functions.size()];
    populate_int_array_with_ids(IDs_to_return);
    return IDs_to_return;
  }
  
  //populates an array of ints with ids from every function in the list of functions
  private void populate_int_array_with_ids( int[] ID_array_to_populate )
  {
    Iterator<Function> function_list_iterator = functions.iterator();
    int ID_array_to_populate_counter = 0;
    while(function_list_iterator.hasNext())
    {
      Function next_function = function_list_iterator.next();
      ID_array_to_populate[ID_array_to_populate_counter] = next_function.id;
      ID_array_to_populate_counter++;
    }
  }
  
  //returns a list of strings populated with the name of each function in the list of functions
  public String[] get_string_array_of_names()
  {
    String[] list_of_names_to_return = new String[functions.size()];
    populate_string_array_with_names( list_of_names_to_return );
    return list_of_names_to_return;
  }
  
  //populates an array of strings with the name of each function in the list of functions
  private void populate_string_array_with_names( String[] name_array_to_populate )
  {
    Iterator<Function> function_list_iterator = functions.iterator();
    int name_array_to_populate_counter = 0;
    while(function_list_iterator.hasNext())
    {
      Function next_function = function_list_iterator.next();
      name_array_to_populate[name_array_to_populate_counter] = next_function.name;
      name_array_to_populate_counter++;
    }
  }
  
}
