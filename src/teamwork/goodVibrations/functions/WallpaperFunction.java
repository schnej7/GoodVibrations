package teamwork.goodVibrations.functions;

import teamwork.goodVibrations.Constants;
import teamwork.goodVibrations.GoodVibrationsService;
import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.AudioManager;
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
 
  public WallpaperFunction(Bundle b,int newID)
  {
    Log.d(TAG,"RingtoneFunction() Constructor");
    mC = GoodVibrationsService.c;
    WM = WallpaperManager.getInstance(mC);
    imageUri = b.getParcelable(Constants.INTENT_KEY_IMAGEURI);
    name = b.getString(Constants.INTENT_KEY_NAME);
    id = newID;
    type = Function.FunctionType.WALLPAPER;
  }
  
  public WallpaperFunction(String s)
  {
    mC = GoodVibrationsService.c;
    WM = WallpaperManager.getInstance(mC);
    String [] categories = s.split(Constants.CATEGORY_DELIM);
    name = categories[0];
    id = new Integer(categories[1]).intValue();
    imageUri = Uri.parse(categories[2]);

    type = Function.FunctionType.WALLPAPER;
  }
  
  @Override
  public void execute()
  {
    Log.d(TAG, "execute() - Setting Wallpaper to " + imageUri);
    
    try
    {
      // RingtoneManager.getRingtone(mC, mUri).play();
      // Log.d(TAG,"Ringtone playing");
      
      //Convert uri to bitmap
      Log.d(TAG,"Changing Wallpaper");
      Bitmap bitmap = MediaStore.Images.Media.getBitmap(mC.getContentResolver(), imageUri);
      WM.setBitmap(bitmap);
    }
    catch(Exception e)
    {
      Log.d(TAG, "Error executing set wallpaper");
      // error handling goes here -- also, use something other than Throwable
    }
  }

  @Override
  public String getInternalSaveString()
  {
    String saveString = new String();
    saveString = name + Constants.CATEGORY_DELIM;
    saveString += id  + Constants.CATEGORY_DELIM;
    saveString += imageUri.toString();
    
    return saveString;
  }

}
