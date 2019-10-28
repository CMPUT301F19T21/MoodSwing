package com.example.moodswing;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;


import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;
import java.util.Map;


/**
 * MainActivity
 */
public class MainActivity extends AppCompatActivity {
    // general
    private Button MapButton;
    private static final String TAG = "MainActivity";
    private FirestoreUserDocCommunicator communicator;
    private static final int USER_ID_REQUEST = 1;

    // UI elements
    private RecyclerView moodList;
    private Button addButton;
    private Button delButton;

    // RecyclerView related
    private RecyclerView.LayoutManager recyclerViewLayoutManager;
    private RecyclerView.Adapter moodListAdapter;
    private ArrayList<MoodEvent> moodDataList;
            // note: can add an array here for item deletion.


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainscreen);


        /* link all UI elements here */
        addButton = (Button) findViewById(R.id.addMoodButton);
        delButton = (Button) findViewById(R.id.delMoodButton);
        moodList = findViewById(R.id.mood_list);

            // recyclerView related
        recyclerViewLayoutManager = new LinearLayoutManager(this);
        moodDataList = new ArrayList<>();
        moodListAdapter = new MoodAdapter(moodDataList);

        moodList.setAdapter(moodListAdapter);
        moodList.setLayoutManager(recyclerViewLayoutManager);


        /* ----------------------- IMPORTANT ---------------------*/
        /* life cycle reminder
         * this is the end of onCreate method
         *
         *
         * for action that need to be performed after user loggin should be written in "onPostLogin()" method
         * for action that need to be performed before user loggin should be written in "onCreate()" method BEFORE this msg
         *
         *
         */
        /* login */
        Intent intentLoginActivity = new Intent(this, LoginActivity.class);
        startActivityForResult(intentLoginActivity, USER_ID_REQUEST);
        MapButton = findViewById(R.id.mapViewButton);
        MapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), GoogleMapActivity.class));
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == USER_ID_REQUEST) {
            if (resultCode == RESULT_OK) {
                String username = data.getStringExtra("username");
                onPostLogin(username);
            }
        }
        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {

            }
        }
    }

    private void onPostLogin(String username){
        /* --------------- init communicator (this should be on top)----------- */
        communicator = new FirestoreUserDocCommunicator(username);
        communicator.initMoodEventsList(moodList); // init listView realtimeListener by communicator

        /* --------------------------- OnclickListeners ------------------------ */
        // adding
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(), NewMoodActivity.class);
                startActivityForResult(i, 2);
                // testing
                //communicator.addMoodEvent(new MoodEvent(1, new DateJar(1998,2,27), new TimeJar(12,30)));
                // can call some method here to switch activity.
            }
        });
        // deletion


        /* ---------------------------- Other Actions ----------------------- */




        //

    }
}
