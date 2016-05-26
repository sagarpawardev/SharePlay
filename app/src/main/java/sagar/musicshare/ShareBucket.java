package sagar.musicshare;

/**
 * Created by Sagar on 23-05-2016.
 */
public class ShareBucket {
    private static String SONG_PATH="";
    private static long START_TIMESTAMP = 0L;
    private static int SEEK_POSITION = 0;

    public static String getSongPath() {
        return SONG_PATH;
    }

    public static void setSongPath(String songPath) {
        SONG_PATH = songPath;
    }

    public static long getStartTimestamp() {
        return START_TIMESTAMP;
    }

    public static void setStartTimestamp(long startTimestamp) {
        START_TIMESTAMP = startTimestamp;
    }

    public static int getSeekPosition() {
        return SEEK_POSITION;
    }

    public static void setSeekPosition(int seekPosition) {
        SEEK_POSITION = seekPosition;
    }
}
