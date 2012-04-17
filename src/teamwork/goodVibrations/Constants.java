package teamwork.goodVibrations;

public class Constants
{
  // Controls the length of a day in the TimeTrigger
  public static long dayInMillis = 86400000;
  // public static final long dayInMillis = 30000; // A 30-second day.

  // A String label for the type of intent
  public static final String INTENT_TYPE = "INTENT_TYPE";

  // Used to determine if intent if a function, trigger, get data for
  // INTENT_TYPE
  public static final int FUNCTION_TYPE = 0;
  public static final int TRIGGER_TYPE = 1;
  public static final int GET_DATA = 2; // Tells the service that we want it to
                                        // send the activity data. See
                                        // INTENT_KEY_TYPE for type of desired
                                        // type of data

  // Function types for INTENT_KEY_TYPE
  public static final int FUNCTION_TYPE_VOLUME = 0;
  public static final int FUNCTION_TYPE_RINGTONE = 1;
  public static final int FUNCTION_TYPE_WALLPAPER = 2;

  // Trigger types for INTENT_KEY_TYPE
  public static final int TRIGGER_TYPE_TIME = 0;
  public static final int TRIGGER_TYPE_LOCATION = 1;

  // GetData types for INTENT_KEY_TYPE
  public static final int INTENT_KEY_TRIGGER_LIST = 0;
  public static final int INTENT_KEY_FUNCTION_LIST = 1;
  
  //GetData types for Image Selection
  public static final int PICK_FROM_FILE = 0;

  // Labels for different types of values that are packed into intents
  public static final String INTENT_KEY_TYPE = "100";
  public static final String INTENT_KEY_NAME = "101";
  public static final String INTENT_KEY_VOLUME = "102";
  public static final String INTENT_KEY_VIBRATE = "103";
  // public static final String INTENT_KEY_BUNDLE = "104";
  public static final String INTENT_KEY_URI = "105";
  public static final String INTENT_KEY_LOCATION = "106";
  public static final String INTENT_KEY_START_TIME = "107";
  public static final String INTENT_KEY_END_TIME = "108";
  public static final String INTENT_KEY_REPEAT_DAYS_BOOL = "109";
  public static final String INTENT_KEY_REPEAT_DAYS_BYTE = "110";
  public static final String INTENT_KEY_RADIUS = "111";
  public static final String INTENT_KEY_FUNCTIONS = "112";
  public static final String INTENT_KEY_TRIGGERS = "113";
  public static final String INTENT_KEY_FUNCTION_NAMES = "114";
  public static final String INTENT_KEY_FUNCTION_IDS = "115";
  public static final String INTENT_KEY_TRIGGER_NAMES = "116";
  public static final String INTENT_KEY_TRIGGER_IDS = "117";
  public static final String INTENT_KEY_DATA_LENGTH = "118";
  public static final String INTENT_KEY_START_FUNCTION_IDS = "119";
  public static final String INTENT_KEY_STOP_FUNCTION_IDS = "120";
  public static final String INTENT_KEY_LONGITUDE = "121";
  public static final String INTENT_KEY_LATITUDE = "122";
  public static final String INTENT_KEY_VOLUME_TYPES = "123";
  public static final String INTENT_KEY_IMAGEURI = "124";

  // Intent request codes. Used in onActivityResult triggers to determine which
  // activity was returned
  public static final int REQUEST_CODE_LOCATION = 0;
  public static final int REQUEST_CODE_TIME = 1;
  public static final int REQUEST_CODE_SET_TIMES_ACTIVITY = 2;
  public static final int REQUEST_CODE_DAY_PICKER = 3;
  public static final int REQUEST_CODE_SET_FUNCTION_IDS = 4;
  public static final int REQUEST_CODE_RINGTONE_PICKER = 5;
  public static final int REQUEST_CODE_WALLPAPER_PICKER = 6;

  // Used to broadcast messages from the service to the activity
  public static final String SERVICE_DATA_TRIGGER_MESSAGE = "serviceDataTriggerMessage";
  public static final String SERVICE_DATA_FUNCTION_MESSAGE = "serviceDataFunctionMessage";
  
  public static final String LIST_DELIM = "#";
  public static final String CATEGORY_DELIM = "@";
  public static final String SAVE_STRING_DELIM = "%";
  
  public static final String TEAM_DIR_PATH = "teamwork";
  public static final String FUNC_DIR_PATH = "teamwork/functions";
  public static final String TRIG_DIR_PATH = "teamwork/triggers";
  public static final String FUNC_LIST_NAME = "Function List.txt";
  public static final String TRIG_LIST_NAME = "Trigger List.txt";

}
