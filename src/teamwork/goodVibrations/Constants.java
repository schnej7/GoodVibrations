package teamwork.goodVibrations;

public class Constants
{
  // TODO CHANGE BACK TO A FULL DAY
  
  // Controls the length of a day in the TimeTrigger
  //public static long dayInMillis = 86400000;
  public static final long dayInMillis = 30000;
  
  // A String label for the type of intent
  public static final String INTENT_TYPE = "INTENT_TYPE";
  
  // Used to determine if intent if a function or a trigger
  public static final int FUNCTION_TYPE = 0;
  public static final int TRIGGER_TYPE = 1;
  
  // Function types
  public static final int FUNCTION_TYPE_VOLUME = 0;
  public static final int FUNCTION_TYPE_RINGTONE = 1;
  
  // Trigger types
  public static final int TRIGGER_TYPE_TIME = 0;
  public static final int TRIGGER_TYPE_LOCATION = 1;
  
  // Labels for different types of values that are packed into intents
  public static final String INTENT_KEY_TYPE = "keytype1";
  public static final String INTENT_KEY_NAME = "keyname1";
  public static final String INTENT_KEY_VOLUME = "KEYVOL";
  public static final String INTENT_KEY_VIBRATE = "KEYVIBE";
  public static final String INTENT_KEY_BUNDLE = "BUNDLE";
  public static final String INTENT_KEY_URI = "URI";
  public static final String INTENT_KEY_LOCATION = "LOCATION";
  public static final String INTENT_KEY_TIME = "TIME";
  
  
  // Intent request codes.  Used in onActivityResult functions to determine which activity was returned
  public static final int REQUEST_CODE_RINGTONE_PICKER = 0;
  
  // Intent request codes.  Used in onActivityResult triggers to determine which activity was returned
  public static final int REQUEST_CODE_LOCATION = 0;
  public static final int REQUEST_CODE_TIME = 1;
  
}
