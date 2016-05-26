package sagar.musicshare.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import java.io.File;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Timer;

import sagar.musicshare.Globals;

/**
 * Created by Sagar on 19-05-2016.
 */
public class SongHelper implements TimerTaskUtil.Callback{

    Timer timer;
    Intent data = null;
    Activity activity;

    public void playSongAfterDelay(Activity activity, int delayInSec, Intent data){
        this.activity = activity;
        this.data = data;

        new SyncHelper().setDelayedTimestamp(delayInSec);
        long delayedTimestamp = SyncHelper.getPlayTimestamp();
        timer = new Timer();
        TimerTaskUtil timerTaskUtil = new TimerTaskUtil(delayedTimestamp, this);
        timer.schedule(timerTaskUtil, new Date(delayedTimestamp));

        Log.e("MyTag","Song will play at:" + new Timestamp(SyncHelper.getPlayTimestamp()));
    }

    public void playFile(){
        String filePath = data.getData().getPath();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(filePath)), "audio/*");
        activity.startActivity(intent);
    }

    public void playSong(Activity activity, long delayedTimestamp){

        String filePath = Globals.APP_DIRECTORY + "/" + Globals.TEMP_FILE_NAME;

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(filePath)), "audio/*");
        activity.startActivity(intent);

        timer = new Timer();
        TimerTaskUtil timerTaskUtil = new TimerTaskUtil(delayedTimestamp, this);
        timer.schedule(timerTaskUtil, new Date(delayedTimestamp));

        Log.e("MyTag","Song will play at:" + new Timestamp(SyncHelper.getPlayTimestamp()));
    }

    @Override
    public void onTimeOut(boolean result) {
        if(timer != null)
            timer.cancel();
        playFile();
    }
}
