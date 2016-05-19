package sagar.musicshare.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import sagar.musicshare.R;

public class ChoiceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice);

        Button btnCreateSession = (Button) findViewById(R.id.btnCreateSession);
        Button btnJoinSession = (Button) findViewById(R.id.btnJoinSession);

        btnCreateSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChoiceActivity.this, CreatorActivity.class);
                startActivity(intent);
            }
        });

        btnJoinSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChoiceActivity.this, JoinerActivity.class);
                startActivity(intent);
            }
        });
    }
}
