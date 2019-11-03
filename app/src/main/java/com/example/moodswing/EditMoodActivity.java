package com.example.moodswing;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

// import com.example.moodswing.customDataTypes.AddMoodAdapter;
import com.example.moodswing.customDataTypes.DateJar;
import com.example.moodswing.customDataTypes.FirestoreUserDocCommunicator;
import com.example.moodswing.customDataTypes.MoodEvent;

import com.example.moodswing.customDataTypes.MoodEventUtility;

import com.example.moodswing.customDataTypes.SelectMoodAdapter;
import com.example.moodswing.customDataTypes.TimeJar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Locale;

public class EditMoodActivity extends AppCompatActivity {
    TextView timeText;


    MoodEvent moodEvent;
    private int moodType;
    private DateJar date;
    private TimeJar time;
    private FloatingActionButton confirmButton;
    private ImageView locationCheckButton;
    private ImageView addNewImageButton;
    private EditText reasonEditText;
    private TextView dateTextView;
    private TextView timeTextView;
    private String period;
    private RecyclerView moodSelectList;
    private RecyclerView.LayoutManager recyclerViewLayoutManager;
    private SelectMoodAdapter moodSelectAdapter;
    private RecyclerView moodView;
    private RecyclerView.Adapter moodAdapter;
    private RecyclerView.LayoutManager manager;
    private String social;
    private Spinner socialSpinner;

    FirestoreUserDocCommunicator communicator;
    String username;
    int position;
    //@Override
    //public void onItemClick(View view, int position) {
     //   Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on item position " + position, Toast.LENGTH_SHORT).show();
     //   moodType = position;
    //}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_mood);
        communicator = FirestoreUserDocCommunicator.getInstance();
        Intent moodIntent = getIntent();
        position = moodIntent.getIntExtra("position",-1);
        moodEvent = communicator.getMoodEvent(position);

        // find view
        confirmButton = findViewById(R.id.add_confirm);
        addNewImageButton = findViewById(R.id.add_newImage);
        reasonEditText = findViewById(R.id.reason_EditView);
        dateTextView = findViewById(R.id.add_date);
        timeTextView = findViewById(R.id.add_time);
        moodSelectList = findViewById(R.id.moodSelect_recycler);
        socialSpinner = (findViewById(R.id.social_spinner));


        // recyclerView
        recyclerViewLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        moodSelectAdapter = new SelectMoodAdapter();
        moodSelectList.setLayoutManager(recyclerViewLayoutManager);
        moodSelectList.setAdapter(moodSelectAdapter);



        initial();


        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (moodSelectAdapter.getSelectedMoodType() != null)
                    moodEvent.setMoodType(moodSelectAdapter.getSelectedMoodType());
                moodEvent.setReason(reasonEditText.getText().toString());
                moodEvent.setSocialSituation(social);
                communicator.updateMoodEvent(moodEvent);
                setResult(RESULT_OK, null);
                finish();
            }
        });
        //Social Situation
        ArrayAdapter<String> socialAdapter = new ArrayAdapter<String>(EditMoodActivity.this,android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.socialSit));
        socialAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        socialSpinner.setAdapter(socialAdapter);
        socialSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(adapterView.getItemAtPosition(i).equals("Select Social Situation")){
                    //do nothing
                }
                else{
                    social = adapterView.getItemAtPosition(i).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    private String getMonth(int month) {
        return new DateFormatSymbols().getMonths()[month];
    }
    private void initial(){
        date = moodEvent.getDate();
        time = moodEvent.getTime();
        timeTextView.setText(MoodEventUtility.getTimeStr(time));
        int year = date.getYear();
        int Day = date.getDay();
        int month = date.getMonth();
        dateTextView.setText(getMonth(month)+" "+Day+", "+year);

    }
}
