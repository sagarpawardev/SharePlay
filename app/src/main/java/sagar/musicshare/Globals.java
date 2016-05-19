package sagar.musicshare;

import android.os.Environment;

/**
 * Created by Sagar on 18-05-2016.
 */
public class Globals {
    public final static int SONG_PORT = 4112;
    public final static int SYNC_PORT = 4113;

    public static String SERVER_IP="";

    public static String APP_DIRECTORY = Environment.getExternalStorageDirectory()+"/MusicShare";
    public static String TEMP_FILE_NAME = "temp.mp3";
}
