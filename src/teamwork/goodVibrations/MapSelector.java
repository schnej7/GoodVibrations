package teamwork.goodVibrations;

import java.util.List;

import couk.chrisjenx.androidmaplib.AMLController;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import android.util.Log;
import android.widget.Toast;

//map selector displays a map via google api and lets a user select a point
public class MapSelector extends MapActivity
{

  // declare variables
  private String TAG = "MapSelector";
  private AMLController aml;
  private LocationManager LM;
  private MapOverlay myLocationOverlay;
  private MapView myMapView;
  private MapController myController;
  private Intent mIntent;
  GeoPoint userLoc;

  // Helper class
  // MapOverlay will draw a point on the map and display a
  // toast message of a tapped location
  class MapOverlay extends com.google.android.maps.Overlay
  {
    @Override
    public boolean draw(Canvas canvas, MapView mapView, boolean shadow,
        long when)
    {
      // Log.d(TAG, "drawing");
      super.draw(canvas, mapView, shadow);

      // ---translate the GeoPoint to screen pixels---
      Point screenPts = new Point();
      mapView.getProjection().toPixels(userLoc, screenPts);
      // Log.d(TAG, "got projection");
      // ---add the marker---
      Bitmap bmp = BitmapFactory.decodeResource(getResources(),
          R.drawable.point);
      canvas.drawBitmap(bmp, screenPts.x, screenPts.y - 25, null);
      // Log.d(TAG, "drew picture");
      return true;
    }

    @Override
    public boolean onTap(GeoPoint p, MapView mapView)
    {
      // update position of point icon on map
      userLoc = p;
      Log.d(TAG, "tap detected");
      Toast.makeText(getBaseContext(),
          p.getLatitudeE6() / 1E6 + "," + p.getLongitudeE6() / 1E6,
          Toast.LENGTH_SHORT).show();
      //store the selected point
      mIntent.putExtra(Constants.INTENT_KEY_LATITUDE, p.getLatitudeE6() / 1E6);
      mIntent
          .putExtra(Constants.INTENT_KEY_LONGITUDE, p.getLongitudeE6() / 1E6);
      setResult(RESULT_OK, mIntent);
      finish(); // Returns to LocationTriggerEditActivity.onActivityResult()
      return false;
    }
  }

  @Override
  protected void onCreate(Bundle icicle)
  {
    Log.d(TAG, "Creating Activity");
    super.onCreate(icicle);
    mIntent = new Intent();
  }

  protected void onStart()
  {
    Log.d(TAG, "Starting Activity");
    super.onStart();

    // Get initial location
    Context c = getApplicationContext();
    LM = (LocationManager) c.getSystemService(Context.LOCATION_SERVICE);
    //reset criteria and location provider
    Criteria criteria = new Criteria();
    criteria.setAccuracy(Criteria.ACCURACY_FINE);
    String bestProvider = LM.getBestProvider(criteria, true);
    Log.d(TAG, "BEST: " + bestProvider);
    Location receivedLocation = LM.getLastKnownLocation(bestProvider);

    //set the view for the map
    setContentView(R.layout.quick_start);
    aml = new AMLController(this, R.id.map_view);

    myMapView = aml.getMapView();

    //set satellite view and other settings
    myMapView.setSatellite(true);
    myMapView.setReticleDrawMode(MapView.ReticleDrawMode.DRAW_RETICLE_OVER);
    myMapView.displayZoomControls(true);
    //get users current position and set map to that point 
    try
    {
      userLoc = new GeoPoint((int) (receivedLocation.getLatitude()  * 1E6),
                             (int) (receivedLocation.getLongitude() * 1E6));
    }
    catch(NullPointerException e)
    {
      userLoc = new GeoPoint((int)(42.730051 * 1E6),(int)(-73.681729*1E6));
    }
    
    myController = myMapView.getController();
    myController.setZoom(17);
    aml.snapTo(userLoc);

    Log.d(TAG, "making overlay");
    // Initialize Overlay
    myLocationOverlay = new MapOverlay();
    myMapView.getOverlays().add(myLocationOverlay);

    List<Overlay> listOfOverlays = myMapView.getOverlays();
    listOfOverlays.clear();
    listOfOverlays.add(myLocationOverlay);

    myMapView.invalidate();
    Log.d(TAG, "made overlay");

  }

  @Override
  //needs to be here for interface
  protected boolean isRouteDisplayed()
  {
    return false;
  }

}
