package teamwork.goodVibrations.functions;

import teamwork.goodVibrations.Constants;
import android.app.WallpaperManager;
import android.content.Context;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

public class WallpaperFunction extends Function
{
  private String TAG = "Wallpaper Function";
  private Context mC;
  private WallpaperManager WM;
  private Uri mUri;
  public WallpaperFunction(Context c, Bundle b,int newID)
  {
    Log.d(TAG,"RingtoneFunction() Constructor");
    mC = c;
    WM = WallpaperManager.getInstance(mC);
    mUri = b.getParcelable(Constants.INTENT_KEY_URI);
    name = b.getString(Constants.INTENT_KEY_NAME);
    id = newID;
  }

  @Override
  public void execute()
  {
    // TODO Auto-generated method stub
    
  }

}
