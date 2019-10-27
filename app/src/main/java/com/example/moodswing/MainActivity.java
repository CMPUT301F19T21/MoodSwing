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


import com.google.firebase.firestore.DocumentReference;

import com.google.firebase.firestore.FirebaseFirestore;


import java.util.ArrayList;


/**
 * MainActivity
 */
public class MainActivity extends AppCompatActivity {
    private FirestoreUserDocCommunicator communicator;
    private static final int USER_ID_REQUEST = 1;
    DocumentReference userRef;

    private ListView moodList;
    private ArrayAdapter<MoodEvent> moodListAdapter;
    private ArrayList<MoodEvent> moodDataList;
    private ArrayList<MoodEvent> selectedItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainscreen);


        /* link all UI elements here */

        /* login */
        Intent intentLoginActivity = new Intent(this, LoginActivity.class);
        startActivityForResult(intentLoginActivity, USER_ID_REQUEST);
        /* life cycle reminder
         * the end of onCreate, all the object that should be init before user login should be
         * written in onPostLogin
         */

        //startActivity(intentLoginActivity);

        moodList = findViewById(R.id.mood_list);
        moodList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        moodDataList = new ArrayList<>();
        DateJar d = new DateJar(1997, 02, 24);
        TimeJar t = new TimeJar(12, 30);
        MoodEvent m = new MoodEvent(33, d, t);

        moodDataList.add(m);
        moodListAdapter = new CustomAdapter(this, moodDataList);
        moodList.setAdapter(moodListAdapter);


        moodList.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        selectedItems = new ArrayList<>();

        moodList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CheckBox checkBox = findViewById(R.id.checkBox);
                MoodEvent selection = moodDataList.get(i);
                if (selectedItems.contains(selection)) {
                    selectedItems.remove(selection);
                    checkBox.setChecked(false);
                }
                else {
                    selectedItems.add(selection);
                    checkBox.setChecked(true);
                }
            }
        });



        Button addButton = (Button) findViewById(R.id.addMoodButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Only test code
                moodDataList.add(new MoodEvent(35, new DateJar(2018, 03, 06), new TimeJar(14, 22)));
                moodListAdapter.notifyDataSetChanged();

            }
        });

        Button delButton = (Button) findViewById(R.id.delMoodButton);
        delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Only test code
                if (!selectedItems.isEmpty()) {
                    for (int i = 0; i < selectedItems.size(); i++) {
                        moodDataList.remove(selectedItems.get(i));
                    }
                    moodListAdapter.notifyDataSetChanged();
                }
            }
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == USER_ID_REQUEST) {
            if (resultCode == RESULT_OK) {
                String username = data.getStringExtra("username");
                //onPostLogin(username);
            }
        }
    }





    private void onPostLogin(String username){
        // init communicator
        communicator = new FirestoreUserDocCommunicator(username);
        // other actions after login:


        // testing
        communicator.addMoodEvent(new MoodEvent(1,new DateJar(1998,2,27),new TimeJar(12,20)));
    }
}
