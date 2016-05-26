package sagar.musicshare.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.concurrent.Executor;

import sagar.musicshare.Globals;
import sagar.musicshare.R;
import sagar.musicshare.ShareBucket;
import sagar.musicshare.utils.SongHelper;
import sagar.musicshare.utils.SongPlayerClient;
import sagar.musicshare.utils.TimerTaskUtil;
import sagar.musicshare.utils.asynctasks.SongReceiver;
import sagar.musicshare.utils.SyncHelper;
import sagar.musicshare.utils.asynctasks.TimestampReceiver;

public class JoinerActivity extends AppCompatActivity
        implements SongReceiver.Callback, SongPlayerClient.OnSongLoadedListener, TimestampReceiver.Callback{

    SongReceiver receiver = null;
    TimestampReceiver dtsReceiver = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joiner);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        final AppCompatEditText etServerIp = (AppCompatEditText) findViewById(R.id.etServerIp);

        //Look For Running Services
        //-- Look For Running Services

        //Download Song
        Button btn = (Button) findViewById(R.id.btnStart);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Find Server IP
                String serverIp = etServerIp.getText().toString();
                //-- Find Server IP

                //Publish IP
                Globals.SERVER_IP = serverIp;
                //-- Publish IP

                //Reset Status
                SongPlayerClient.resetStatus();
                //-- Resete Status

                //Download Song
                receiver = new SongReceiver(Globals.SERVER_IP, JoinerActivity.this);
                receiver.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                Toast.makeText(JoinerActivity.this, "Request Sent", Toast.LENGTH_SHORT).show();
                //-- Download Song
            }
        });
        //-- Download Song

    }

    @Override
    public void onSongReceive() {

        //----------------PARALLEL TASK 1/2--------------//
        //Publish Song Path
        ShareBucket.setSongPath(Globals.TEMP_FILE_PATH);
        //-- Publish Song Path

        //Load Song
        SongPlayerClient.load(this, Globals.TEMP_FILE_PATH, this);
        //-- Load Song

        //---------------PARALLEL TASK 2/2---------------//
        //Request DTS
        dtsReceiver = new TimestampReceiver(Globals.SERVER_IP, this);
        dtsReceiver.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        //-- Request DTS
    }

    @Override
    public void onSongLoaded() {
        //Set Load Status
        SongPlayerClient.songLoaded();
        //-- Set Load Status

        //Schedule Song
        SongPlayerClient.playSongWithDts();
        //-- Schedule Song
    }

    @Override
    public void onTimestampReceive(long timestamp, int seekPosition) {
        //Publish DTS and Seek Position
        ShareBucket.setStartTimestamp(timestamp);
        ShareBucket.setSeekPosition(seekPosition);
        //-- Publish DTS and Seek Position

        //Set DTS Status
        SongPlayerClient.dtsReady();
        //-- Set DTS Status

        //Try to play song
        SongPlayerClient.playSongWithDts();
        //-- Try to play Song
    }
}