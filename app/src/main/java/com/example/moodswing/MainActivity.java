package com.example.moodswing;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * MainActivity
 */
public class MainActivity extends AppCompatActivity {
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseFirestore.getInstance();
        Intent intentLoginActivity = new Intent(this, LoginActivity.class);

        startActivity(intentLoginActivity);

    }


}
