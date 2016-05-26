package sagar.musicshare.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.nononsenseapps.filepicker.FilePickerActivity;

import java.io.File;

import sagar.musicshare.Globals;
import sagar.musicshare.R;
import sagar.musicshare.ShareBucket;
import sagar.musicshare.utils.SongPlayerServer;
import sagar.musicshare.utils.asynctasks.SongSender;
import sagar.musicshare.utils.TimerTaskUtil;
import sagar.musicshare.utils.TimestampLogics;

public class CreatorActivity extends AppCompatActivity implements TimerTaskUtil.Callback, SongPlayerServer.OnSongLoadedListener{

    private final int OPEN_REQUEST_CODE = 1;
    TextView tvFileName = null;
    public int numRequests = 0;
    TextView tvNumRequests = null;

    MediaPlayer nextPlayer = null, prevPlayer = null;

    Intent data = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creator);
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

        Button bChoose = (Button)findViewById(R.id.bChoose);
        bChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Open File Chooser
                Intent intent = new Intent(CreatorActivity.this, FilePickerActivity.class);
                intent.putExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, false);
                intent.putExtra(FilePickerActivity.EXTRA_ALLOW_CREATE_DIR, false);
                intent.putExtra(FilePickerActivity.EXTRA_MODE, FilePickerActivity.MODE_FILE);
                intent.setType("audio/*");

                intent.putExtra(FilePickerActivity.EXTRA_START_PATH, Environment.getExternalStorageDirectory().getPath());
                startActivityForResult(intent, OPEN_REQUEST_CODE);
                //-- Open File Chooser
            }
        });

        tvNumRequests = (TextView) findViewById(R.id.tvNumRequests);
        //tvFileName = (TextView) findViewById(R.id.tvFileName);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {

        this.data = data;
        switch (requestCode) {
            case OPEN_REQUEST_CODE:
                if (resultCode == RESULT_OK) {

                    //-------------PARALLEL TASK 1/3-------------//
                    //Publish Song
                    String filePath = data.getData().getPath();
                    ShareBucket.setSongPath(filePath);
                    //-- Publish Song

                    //Load Song
                    SongPlayerServer.load(this, filePath, this);
                    //-- Load Song


                    //-------------PARALLEL TASK 2/3-------------//
                    //Open SongSender Port
                    File file = new File(filePath);
                    new SongSender(getApplicationContext()).execute(file);
                    //-- Open SongSender Port


                    //-------------PARALLEL TASK 3/3-------------//
                    //Open DTS publish port

                    //-- OPen DTS publish port
                    /*//Set Song scheduler
                    new SongHelper().playSongAfterDelay(CreatorActivity.this, Globals.DELAY_IN_SEC, data);
                    //-- Set Song Seduler*/
                }
                break;
            default:
                Toast.makeText(this, "Cannot Start Session: Unsupported File Type", Toast.LENGTH_LONG).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onTimeOut(boolean result) {
        Log.e("My Tag", "Starting Song..........");
        //Play Song
        nextPlayer.start();
        //-- Play Song
        Log.e("My Tag", "Song Started!!!!!!!!");
    }

    @Override
    public void onSongLoaded() {

        //Create DTS and Publish
        long dts = TimestampLogics.getDelayedTimestamp(Globals.DELAY_IN_SEC);
        ShareBucket.setStartTimestamp(dts);
        //-- Create DTS and Publish

        //Set Seek Position and Publish
        int seekPosition = 0;
        ShareBucket.setSeekPosition(seekPosition);
        //-- Set Seek Position and Publish

        Log.e("My Tag","Scheduling Song");
        //Play Song with DTS
        SongPlayerServer.playSongWithDts();
        //-- Play Song with DTS
        Log.e("My Tag","Song Scheduled");
    }
}