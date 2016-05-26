package sagar.musicshare;

import android.os.Environment;

/**
 * Created by Sagar on 18-05-2016.
 */
public class Globals {
    public final static int SONG_PORT = 7117;
    public final static int SYNC_PORT = 7107;

    public static String SERVER_IP="";

    public static String APP_DIRECTORY = Environment.getExternalStorageDirectory()+"/MusicShare";
    public static String TEMP_FILE_NAME = "temp.mp3";
    public static String TEMP_FILE_PATH = APP_DIRECTORY + "/" +TEMP_FILE_NAME;

    public static final int DELAY_IN_SEC = 2;

    public static final String TIMESTAMP_JSON_KEY = "timestamp";
    public static final String SEEK_POSITION_JSON_KEY = "seekPosition";

}
