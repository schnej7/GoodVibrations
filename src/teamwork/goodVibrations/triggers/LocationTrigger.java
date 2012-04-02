package teamwork.goodVibrations.triggers;

import java.util.ArrayList;
import java.util.List;

import teamwork.goodVibrations.Constants;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class LocationTrigger extends Trigger
{
  
  private static String TAG = "LocationTrigger";
  
  private boolean lastLocation;    // True - inside
  private boolean currentLocation; // True - inside
  private Context mC;
  private LocationManager LM;
  private List<String> providers;
  private Location center;
  private float radius;
  private ArrayList<Integer> enterFunctionIDs;       // The functions that will be executed on start
  private ArrayList<Integer> exitFunctionIDs;  // The functions that will be executed on stop
  private Criteria criteria;
  
  public static boolean ENTERFUNCTION = true;
  public static boolean EXITFUNCTION = false;
  
  private int lat = 0;
  private int lon = 0;
  
  public LocationTrigger(Context c,Bundle b, int newID)
  {
    mC = c;
    name = b.getString(Constants.INTENT_KEY_NAME);
    id = newID;
    // Get the location manager
    LM = (LocationManager) c.getSystemService(Context.LOCATION_SERVICE);
    enterFunctionIDs = new ArrayList<Integer>();
    exitFunctionIDs = new ArrayList<Integer>();
    
    Log.d(TAG,"Made Location Manager");
    
    int[] enterIDs = b.getIntArray(Constants.INTENT_KEY_FUNCTION_IDS);
    int[] exitIDs = {1}; // Hardcoded for Product stakeholder review 1
    for(int i = 0; i < enterIDs.length; i++)
    {
      enterFunctionIDs.add(new Integer(enterIDs[i]));
    }
    for(int i = 0; i < exitIDs.length; i++)
    {
      exitFunctionIDs.add(new Integer(exitIDs[i]));
    }
    // List all providers:Log
    providers = new ArrayList<String>();
    providers = LM.getAllProviders();
    
    Log.d(TAG, "Got providers");
    Log.d(TAG,"PROVIDERS:" + providers);
    criteria = new Criteria();
    String bestProvider = LM.getBestProvider(criteria, false);
    //Location recievedLocation = LM.getLastKnownLocation(bestProvider);
    Location recievedLocation = new Location(bestProvider);
    //recievedLocation.setLatitude(0);
    //recievedLocation.setLongitude(0);
    
    Log.d(TAG,"Got location" + recievedLocation);
    radius = b.getFloat(Constants.INTENT_KEY_RADIUS);
    Location l = new Location("");
    l.setLatitude(b.getDouble(Constants.INTENT_KEY_LATITUDE));
    l.setLongitude(b.getDouble(Constants.INTENT_KEY_LONGITUDE));
    center = l;
    
    if(recievedLocation != null)
    {
      if(recievedLocation.distanceTo(center) > radius)
      {
        lastLocation = false;
        currentLocation = false;
      }
      else
      {
        lastLocation = true;
        currentLocation = true;
      }
    }
    else
    {
      Log.d(TAG,"Location not recieved");
    }
    
  }
  
  public void removeFunction(Integer id)
  {
    // TODO Auto-generated method stub
    
  }

  public long getSleepTime()
  {
    // Check location every 5 minutes
    //return 300000;
    return 10000;
  }

  public ArrayList<Integer> getFunctions()
  {
	if (lastLocation && !currentLocation)
	{
	   return exitFunctionIDs;
	}
	  
    if(currentLocation == lastLocation)
    {
      return null;
    }
    
    if (!lastLocation && currentLocation)
    {
      return enterFunctionIDs;
    }
    
    return null;
  }

  public void switchState()
  {
    // TODO Auto-generated method stub
  }

  public boolean canExecute()
  {
    Log.d(TAG,"canExecute()");
    //Get new location and calculate distance to target
    String bestProvider = LM.getBestProvider(criteria, false);
    //Location recievedLocation = LM.getLastKnownLocation(bestProvider);
    Location recievedLocation = new Location(bestProvider);
    if(lat > 5)
    {
      lat = -2;
      lon = -2;
    }
    recievedLocation.setLatitude(lat++);
    recievedLocation.setLongitude(lon++);
    
    Log.d(TAG,"LAT/LON " + lat);
    
    if(recievedLocation == null)
    {
      Log.d(TAG,"Location not recieved");
      return false;
    }
    else
    {
      double dist = recievedLocation.distanceTo(center); 
      
      //make the current location the new location since we're updating current location
      lastLocation = currentLocation;
      
      //update location based in distance to target
      if(dist < radius)
      {
        currentLocation = true;
      }
      else
      {
        currentLocation = false;
      }
      
      //Return true if we've moved into the target area
      if(!lastLocation  && currentLocation)
      {
        currentLocation = true;
        return true;
      }
      
      //Also return true if we've moved out of the target area
      if(lastLocation && !currentLocation)
      {
        return true;
      }
      
      //otherwise, we haved moved in our out of target area
      if(currentLocation == lastLocation)
      {
        return false;
      }
    }
    return false;
  }
  
  //Adds a functionID to either the start or stop list
  public boolean addFunction(boolean type, Integer f)
  {
    if(type == ENTERFUNCTION)
    {
      enterFunctionIDs.add(f);
    }
    else if(type == EXITFUNCTION)
    {
      exitFunctionIDs.add(f);
    }
     return true;
  }
  
}