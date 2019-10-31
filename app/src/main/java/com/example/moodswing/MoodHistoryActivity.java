package com.example.moodswing;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;


/**
 * MainActivity
 */
public class MoodHistoryActivity extends AppCompatActivity {
    // communicator
    private FirestoreUserDocCommunicator communicator;

    // general
    private Button MapButton;
    private static final String TAG = "MainActivity";
    private static final int USER_ID_REQUEST = 1;


    // UI elements
    private RecyclerView moodList;
    private Button addButton;
    private Button delButton;

    // delete latter, here for convince
    private Button logoutBtn;

    // RecyclerView related
    private RecyclerView.LayoutManager recyclerViewLayoutManager;
    private RecyclerView.Adapter moodListAdapter;
    private ArrayList<MoodEvent> moodDataList;
            // note: can add an array here for item deletion.


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mood_history_screen);
        communicator = FirestoreUserDocCommunicator.getInstance();


        /* link all UI elements here */
        addButton = (Button) findViewById(R.id.addMoodButton);
        delButton = (Button) findViewById(R.id.delMoodButton);
        moodList = findViewById(R.id.mood_list);
        MapButton = findViewById(R.id.mapViewButton);

        //
        logoutBtn = findViewById(R.id.buttonTempLogout);

    // recyclerView related
        recyclerViewLayoutManager = new LinearLayoutManager(this);
        moodDataList = new ArrayList<>();
        moodListAdapter = new MoodAdapter(moodDataList);

        moodList.setAdapter(moodListAdapter);
        moodList.setLayoutManager(recyclerViewLayoutManager);
        // set realtime listener
        communicator.initMoodEventsList(moodList);
        // listeners
        MapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), GoogleMapActivity.class));
            }
        });
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), NewMoodActivity.class);
                startActivityForResult(i, 2);

                //communicator.addMoodEvent(new MoodEvent(1, new DateJar(1998,2,27), new TimeJar(12,30)));
            }
        });
        // deletion
        delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i = 0; i < moodListAdapter.getItemCount(); i++){
                    if (moodDataList.get(i).isSelected()) {
                        communicator.removeMoodEvent(moodDataList.get(i).getUniqueID());
                    }
                }
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirestoreUserDocCommunicator.destroy();
                finishAffinity();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });

        BottomNavigationView navigation = findViewById(R.id.nav_view);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_home:
                        break;
                    case R.id.navigation_following:
                        Intent intent = new Intent(getApplicationContext(),FollowingActivity.class);
                        startActivity(intent);
                        break;
                }
                return false;
            }
        });
    }
}
