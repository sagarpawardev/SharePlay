package sagar.musicshare.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.nononsenseapps.filepicker.FilePickerActivity;

import java.io.File;

import sagar.musicshare.R;
import sagar.musicshare.utils.SongSender;

public class CreatorActivity extends AppCompatActivity implements SongSender.Callback{

    private final int OPEN_REQUEST_CODE = 1;
    TextView tvFileName = null;
    public int numRequests = 0;
    TextView tvNumRequests = null;

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

        switch (requestCode) {
            case OPEN_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    //Open Sender Port
                    String filePath = data.getData().getPath();
                    File file = new File(filePath);
                    new SongSender(this, this).execute(file);
                    //-- Open Sender Port

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            playFile(data);
                        }
                    }, 3000);
                }
                break;
            default:
                Toast.makeText(this, "Cannot Start Session: Unsupported File Type", Toast.LENGTH_LONG).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    protected void playFile(Intent data){

        String filePath = data.getData().getPath();

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(filePath)), "audio/*");
        startActivity(intent);
    }

    @Override
    public void onSongSent(boolean result, File file) {
        //Update Count
        numRequests++;
        tvNumRequests.setText("Num Requests"+numRequests);
        //-- Update Count

        //Open Sender Port Again
        new SongSender(this, this).execute(file);
        //-- Open Sender Port Again
    }
}