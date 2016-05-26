package sagar.musicshare.utils;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Timer;

import sagar.musicshare.ShareBucket;

/**
 * Created by Sagar on 24-05-2016.
 */
public class SongPlayerServer implements TimerTaskUtil.Callback {

    static String songPath = null;
    static MediaPlayer player = null;



    public static void load(final Context context, String songPath, final OnSongLoadedListener callback){

        //Stop prev and Load new Song
        if(player!=null)
            player.stop();

        player = new MediaPlayer();
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            player.setDataSource(context, Uri.fromFile(new File(songPath)));
            Log.e("My Tag","Preparing Song");

            player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    Log.e("My Tag","Song Prepared");
                    callback.onSongLoaded();
                }
            });
            player.prepareAsync();
        } catch (IOException e) {
            Log.e("My Tag Song Prepare", e.getMessage());
            e.printStackTrace();
        }
        //-- Stop prev and Load new Song
    }

    public static void playSongWithDts(){
        //Play Song with DTS
        Timer timer = new Timer();
        TimerTaskUtil timerTaskUtil = new TimerTaskUtil(ShareBucket.getStartTimestamp(), new SongPlayerServer());
        timer.schedule(timerTaskUtil, new Date(ShareBucket.getStartTimestamp()));
        //-- Play Song with DTS
    }

    public static void start(){
        Log.e("My Tag","Playing Song");
//        player.reset();
        player.start();
        Log.e("My Tag","Song Started");
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

    @Override
    public void onTimeOut(boolean result) {
        Log.e("My Tag","Timed Out");
        start();
    }

    public interface OnSongLoadedListener{
        void onSongLoaded();
    }
}
