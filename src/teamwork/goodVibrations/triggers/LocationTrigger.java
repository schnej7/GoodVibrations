package teamwork.goodVibrations.triggers;

import java.util.ArrayList;
import java.util.List;

import teamwork.goodVibrations.Constants;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

public class LocationTrigger implements Trigger
{
  
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
  
  public LocationTrigger(Context c,Bundle b)
  {
    mC = c;
    // Get the location manager
    LM = (LocationManager) c.getSystemService(Context.LOCATION_SERVICE);
    
    // List all providers:
    providers = LM.getAllProviders();
    
    Criteria criteria = new Criteria();
    String bestProvider = LM.getBestProvider(criteria, false);
    Location recievedLocation = LM.getLastKnownLocation(bestProvider);
    
    radius = b.getFloat(Constants.INTENT_KEY_RADIUS);
    recievedLocation = (Location)b.getParcelable(Constants.INTENT_KEY_LOCATION);
    
    
    if(getDistanceBetween(recievedLocation, center) > radius)
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
  
  private double getDistanceBetween(Location a, Location b)
  {
    float results[] = new float[3];
    Location.distanceBetween(a.getLatitude(), a.getLongitude(), b.getLatitude(), b.getLongitude(), results);
    return results[0];
  }
  
  public void removeFunction(Integer id)
  {
    // TODO Auto-generated method stub
    
  }

  public long getNextExecutionTime()
  {
	//Check location every 5 minutes
    return 300000;
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
	//Get new location and calculate distance to target
    String bestProvider = LM.getBestProvider(criteria, false);
    Location recievedLocation = LM.getLastKnownLocation(bestProvider);
    
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
    
    return false;
  }

}
