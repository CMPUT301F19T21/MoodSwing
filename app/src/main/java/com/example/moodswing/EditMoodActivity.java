package com.example.moodswing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.protobuf.Empty;

import java.io.Serializable;

public class EditMoodActivity extends AppCompatActivity implements AddMoodAdapter.ItemClickListener{

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
    private AddMoodAdapter adapter;

    FirestoreUserDocCommunicator communicator;
    String username;
    //Mood onclick
    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on item position " + position, Toast.LENGTH_SHORT).show();
        moodType = position;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_mood);

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
    }
}
