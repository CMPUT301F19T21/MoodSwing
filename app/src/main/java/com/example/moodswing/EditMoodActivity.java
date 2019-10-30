package com.example.moodswing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.protobuf.Empty;

import java.io.Serializable;

public class EditMoodActivity extends AppCompatActivity implements Serializable{

    EditText minuteText;
    EditText hourText;
    EditText reasonEditText;
    ImageButton happyButton;
    ImageButton angryButton;
    ImageButton sadButton;
    ImageButton emotionalButton;
    ImageButton confirmButton;

    MoodEvent moodEvent;
    private int moodType;
    private DateJar date;
    private TimeJar time;

    private String reason;
    private Integer socialSituation;

    FirestoreUserDocCommunicator communicator;
    String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_mood);

        final Intent intent = getIntent();
        username = intent.getStringExtra("UserName");
        moodEvent = (MoodEvent) intent.getSerializableExtra("MoodEvent");

        username = "1";

        communicator = FirestoreUserDocCommunicator.getInstance();

        hourText = findViewById(R.id.Hours);
        minuteText = findViewById(R.id.Minutes);
        //reasonEditText = findViewById(R.id.reasonEditText);
        //happyButton = findViewById(R.id.happy_button);
        //sadButton = findViewById(R.id.sad_button);
        //angryButton = findViewById(R.id.angry_button);
        //emotionalButton = findViewById(R.id.emotional_button);
        confirmButton = findViewById(R.id.confirmNewMood);



        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if (!dateText.getText().toString().equals("")){
                    String[] datetemp = dateText.getText().toString().split("-");
                    DateJar dateJar = new DateJar(Integer.parseInt(datetemp[0]),Integer.parseInt(datetemp[1]),Integer.parseInt(datetemp[2]));
                    moodEvent.setDate(dateJar);}*/
                String minutes = minuteText.getText().toString();
                String hours = hourText.getText().toString();
                if (!minutes.equals("") && !hours.equals("")){

                    TimeJar timeJar = new TimeJar(Integer.parseInt(minutes),Integer.parseInt(hours));
                    moodEvent.setTime(timeJar);}
                //reason = reasonEditText.getText().toString();
                //moodEvent.setReason(reason);
                communicator.editMood(moodEvent);
                Intent backIntent = new Intent(EditMoodActivity.this,MoodDetailActivity.class);
                backIntent.putExtra("UserName", username);
                backIntent.putExtra("MoodEvent",  moodEvent);
                startActivity(backIntent);
            }
        });
        happyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moodType = 0;
            }
        });
    }
}
