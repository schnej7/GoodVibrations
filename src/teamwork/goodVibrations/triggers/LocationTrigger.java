package teamwork.goodVibrations.triggers;

import java.util.ArrayList;
import java.util.List;

import teamwork.goodVibrations.Constants;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class LocationTrigger extends Trigger
{  
  private static String TAG = "LocationTrigger";
  
  private boolean isInside;
  private Context mC;
  private LocationManager LM;
  private List<String> providers;
  private Location myLocation;
  private Location center;
  private float radius;
  private ArrayList<Integer> enterFunctionIDs;       // The functions that will be executed on start
  private ArrayList<Integer> exitFunctionIDs;  // The functions that will be executed on stop
  private Criteria criteria;
  private String bestProvider;
  
  public static boolean ENTERFUNCTION = true;
  public static boolean EXITFUNCTION = false;
  LocationListener listener;   
  public LocationTrigger(Context c,Bundle b, int newID)
  {
    listener = new LocationListener()
    {
      public void onLocationChanged(Location location) 
      {
        // Called when a new location is found by the network location provider.
        //LM.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        //Log.d(TAG, "LOCATION!!!!!");
        myLocation = location;
      }

      public void onStatusChanged(String provider, int status, Bundle extras) {}
      public void onProviderEnabled(String provider) {}
      public void onProviderDisabled(String provider) {}
    };

    isInside = false;
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
    criteria.setAccuracy(Criteria.ACCURACY_FINE);
    bestProvider = LM.getBestProvider(criteria, true);
    myLocation = new Location(bestProvider);
    myLocation = LM.getLastKnownLocation(bestProvider);
    // Define a listener that responds to location updates
    

    //Location recievedLocation = new Location(bestProvider);
    //recievedLocation.setLatitude(0);
    //recievedLocation.setLongitude(0);
    
    Log.d(TAG,"Got location" + myLocation);
    Log.d(TAG, "Proider: " + bestProvider);
    //radius = b.getFloat(Constants.INTENT_KEY_RADIUS);
    //Constant value of 50 for radius
    radius = 50;
    Location l = new Location("");
    l.setLatitude(b.getDouble(Constants.INTENT_KEY_LATITUDE));
    l.setLongitude(b.getDouble(Constants.INTENT_KEY_LONGITUDE));
    center = l;
    
    /*
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
    */
  }
  
  public void removeFunction(Integer id)
  {
    // TODO Auto-generated method stub
    
  }

  public long getSleepTime()
  {
    // Check location every 5 minutes
    //return 300000;
    
    //Check location every 10 seconds 
    return 3000;
  }

  public ArrayList<Integer> getFunctions()
  {
    /*
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
    */
    if (myLocation.distanceTo(center) > radius)
    {
       return exitFunctionIDs;
    }
    
    else
    {
      return enterFunctionIDs;
    }
  }

  public void switchState()
  {
    // TODO Auto-generated method stub
  }

  public boolean canExecute()
  {
    if (myLocation!= null)
      {
      LM.requestLocationUpdates(bestProvider, 0, 0, listener);
      //LM.requestLocationUpdates(bestProvider, 0, 0, listener);
      Log.d(TAG,"canExecute()");
      //Get new location and calculate distance to target
      
      Log.d(TAG,"LAT/LON " + myLocation.getLatitude() + "/" + myLocation.getLongitude());
      double dist = myLocation.distanceTo(center);
      boolean isNowInside = (dist < radius);
      Log.d(TAG, "center: " + center);
      Log.d(TAG, "dist: " + dist + " Radius: " + radius);
      Log.d(TAG, "IsnowInside: " + isNowInside);
      LM.removeUpdates(listener);
      
      if(isNowInside != isInside)
      {
        isInside = isNowInside;
        return true;
      }
      
      else
      {
        return false;
      }
      
      //return false;
    }
    else 
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