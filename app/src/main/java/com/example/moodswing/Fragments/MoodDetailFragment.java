package com.example.moodswing.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.moodswing.EditMoodActivity;
import com.example.moodswing.R;
import com.example.moodswing.customDataTypes.DateJar;
import com.example.moodswing.customDataTypes.FirestoreUserDocCommunicator;
import com.example.moodswing.customDataTypes.MoodEvent;
import com.example.moodswing.customDataTypes.TimeJar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormatSymbols;
import java.util.Locale;

public class MoodDetailFragment extends Fragment{
    private FirestoreUserDocCommunicator communicator;
    private int moodType;
    private DateJar date;
    private TimeJar time;

    private String reason;
    private Integer socialSituation;
    MoodEvent moodEvent;

    TextView dateText;
    TextView timeText;
    TextView moodText;
    TextView descriptionText;
    ImageView moodImage;


    private ImageButton delButton;
    private ImageButton editButton;
    private FloatingActionButton confirmButton;

    int position;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.mood_details, container, false);
        communicator = FirestoreUserDocCommunicator.getInstance();



        Intent moodIntent = getIntent();
        position = moodIntent.getIntExtra("position",-1);

        // find view
        dateText = findViewById(R.id.dateText);
        timeText = findViewById(R.id.timeText);
        moodText = findViewById(R.id.moodText);
        descriptionText = findViewById(R.id.detailedView_reasonText);
        delButton = findViewById(R.id.deleteButton);
        editButton = findViewById(R.id.editButton);
        moodImage = findViewById(R.id.moodImg);
        confirmButton = findViewById(R.id.confirm_Button);

        initial();

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MoodDetailFragment.this, EditMoodActivity.class);
                intent.putExtra("position",position);
                startActivity(intent);
                finish();
            }
        });

        delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                communicator.removeMoodEvent(moodEvent);
                finish();
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    private String getMonth(int month) {
        return new DateFormatSymbols().getMonths()[month];
    }
    private void setMoodImage(int moodType){
        switch(moodType){
            case 1:
                moodText.setText("HAPPY");
                moodImage.setImageResource(R.drawable.mood1);
                break;
            case 2:
                moodText.setText("SAD");
                moodImage.setImageResource(R.drawable.mood2);
                break;
            case 3:
                moodText.setText("ANGRY");
                moodImage.setImageResource(R.drawable.mood3);
                break;
            case 4:
                moodText.setText("EDMOTIONAL");
                moodImage.setImageResource(R.drawable.mood4);
                break;
        }
    }
    private void initial(){
        moodEvent = communicator.getMoodEvents().get(position);

        moodType = moodEvent.getMoodType();
        date = moodEvent.getDate();
        time = moodEvent.getTime();
        reason = moodEvent.getReason();

        String period;
        int Hr = time.getHr();
        int Min = time.getMin();

        if(Hr >12){
            Hr = Hr-12;
            period = "PM";
        }
        else period = "AM";
        timeText.setText(String.format(Locale.getDefault(), "%d:%02d %s",Hr,Min,period));
        int year = date.getYear();
        int Day = date.getDay();
        int month = date.getMonth();
        dateText.setText(getMonth(month)+" "+Day+" "+year);
        //moodText.setText(moodType);
        //socialText.setText(socialSituation);
        descriptionText.setText(reason);
        setMoodImage(moodType);
    }
}
