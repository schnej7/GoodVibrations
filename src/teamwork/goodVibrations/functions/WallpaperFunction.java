package teamwork.goodVibrations.functions;

import teamwork.goodVibrations.Constants;
import teamwork.goodVibrations.GoodVibrationsService;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;

// A function to set the wallpaper
public class WallpaperFunction extends Function
{
  private String TAG = "Wallpaper Function";
  private Context mC;           // A reference to the context
  private WallpaperManager WM;  // A reference to the wallpaper manager
  private Uri imageUri;         // A URI to the wallpaper image

  // Constructor when creating from service
  public WallpaperFunction(Bundle b, int newID)
  {
    Log.d(TAG, "RingtoneFunction() Constructor");
    mC = GoodVibrationsService.c;
    WM = WallpaperManager.getInstance(mC);
    imageUri = b.getParcelable(Constants.INTENT_KEY_IMAGEURI);
    name = b.getString(Constants.INTENT_KEY_NAME);
    if (b.getBoolean(Constants.INTENT_KEY_EDITED_BOOL))
    {
      id = b.getInt(Constants.INTENT_KEY_EDITED_ID);
    }
    else
    {
      id = newID;
    }
    type = Function.FunctionType.WALLPAPER;
  }

  // Constructor when remaking from persistent storage
  public WallpaperFunction(String s)
  {
    mC = GoodVibrationsService.c;
    WM = WallpaperManager.getInstance(mC);
    String[] categories = s.split(Constants.CATEGORY_DELIM);
    name = categories[0];
    id = new Integer(categories[1]).intValue();
    imageUri = Uri.parse(categories[2]);
    type = Function.FunctionType.WALLPAPER;
  }

  // execute
  // Does the actual changing of the wallpaper
  @Override
  public WallpaperFunction execute()
  {
    Log.d(TAG, "execute() - Setting Wallpaper to " + imageUri);
    WallpaperFunction inverse = getInverse();

    try
    {
      // Convert uri to bitmap
      Log.d(TAG, "Changing Wallpaper");

      Bitmap bitmap = MediaStore.Images.Media.getBitmap(
          mC.getContentResolver(), imageUri);
      WM.setBitmap(bitmap);
    }
    catch (Exception e)
    {
      Log.d(TAG, "Error executing set wallpaper");
      // error handling goes here -- also, use something other than Throwable
    }
    return inverse;
  }

  // getInverse
  // Note:  This function does not actually get the current wallpaper.
  //        This is because there is no access to it in the Android API
  //        Instead, this just returns the wallpaper that the function sets
  private WallpaperFunction getInverse()
  {
    Bundle b = new Bundle();

    b.putParcelable(Constants.INTENT_KEY_IMAGEURI, imageUri);
    b.putString(Constants.INTENT_KEY_NAME, name + "inv");

    WallpaperFunction inverse = new WallpaperFunction(b, id * -1);
    return inverse;
  }

  // getInternalSaveString
  // Gets the function as a string to save in the persistent storage 
  @Override
  public String getInternalSaveString()
  {
    String saveString = new String();
    saveString = name + Constants.CATEGORY_DELIM;
    saveString += id + Constants.CATEGORY_DELIM;
    saveString += imageUri.toString() + Constants.CATEGORY_DELIM;

    return saveString;
  }

  // getFunctionAsIntent
  // Returns the wallpaper as an intent which is used when editing the
  // wallpaper function in the GUI
  @Override
  public Intent getFunctionAsIntent()
  {
    Intent i = new Intent(Constants.SERVICE_MESSAGE);

    i.putExtra(Constants.INTENT_KEY_TYPE, Constants.FUNCTION_TYPE_WALLPAPER);
    i.putExtra(Constants.INTENT_KEY_NAME, name);
    i.putExtra(Constants.INTENT_KEY_IMAGEURI, imageUri);
    i.putExtra(Constants.INTENT_KEY_EDITED_ID, id);

    return i;
  }

}
