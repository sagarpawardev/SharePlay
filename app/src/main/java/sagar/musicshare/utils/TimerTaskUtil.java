package sagar.musicshare.utils;

import android.content.Intent;
import android.util.Log;

import java.sql.Timestamp;
import java.util.TimerTask;

/**
 * Created by Sagar on 19-05-2016.
 */
public class TimerTaskUtil extends TimerTask {

    public long playTimeStamp;
    public Callback callback;
    public Intent data;

    public TimerTaskUtil(long playTimeStamp, Callback callback){
        this.playTimeStamp = playTimeStamp;
        this.callback = callback;
        this.data = data;
    }

    @Override
    public void run() {
        long currentTime = System.currentTimeMillis();
        Log.e("MyTag", "Current Time:" + new Timestamp(currentTime));
        if(currentTime >= playTimeStamp){
            callback.onTimeOut(true);
        }
    }

    public interface Callback{
        void onTimeOut(boolean result);
    }
}
