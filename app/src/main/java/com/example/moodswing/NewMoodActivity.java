package com.example.moodswing;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;



import java.util.Calendar;

public class NewMoodActivity extends AppCompatActivity {


    private EditText hoursEditText;
    private EditText minutesEditText;
    private ImageButton confirmButton;
    private int hours;
    private int minutes;

    private TextView dateView;
    private DatePickerDialog.OnDateSetListener dateListener;
    private DateJar date;
    private TimeJar time;

    private int moodState;
    private ImageButton happyMood;
    private ImageButton sadMood;
    private ImageButton emotionalMood;
    private ImageButton angryMood;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_mood);



        dateView = (TextView) findViewById(R.id.dateView);
        hoursEditText = (EditText) findViewById(R.id.addHours);
        minutesEditText = (EditText) findViewById(R.id.addMinutes);


        //Initializing the date picker

        dateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                DatePickerDialog datepick = new DatePickerDialog( NewMoodActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth,dateListener,year,month,day);
                datepick.show();
            }
        });

        dateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                date = new DateJar(year,month,day);
                Log.v("SOMETHING", year + "");
            }
        };


        //Setting the mood buttons to a listener, 1=happy,2=angry,3=emotional,4=sad

        happyMood = (ImageButton) findViewById(R.id.happy_button);
        angryMood = (ImageButton) findViewById(R.id.angry_button);
        emotionalMood = (ImageButton) findViewById(R.id.emotional_button);
        sadMood = (ImageButton) findViewById(R.id.sad_button);

        happyMood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moodState = 1;
            }
        });

        angryMood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moodState = 2;
            }
        });

        emotionalMood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moodState = 3;
            }
        });

        sadMood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moodState = 4;
            }
        });




        confirmButton = (ImageButton) findViewById(R.id.confirmNewMood);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!hoursEditText.getText().toString().matches("") && !minutesEditText.getText().toString().matches("")) {
                    hours = Integer.parseInt(hoursEditText.getText().toString());
                    minutes = Integer.parseInt(minutesEditText.getText().toString());
                    Log.v("SOMETHING", hours + "");
                    Log.v("SOMETHING", minutes + "");
                    time = new TimeJar(hours, minutes);
                    if (date != null && moodState != 0) {
                        Log.v("SOMETHING", moodState + "");
                        finish();
                    }
                }
            }
        });



















    }
}
