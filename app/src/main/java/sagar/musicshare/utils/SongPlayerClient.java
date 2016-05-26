package sagar.musicshare.utils;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Timer;

import sagar.musicshare.Globals;
import sagar.musicshare.ShareBucket;

/**
 * Created by Sagar on 26-05-2016.
 */
public class SongPlayerClient implements TimerTaskUtil.Callback {

    static String songPath = null;
    static MediaPlayer player = null;
    static boolean hasDts = false;
    static boolean hasLoaded = false;
    static Timer timer = null;

    public static void load(final Context context, String songPath, final OnSongLoadedListener callback){

        //Stop prev and Load new Song
        if(player!=null)
            player.stop();

        player = new MediaPlayer();
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            player.setDataSource(context, Uri.fromFile(new File(songPath)));
            player.prepareAsync();
            Log.e("My Tag","Preparing Song");

            player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    Log.e("My Tag","Song Prepared");
                    callback.onSongLoaded();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        //-- Stop prev and Load new Song
    }

    synchronized public static void playSongWithDts(){
        //Play Song with DTS
        if(isReady()) {

            long dts = ShareBucket.getStartTimestamp();
            long currentTs = System.currentTimeMillis();
            Log.e("My Tag", "DTS: "+new Timestamp(dts)+" Current: "+new Timestamp(currentTs));
            if(dts > currentTs) {
                timer = new Timer();
                TimerTaskUtil timerTaskUtil = new TimerTaskUtil(ShareBucket.getStartTimestamp(), new SongPlayerClient());
                timer.schedule(timerTaskUtil, new Date(ShareBucket.getStartTimestamp()));
                Log.e("My Tag", "song scheduled");
            }
            else{
                Log.e("My Tag", "Directly Playing");
                play();
            }
        }
        //-- Play Song with DTS
    }

    public static void play(){
        if(isReady()) {

            long dts = ShareBucket.getStartTimestamp();
            long currentTs = System.currentTimeMillis();

            if(dts < currentTs){
                int diff = (int)(currentTs - dts);
                Log.e("My Tag", "Diff: "+diff);
                player.seekTo(diff);
            }

            Log.e("My Tag", "Playing Song");
            player.start();
            Log.e("My Tag", "Song Started");
        }
    }

    public static void pause(){
        player.pause();
    }

    public static void resume(){
        player.start();
    }

    public static void stop(){
        player.stop();
    }

    public static void resetStatus(){
        hasDts = false;
        hasLoaded = false;
    }

    public static void dtsReady(){
        hasDts = true;
    }

    public static void songLoaded(){
        hasLoaded = true;
    }

    public static boolean isReady(){
        return hasDts && hasLoaded;
    }

    @Override
    public void onTimeOut(boolean result) {
        Log.e("My Tag","Timed Out");
        play();
    }

    public interface OnSongLoadedListener{
        void onSongLoaded();
    }
}

