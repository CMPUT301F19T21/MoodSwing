package com.example.moodswing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewDebug;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.moodswing.customDataTypes.DateJar;
import com.example.moodswing.customDataTypes.FirestoreUserDocCommunicator;
import com.example.moodswing.customDataTypes.MoodEvent;
import com.example.moodswing.customDataTypes.MoodType;
import com.example.moodswing.customDataTypes.TimeJar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.Serializable;
import java.text.DateFormatSymbols;

public class MoodDetailActivity extends AppCompatActivity implements Serializable {
    MoodEvent moodEvent;
    private int moodType;
    private DateJar date;
    private TimeJar time;

    private String reason;
    private Integer socialSituation;

    private FirestoreUserDocCommunicator communicator;
    TextView dateText;
    TextView timeText;
    TextView moodText;
    TextView descriptionText;
    TextView socialText;
    ImageView moodImage;


    private ImageButton delButton;
    private ImageButton editButton;
    private FloatingActionButton confirmButton;

    private static boolean alreadyLoggedIn = true;
    int position;
    String UID;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mood_details);
        communicator = FirestoreUserDocCommunicator.getInstance();
        Intent moodIntent = getIntent();
        position = moodIntent.getIntExtra("position",-1);

        // find view
        dateText = findViewById(R.id.dateText);
        timeText = findViewById(R.id.timeText);
        moodText = findViewById(R.id.moodText);
        descriptionText = findViewById(R.id.descriptionText);
        delButton = findViewById(R.id.deleteButton);
        editButton = findViewById(R.id.editButton);
        moodImage = findViewById(R.id.moodImg);
        confirmButton = findViewById(R.id.confirm_Button);

        initial();

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MoodDetailActivity.this, EditMoodActivity.class);
                intent.putExtra("position",position);
                startActivity(intent);
                finish();
            }
        });

        delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                communicator.removeMoodEvent(moodEvent);
                finish();
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    private String getMonth(int month) {
        return new DateFormatSymbols().getMonths()[month-1];
    }
    private void setMoodImage(int moodType){
        switch(moodType){
            case 1:
                moodText.setText("HAPPY");
                moodImage.setImageResource(R.drawable.mood1);
                break;
            case 2:
                moodText.setText("SAD");
                moodImage.setImageResource(R.drawable.mood2);
                break;
            case 3:
                moodText.setText("ANGRY");
                moodImage.setImageResource(R.drawable.mood3);
                break;
            case 4:
                moodText.setText("EDMOTIONAL");
                moodImage.setImageResource(R.drawable.mood4);
                break;
        }
    }
    private void initial(){
        moodEvent = communicator.getMoodEvents().get(position);

        moodType = moodEvent.getMoodType();
        date = moodEvent.getDate();
        time = moodEvent.getTime();
        reason = moodEvent.getReason();



        int Hr = time.getHr();
        int Min = time.getMin();
        timeText.setText(Hr+":"+Min);
        int year = date.getYear();
        int Day = date.getDay();
        int month = date.getMonth();
        dateText.setText(getMonth(month)+","+Day+","+year);
        //moodText.setText(moodType);
        //socialText.setText(socialSituation);
        descriptionText.setText(reason);
        setMoodImage(moodType);
    }
}
