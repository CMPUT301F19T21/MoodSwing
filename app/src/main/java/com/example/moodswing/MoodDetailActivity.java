package com.example.moodswing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class MoodDetailActivity extends AppCompatActivity {
    MoodEvent moodEvent;
    private int moodType;
    private DateJar date;
    private TimeJar time;
    // optional fields
    private String reason;
    private Integer socialSituation;

    private TextView dateText;
    private TextView timeText;
    private TextView moodText;
    private TextView descriptionText;

    private ImageButton delButton;
    private ImageButton editButton;

    private static boolean alreadyLoggedIn = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mood_details);
        Intent moodintent = getIntent();

        moodType = moodEvent.getMoodType();
        date = moodEvent.getDate();
        time = moodEvent.getTime();
        reason = moodEvent.getReason();
        socialSituation = moodEvent.getSocialSituation();

        dateText = findViewById(R.id.dateText);
        timeText = findViewById(R.id.timeText);
        moodText = findViewById(R.id.moodText);
        descriptionText = findViewById(R.id.descriptionText);
        delButton = findViewById(R.id.deleteButton);
        editButton = findViewById(R.id.editButton);


        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MoodDetailActivity.this, AddMoodActivity.class);

                startActivity(intent);
            }
        });
    }
}
