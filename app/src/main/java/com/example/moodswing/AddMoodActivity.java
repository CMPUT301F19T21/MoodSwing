package com.example.moodswing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class AddMoodActivity extends AppCompatActivity {

    EditText dateText;
    EditText timeText;
    EditText reasonEditText;
    ImageButton locationButton;
    TextView loctionText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_mood);

        Intent intent = getIntent();


    }
}