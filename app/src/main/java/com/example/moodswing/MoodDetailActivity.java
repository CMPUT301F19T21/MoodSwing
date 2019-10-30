package com.example.moodswing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewDebug;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.Serializable;

public class MoodDetailActivity extends AppCompatActivity implements Serializable {
    MoodEvent moodEvent;
    private int moodType;
    private DateJar date;
    private TimeJar time;

    private String reason;
    private Integer socialSituation;


    TextView dateText;
    TextView timeText;
    TextView moodText;
    TextView descriptionText;
    TextView socialText;


    private ImageButton delButton;
    private ImageButton editButton;

    private static boolean alreadyLoggedIn = true;

    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mood_details);
        Intent moodIntent = getIntent();
        username = moodIntent.getStringExtra("UserName");
        moodEvent = (MoodEvent) moodIntent.getSerializableExtra("MoodEvent");

        //test case
        username = "1";
        DateJar dateJar = new DateJar(2000,11,1);
        TimeJar timeJar = new TimeJar(11,11);
        if (moodEvent == null)
            moodEvent = new MoodEvent(1,dateJar,timeJar);
        final FirestoreUserDocCommunicator communicator = FirestoreUserDocCommunicator.getInstance();

        moodType = moodEvent.getMoodType();
        date = moodEvent.getDate();
        time = moodEvent.getTime();
        reason = moodEvent.getReason();
        socialSituation = moodEvent.getSocialSituation();

        socialText = findViewById(R.id.socialTxt);
        dateText = findViewById(R.id.dateText);
        timeText = findViewById(R.id.timeText);
        moodText = findViewById(R.id.moodText);
        descriptionText = findViewById(R.id.descriptionText);
        delButton = findViewById(R.id.deleteButton);
        editButton = findViewById(R.id.editButton);

        int Hr = time.getHr();
        int Min = time.getMin();
        timeText.setText(Hr+":"+Min);
        String month = date.getMonthName();
        int year = date.getYear();
        int Day = date.getDay();
        dateText.setText(month+" "+Day+", "+year);
        //moodText.setText(moodType);
        //socialText.setText(socialSituation);
        descriptionText.setText(reason);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MoodDetailActivity.this, EditMoodActivity.class);
                intent.putExtra("UserName", username);
                intent.putExtra("MoodEvent",(Serializable) moodEvent);
                startActivity(intent);
            }
        });
        delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                communicator.removeMoodEvent(moodEvent.getUniqueID());
                //Intent intent = new Intent(MoodDetailActivity.this,MoodHistoryActivity.class);
                //startActivity(intent);
            }
        });
    }
}
