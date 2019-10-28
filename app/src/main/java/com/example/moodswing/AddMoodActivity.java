package com.example.moodswing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class AddMoodActivity extends AppCompatActivity {

    private EditText dateText;
    private EditText timeText;
    private EditText reasonEditText;
    private ImageButton locationButton;
    private TextView loctionText;
    private static boolean alreadyLoggedIn = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_mood);

        Intent intent = getIntent();


    }
}
