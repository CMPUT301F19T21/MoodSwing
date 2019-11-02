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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.example.moodswing.customDataTypes.AddMoodAdapter;
import com.example.moodswing.customDataTypes.DateJar;
import com.example.moodswing.customDataTypes.MoodEvent;
import com.example.moodswing.customDataTypes.TimeJar;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class NewMoodActivity extends AppCompatActivity implements AddMoodAdapter.ItemClickListener, AdapterView.OnItemSelectedListener {


    private ImageButton confirmButton;


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

    private Spinner socialSituationSpinner;
    private String socialSitToAdd;

    private TextView reasonTextView;
    private String reason;


    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on item position " + position, Toast.LENGTH_SHORT).show();
        moodState = position;
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
//        adapter = new AddMoodAdapter(this, viewColors, animalNames);
//        adapter.setClickListener(NewMoodActivity.this);
//        recyclerView.setAdapter(adapter);


        //Setting the date
        //month indexed 1 month behind for some reason
        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        int year = Calendar.getInstance().get(Calendar.YEAR);
        Log.v("dateCheck", "year:" + year + " day:" + day + " month:" + month);
        date = new DateJar(year, month, day);


        //creating the social situation. onitemselected and onnothingselected methods below handles functionality. Will switch to adapter later
        socialSituationSpinner = findViewById(R.id.SituationSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.socialSit, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        socialSituationSpinner.setAdapter(adapter);
        socialSituationSpinner.setOnItemSelectedListener(this);


        confirmButton = (ImageButton) findViewById(R.id.confirmNewMood);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int hours = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
                int minutes = Calendar.getInstance().get(Calendar.MINUTE);
                time = new TimeJar(hours, minutes);
                Log.v("SOMETHING", moodState + "");
                Log.v("dateCheck", "hours: " + hours + "minutes:" + minutes);


                reasonTextView = findViewById(R.id.reasonText);
                reason = reasonTextView.getText().toString();
                Log.v("dateCheck", reason);

                String[] temparray = reason.split(" ");
                if (temparray.length <= 3) {
//
//
////                        moodObj = new MoodEvent(moodState, date, time);
////                        Log.v("SOMETHING", moodObj.getDate().toString());
////                        returnIntent = new Intent();
////                        returnIntent.putExtra("result", moodObj);
////                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
//
                }
                }
            });
        }

        @Override
        public void onItemSelected (AdapterView < ? > adapterView, View view,int i, long l){
            if (!adapterView.getItemAtPosition(i).toString().equals("Select Social Situation")) {

                socialSitToAdd = adapterView.getItemAtPosition(i).toString();
            } else {
                socialSitToAdd = "";

            }
            Log.v("dateCheck", socialSitToAdd);

        }

        @Override
        public void onNothingSelected (AdapterView < ? > adapterView){

        }
    }
