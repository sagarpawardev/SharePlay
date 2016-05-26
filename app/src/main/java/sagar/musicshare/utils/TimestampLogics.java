package sagar.musicshare.utils;

import android.util.Log;

import java.sql.Timestamp;
import java.util.Calendar;

/**
 * Created by Sagar on 23-05-2016.
 */
public class TimestampLogics {
    public static long getDelayedTimestamp(int delayInSec){
        long tsInMillis = System.currentTimeMillis();

        Calendar calendar = Calendar.getInstance();
        Log.e("MyTag","Original Timestamp: " + new Timestamp(tsInMillis));
        calendar.setTimeInMillis(tsInMillis);
        calendar.add(Calendar.SECOND, delayInSec);

        long playTimestamp = calendar.getTime().getTime();
        Log.e("MyTag","Delayed Timestamp: " + new Timestamp(playTimestamp));
        return playTimestamp;
    }
}
