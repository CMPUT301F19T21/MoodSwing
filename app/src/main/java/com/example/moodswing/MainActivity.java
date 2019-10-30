package com.example.moodswing;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private static final int LOGIN_ACTIVITY_REQUEST_CODE = 1;
    private static final int REG_ACTIVITY_REQUEST_CODE = 2;


    // RecyclerView related
    private RecyclerView.LayoutManager recyclerViewLayoutManager;
    private MoodAdapter moodListAdapter;
    private ArrayList<MoodEvent> moodDataList;
            // note: can add an array here for item deletion.



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_screen);


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
        toLogin();
}

    private void toLogin(){
        Intent intent = new Intent(this, LoginActivity.class);
        finishAffinity();
        startActivityForResult(intent, LOGIN_ACTIVITY_REQUEST_CODE);
    }

    private void toReg(){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivityForResult(intent, REG_ACTIVITY_REQUEST_CODE);
    }

    private void toMood(){
        Intent intent = new Intent (this, MoodHistoryActivity.class);
        finishAffinity();
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOGIN_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                switch(data.getIntExtra("return_mode",0)){
                    case 1:
                        toReg();
                        break;
                    case 2:
                        toMood();
                        break;
                    case 0:
                        // should never happen
                }
            }
        }
        if (requestCode == REG_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                switch(data.getIntExtra("return_mode",0)){
                    case 1:
                        toLogin();
                        break;
                    case 0:
                        // should never happen
                }
            }
        }
        if (requestCode == 9) {
            if (resultCode == RESULT_OK) {
                String UID = data.getStringExtra("UID");
                if (UID != null)
                    communicator.removeMoodEvent(UID);
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


        /* ---------------------------- Other Actions ----------------------- */


        moodListAdapter.setOnItemClickListener(new MoodAdapter.OnItemClickListener() {
            @Override
            public void OnItemiClick(int position) {
                MoodEvent moodEvent = moodDataList.get(position);
                Intent intent = new Intent(MainActivity.this,MoodDetailActivity.class);
                intent.putExtra("MoodEvent",moodEvent);
                startActivityForResult(intent,9);
            }
        });
        //

    }
}
