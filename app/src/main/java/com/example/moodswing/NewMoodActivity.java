package com.example.moodswing;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.moodswing.customDataTypes.FirestoreUserDocCommunicator;
import com.example.moodswing.customDataTypes.MoodEvent;
import com.example.moodswing.customDataTypes.SelectMoodAdapter;
import com.example.moodswing.customDataTypes.DateJar;
import com.example.moodswing.customDataTypes.SocialSituationItem;
import com.example.moodswing.customDataTypes.SpinnerAdapter;
import com.example.moodswing.customDataTypes.TimeJar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class NewMoodActivity extends AppCompatActivity {
////    private ArrayList<SocialSituationItem> mSocialList;
////    private SpinnerAdapter spinnerAdapter;
////    private Spinner socialSituationSpinner;
////    private String socialSitToAdd;

    private FloatingActionButton confirmButton;
    private ImageView locationCheckButton;
    private ImageView addNewImageButton;
    private EditText reasonEditText;
    private TextView dateTextView;
    private TextView timeTextView;

    private RecyclerView moodSelectList;
    private RecyclerView.LayoutManager recyclerViewLayoutManager;
    private SelectMoodAdapter moodSelectAdapter;

    private FirestoreUserDocCommunicator communicator;
    private MoodEvent moodEvent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_mood);

        // find view
        confirmButton = findViewById(R.id.add_confirm);
        addNewImageButton = findViewById(R.id.add_newImage);
        reasonEditText = findViewById(R.id.reason_EditView);
        dateTextView = findViewById(R.id.add_date);
        timeTextView = findViewById(R.id.add_time);
        moodSelectList = findViewById(R.id.moodSelect_recycler);


        // recyclerView
        recyclerViewLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        moodSelectAdapter = new SelectMoodAdapter();
        moodSelectList.setLayoutManager(recyclerViewLayoutManager);
        moodSelectList.setAdapter(moodSelectAdapter);

        // init communicator
        communicator = FirestoreUserDocCommunicator.getInstance();
        moodEvent = new MoodEvent();

        // set up current date and time
        Calendar calendar = Calendar.getInstance();

            // set date and time
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        int hr = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);
        DateJar date = new DateJar(year,month,day);
        TimeJar time = new TimeJar(hr,min);

        moodEvent.setDate(date);
        moodEvent.setTime(time);
            // set date and time for display
        dateTextView.setText(getDateStr(date));
        timeTextView.setText(getTimeStr(time));

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (moodSelectAdapter.getSelectedMoodType() != null) {
                    // do upload
                    moodEvent.setUniqueID(communicator.generateMoodID());
                    moodEvent.setMoodType(moodSelectAdapter.getSelectedMoodType());
                    communicator.addMoodEvent(moodEvent);
                    finish();
                }else{
                    // prompt user to select a mood
                }
            }
        });
    }

    public String getDateStr (DateJar date) {
        String month = returnMonthStr(date.getMonth());
        return String.format(Locale.getDefault(), "%s %d, %d",month,date.getDay(),date.getYear());
    }

    public String getTimeStr (TimeJar time) {
        int hr = time.getHr();
        String period;
        if (hr>12){
            hr =hr-12;
            period = "PM";
        }
        else period = "AM";
        return String.format(Locale.getDefault(), "%d:%02d %s",hr,time.getMin(),period);
    }

    public String returnMonthStr(int monthInt){
        String monthStr = null;
        switch (monthInt){
            case 0:
                monthStr = "January";
                break;
            case 1:
                monthStr = "February";
                break;
            case 2:
                monthStr = "March";
                break;
            case 3:
                monthStr = "April";
                break;
            case 4:
                monthStr = "May";
                break;
            case 5:
                monthStr = "June";
                break;
            case 6:
                monthStr = "July";
                break;
            case 7:
                monthStr = "August";
                break;
            case 8:
                monthStr = "September";
                break;
            case 9:
                monthStr = "October";
                break;
            case 10:
                monthStr = "November";
                break;
            case 11:
                monthStr = "December";
                break;
        }
        return monthStr;
    }
}

////        ArrayList<Integer> viewColors = new ArrayList<>();
////        viewColors.add(1);
////        viewColors.add(2);
////        viewColors.add(3);
////        viewColors.add(4);
////
////        ArrayList<String> animalNames = new ArrayList<>();
////        animalNames.add("Happy");
////        animalNames.add("Angry");
////        animalNames.add("Emotional");
////        animalNames.add("Sad");
//
//
//        RecyclerView recyclerView = findViewById(R.id.addRecyclerView);
//        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(NewMoodActivity.this, LinearLayoutManager.HORIZONTAL, false);
//        recyclerView.setLayoutManager(horizontalLayoutManager);
////        adapter = new SelectMoodAdapter(this, viewColors, animalNames);
////        adapter.setClickListener(NewMoodActivity.this);
////        recyclerView.setAdapter(adapter);
//
//
//        //Setting the date
//        //month indexed 1 month behind for some reason
//        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
//        int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
//        int year = Calendar.getInstance().get(Calendar.YEAR);
//        Log.v("dateCheck", "year:" + year + " day:" + day + " month:" + month);
//        date = new DateJar(year, month, day);
//
//
//
//
//        mSocialList = new ArrayList<>();
//        mSocialList.add(new SocialSituationItem("Select Social Situation", 0));
//        mSocialList.add(new SocialSituationItem("Alone", R.drawable.aloneicon));
//        mSocialList.add(new SocialSituationItem("With One Person", R.drawable.onepersonicon));
//        mSocialList.add(new SocialSituationItem("With 2-7 People", R.drawable.twoplusicon));
//        mSocialList.add(new SocialSituationItem("With a Crowd", R.drawable.crowdicon));
//
//        socialSituationSpinner = findViewById(R.id.SituationSpinner);
//        spinnerAdapter = new SpinnerAdapter(this, mSocialList);
//        socialSituationSpinner.setAdapter(spinnerAdapter);
//
//        socialSituationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                SocialSituationItem current = (SocialSituationItem) adapterView.getItemAtPosition(i);
//                socialSitToAdd = current.getSituation();
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
//
//
//
//
//        confirmButton = (ImageButton) findViewById(R.id.confirmNewMood);
//        confirmButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                int hours = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
//                int minutes = Calendar.getInstance().get(Calendar.MINUTE);
//                time = new TimeJar(hours, minutes);
//                Log.v("SOMETHING", moodState + "");
//                Log.v("dateCheck", "hours: " + hours + "minutes:" + minutes);
//
//
//                reasonTextView = findViewById(R.id.reasonText);
//                reason = reasonTextView.getText().toString();
//                Log.v("dateCheck", reason);
//                Log.v("dateCheck", socialSitToAdd);
//
//                String[] temparray = reason.split(" ");
//                if (temparray.length <= 3) {
////
////
//////                        moodObj = new MoodEvent(moodState, date, time);
//////                        Log.v("SOMETHING", moodObj.getDate().toString());
//////                        returnIntent = new Intent();
//////                        returnIntent.putExtra("result", moodObj);
//////                        setResult(Activity.RESULT_OK, returnIntent);
//                        finish();
////
//                }
//                }
//            });
//        }
