package com.example.moodswing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class EditMoodActivity extends AppCompatActivity {

    EditText dateText;
    EditText timeText;
    EditText reasonEditText;
    ImageButton locationButton;
    TextView loctionText;

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

        Intent intent = getIntent();
        communicator = (FirestoreUserDocCommunicator) intent.getSerializableExtra("communicator");
        UID = intent.getStringExtra("UID");



    }
}
