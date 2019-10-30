package com.example.moodswing;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.protobuf.Empty;

import java.io.Serializable;
import java.util.Calendar;

public class EditMoodActivity extends AppCompatActivity implements Serializable{

    EditText minuteText;
    EditText hourText;
    TextView dateView;
    ImageButton happyButton;
    ImageButton angryButton;
    ImageButton sadButton;
    ImageButton emotionalButton;
    ImageButton confirmButton;

    MoodEvent moodEvent;
    private int moodType;
    private DateJar date;
    private TimeJar time;
    private DatePickerDialog.OnDateSetListener dateListener;

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

        communicator = new FirestoreUserDocCommunicator(username);

        hourText = findViewById(R.id.Hours);
        minuteText = findViewById(R.id.Minutes);
        happyButton = findViewById(R.id.happy_button);
        sadButton = findViewById(R.id.sad_button);
        angryButton = findViewById(R.id.angry_button);
        emotionalButton = findViewById(R.id.emotional_button);
        confirmButton = findViewById(R.id.confirmNewMood);
        dateView = findViewById(R.id.dateView);


        dateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                DatePickerDialog datepick = new DatePickerDialog( EditMoodActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth,dateListener,year,month,day);
                datepick.show();
            }
        });

        dateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                date = new DateJar(year,month,day);
                Log.v("SOMETHING", year + "");
                dateView.setText(Integer.toString(year));

            }
        };

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String minutes = minuteText.getText().toString();
                String hours = hourText.getText().toString();
                if (!minutes.equals("") && !hours.equals("")){
                    TimeJar timeJar = new TimeJar(Integer.parseInt(minutes),Integer.parseInt(hours));
                    moodEvent.setTime(timeJar);}
                if (date != null)
                    moodEvent.setDate(date);
                communicator.editMood(moodEvent);
                Intent backIntent = new Intent();
                backIntent.putExtra("UserName", username);
                backIntent.putExtra("MoodEvent",  moodEvent);
                setResult(Activity.RESULT_OK,backIntent);
                finish();
            }
        });

        happyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moodType = 1;
            }
        });
        angryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moodType = 2;
            }
        });

        emotionalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moodType = 3;
            }
        });

        sadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moodType = 4;
            }
        });
    }
}
