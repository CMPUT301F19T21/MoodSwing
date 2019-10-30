package com.example.moodswing;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Calendar;

public class NewMoodActivity extends AppCompatActivity implements AddMoodAdapter.ItemClickListener{


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

    private MoodEvent moodObj;

    private Intent returnIntent;

    private AddMoodAdapter adapter;

    private Integer selectedMood;


    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on item position " + position, Toast.LENGTH_SHORT).show();
        selectedMood = position;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_mood);

        ArrayList<Integer> viewColors = new ArrayList<>();
        viewColors.add(1);
        viewColors.add(2);
        viewColors.add(3);
        viewColors.add(4);

        ArrayList<String> animalNames = new ArrayList<>();
        animalNames.add("Happy");
        animalNames.add("Angry");
        animalNames.add("Emotional");
        animalNames.add("Sad");


        RecyclerView recyclerView = findViewById(R.id.addRecyclerView);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(NewMoodActivity.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManager);
        adapter = new AddMoodAdapter(this, viewColors, animalNames);
        adapter.setClickListener(NewMoodActivity.this);
        recyclerView.setAdapter(adapter);


        dateView = (TextView) findViewById(R.id.dateView);
        hoursEditText = (EditText) findViewById(R.id.Hours);
        minutesEditText = (EditText) findViewById(R.id.Minutes);


        //Initializing the date picker
        dateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                DatePickerDialog datepick = new DatePickerDialog(NewMoodActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, dateListener, year, month, day);
                datepick.show();
            }
        });

        dateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                date = new DateJar(year, month, day);
                Log.v("SOMETHING", year + "");
            }
        };


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

                        moodObj = new MoodEvent(moodState, date, time);
                        Log.v("SOMETHING", moodObj.getDate().toString());
                        returnIntent = new Intent();
                        returnIntent.putExtra("result", moodObj);
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                    }
                }
            }
        });
    }
}
