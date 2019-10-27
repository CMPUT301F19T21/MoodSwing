package com.example.moodswing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.Serializable;

public class EditMoodActivity extends AppCompatActivity implements Serializable{

    EditText dateText;
    EditText timeText;
    EditText reasonEditText;
    ImageButton locationButton;
    TextView loctionText;
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
    String UID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_mood);

        final Intent intent = getIntent();
        communicator = (FirestoreUserDocCommunicator) intent.getSerializableExtra("communicator");
        moodEvent = (MoodEvent) intent.getSerializableExtra("MoodEvent");

        dateText = findViewById(R.id.dateText);
        timeText = findViewById(R.id.timeText);
        reasonEditText = findViewById(R.id.reasonEditText);
        locationButton = findViewById(R.id.locationButton);
        loctionText = findViewById(R.id.locationText);
        happyButton = findViewById(R.id.happy_button);
        sadButton = findViewById(R.id.sad_button);
        angryButton = findViewById(R.id.angry_button);
        emotionalButton = findViewById(R.id.emotional_button);
        confirmButton = findViewById(R.id.confirmButton);

        String datetemp = dateText.getText().toString();

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backIntent = new Intent(EditMoodActivity.this,MoodDetailActivity.class);
                backIntent.putExtra("communicator", (Serializable) communicator);
                backIntent.putExtra("MoodEvent", (Serializable) moodEvent);
                startActivity(backIntent);
            }
        });
    }
}
