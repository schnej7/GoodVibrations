package teamwork.goodVibrations;

public class Constants
{
  // TODO CHANGE BACK TO A FULL DAY
  
  // Controls the length of a day in the TimeTrigger
  //public static long dayInMillis = 86400000;
  public static final long dayInMillis = 30000;
  
  // A String label for the type of intent
  public static final String INTENT_TYPE = "INTENT_TYPE";
  
  // Used to determine if intent if a function or a trigger for INTENT_TYPE
  public static final int FUNCTION_TYPE = 0;
  public static final int TRIGGER_TYPE = 1;
  
  // Function types for INTENT_KEY_TYPE
  public static final int FUNCTION_TYPE_VOLUME = 0;
  public static final int FUNCTION_TYPE_RINGTONE = 1;
  
  // Trigger types for INTENT_KEY_TYPE
  public static final int TRIGGER_TYPE_TIME = 0;
  public static final int TRIGGER_TYPE_LOCATION = 1;
  
  // Labels for different types of values that are packed into intents
  public static final String INTENT_KEY_TYPE = "100";
  public static final String INTENT_KEY_NAME = "101";
  public static final String INTENT_KEY_VOLUME = "102";
  public static final String INTENT_KEY_VIBRATE = "103";
  //public static final String INTENT_KEY_BUNDLE = "104";
  public static final String INTENT_KEY_URI = "105";
  public static final String INTENT_KEY_LOCATION = "106";
  public static final String INTENT_KEY_START_TIME = "107";
  public static final String INTENT_KEY_END_TIME = "108";
  public static final String INTENT_KEY_REPEAT_DAYS_BOOL = "109";
  public static final String INTENT_KEY_REPEAT_DAYS_BYTE = "110";
  public static final String INTENT_KEY_RADIUS = "111";  
  public static final String MAP_API_KEY = "0UvdZCYV5hFgYk_rTiVtYv14xstUVf0fV2jHLhQ";
  
  // Intent request codes.  Used in onActivityResult functions to determine which activity was returned
  public static final int REQUEST_CODE_RINGTONE_PICKER = 0;
  
  // Intent request codes.  Used in onActivityResult triggers to determine which activity was returned
  public static final int REQUEST_CODE_LOCATION = 0;
  public static final int REQUEST_CODE_TIME = 1;
  
}
