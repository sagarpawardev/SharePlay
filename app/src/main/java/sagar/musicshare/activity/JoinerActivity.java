package sagar.musicshare.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;

import sagar.musicshare.Globals;
import sagar.musicshare.R;
import sagar.musicshare.utils.SongReceiver;

public class JoinerActivity extends AppCompatActivity implements SongReceiver.Callback{

    SongReceiver receiver = null;

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

        Button btn = (Button) findViewById(R.id.btnStart);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Send Song Request
                String str = etServerIp.getText().toString();
                receiver = new SongReceiver(str, JoinerActivity.this);
                receiver.execute();
                Toast.makeText(JoinerActivity.this, "Request Sent", Toast.LENGTH_SHORT).show();
                //-- Send Song Request
            }
        });
    }

    protected void playFile(){

        String filePath = Globals.APP_DIRECTORY + "/" + Globals.TEMP_FILE_NAME;

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(filePath)), "audio/*");
        startActivity(intent);
    }

    @Override
    public void onSongReceive() {
        //Request Time
        //-- Request Time

        playFile();
    }
}
