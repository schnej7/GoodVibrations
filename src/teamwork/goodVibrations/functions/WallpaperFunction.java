package teamwork.goodVibrations.functions;

import teamwork.goodVibrations.Constants;
import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;

public class WallpaperFunction extends Function
{
  private String TAG = "Wallpaper Function";
  private Context mC;
  private WallpaperManager WM;
  private Uri imageUri;
 
  public WallpaperFunction(Context c, Bundle b,int newID)
  {
    Log.d(TAG,"RingtoneFunction() Constructor");
    mC = c;
    WM = WallpaperManager.getInstance(mC);
    imageUri = b.getParcelable(Constants.INTENT_KEY_IMAGEURI);
    name = b.getString(Constants.INTENT_KEY_NAME);
    id = newID;
  }
  
  // this is used to regenerate your object. All Parcelables must have a CREATOR
  // that implements these two methods
  //not sure what I'm supposed to do with these
  /*
  public static final Parcelable.Creator<RingtoneFunction> CREATOR = new Parcelable.Creator<WalpaperFunction>()
  {
    public WallpaperFunction createFromParcel(Parcel in)
    {
      return new WallpaperFunction(in);
    }

    public RingtoneFunction[] newArray(int size)
    {
      return new RingtoneFunction[size];
    }
  };
  
  
*/
  private WallpaperFunction(Parcel in)
  {
    name = in.readString();
    id = in.readInt();
  }
  
  @Override
  public void execute()
  {
    Log.d(TAG, "execute() - Setting Wallpaper to " + imageUri);
    
    try
    {
      // RingtoneManager.getRingtone(mC, mUri).play();
      // Log.d(TAG,"Ringtone playing");
      
      //Convert uri to bitmap... this is fluffing retarded.
      Bitmap bitmap = MediaStore.Images.Media.getBitmap(mC.getContentResolver(), imageUri);
      WM.setBitmap(bitmap);
    }
    catch(Exception e)
    {
      Log.d(TAG, "Error executing set ringtone");
      // error handling goes here -- also, use something other than Throwable
    }
  }

  @Override
  public String getInternalSaveString()
  {
    // TODO Auto-generated method stub
    return null;
  }

}
