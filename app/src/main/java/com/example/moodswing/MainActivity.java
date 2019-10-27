package com.example.moodswing;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
    private static final String TAG = "MainActivity";
    private FirestoreUserDocCommunicator communicator;
    private static final int USER_ID_REQUEST = 1;
    DocumentReference userRef;

    // listview

    private ListView moodList;
    private Button addButton;
    private Button delButton;

    private ArrayAdapter<MoodEvent> moodListAdapter;
    private ArrayList<MoodEvent> moodDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainscreen);


        /* link all UI elements here */
        addButton = (Button) findViewById(R.id.addMoodButton);
        delButton = (Button) findViewById(R.id.delMoodButton);
        moodList = findViewById(R.id.mood_list);


        moodDataList = new ArrayList<>();
        moodListAdapter = new MoodAdapter(this, moodDataList);
        moodList.setAdapter(moodListAdapter);


        /* login */
        Intent intentLoginActivity = new Intent(this, LoginActivity.class);
        startActivityForResult(intentLoginActivity, USER_ID_REQUEST);
        /* life cycle reminder
         * the end of onCreate, all the object that should be init before user login should be
         * written in onPostLogin
         */


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
    }

    private void onPostLogin(String username){
        // init communicator
        communicator = new FirestoreUserDocCommunicator(username);
        communicator.showListView(moodList);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // testing
                communicator.addMoodEvent(new MoodEvent(1, new DateJar(1998,2,27), new TimeJar(12,30)));
            }
        });

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference events = db.collection("Accounts").document(username).collection("MoodEvents");
        /*
        events.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                moodList.clearMoodEvents();
                for (QueryDocumentSnapshot eventsDoc : queryDocumentSnapshots) {
                    Map<String, Object> data = eventsDoc.getData();
                    Map<String, Integer> dateMap = (Map<String, Integer>) data.get("date");
                    Map<String, Integer> timeMap = (Map<String, Integer>) data.get("time");

                    DateJar dateJar = new DateJar(dateMap.get("year"),dateMap.get("month"),dateMap.get("day"));
                    TimeJar timeJar = new TimeJar (timeMap.get("hr"),timeMap.get("min"));
                    MoodEvent moodEvent = new MoodEvent((Integer) data.get("moodType"),dateJar,timeJar);
                    ((MoodAdapter)listView.getAdapter()).addToMoods(moodEvent);
                }

            }
        });

         */
        /* ------------------------------------------------ */
        // other actions after login:





        //

    }
}
