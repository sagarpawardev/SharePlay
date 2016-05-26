package sagar.musicshare.utils;

import android.util.Log;

import java.sql.Timestamp;
import java.util.Calendar;

/**
 * Created by Sagar on 19-05-2016.
 */
public class SyncHelper {

    private static long playTimestamp;

    public void setDelayedTimestamp(int delayInSec){
        long tsInMillis = System.currentTimeMillis();

        Calendar calendar = Calendar.getInstance();
        Log.e("MyTag","Original Timestamp: " + new Timestamp(tsInMillis));
        calendar.setTimeInMillis(tsInMillis);
        calendar.add(Calendar.SECOND, delayInSec);

        playTimestamp = calendar.getTime().getTime();
        Log.e("MyTag","Delayed Timestamp: " + new Timestamp(playTimestamp));
    }

    public static long getPlayTimestamp(){
        return playTimestamp;
    }

    public static void setPlayTimestamp(long timestamp){
        playTimestamp = timestamp;
    }
}