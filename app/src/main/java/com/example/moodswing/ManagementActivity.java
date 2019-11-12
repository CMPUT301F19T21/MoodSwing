package com.example.moodswing;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moodswing.Fragments.sendingRequestFragment;
import com.example.moodswing.customDataTypes.FirestoreUserDocCommunicator;
import com.example.moodswing.customDataTypes.SimpleUserJarAdapter;
import com.example.moodswing.customDataTypes.UserJar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

/**
 * The follower management screen. Shows followers and following, and can send new requests.
 */
public class ManagementActivity extends AppCompatActivity {
    // communicator
    private FirestoreUserDocCommunicator communicator;

    // UI elements
    private FloatingActionButton sendRequestButton;
    private FloatingActionButton backBtn;

    // request recyclerView related
    private RecyclerView.LayoutManager recyclerViewLayoutManager_request;
    private SimpleUserJarAdapter requestAdapter;
    private ArrayList<UserJar> request_userJars;
    private RecyclerView requestList;

    // following recyclerView related
    private RecyclerView.LayoutManager recyclerViewLayoutManager_following;
    private SimpleUserJarAdapter followingAdapter;
    private ArrayList<UserJar> following_userJars;
    private RecyclerView followingList;

    /**
     *initializes the arraylists for followers and following and their respective adapters as well as firestore listener
     * and the send new following request button
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_management);


        communicator = FirestoreUserDocCommunicator.getInstance();

        sendRequestButton = findViewById(R.id.request_button);
        backBtn = findViewById(R.id.request_backBtn);
        requestList = findViewById(R.id.management_request);
        followingList = findViewById(R.id.managment_following);


        recyclerViewLayoutManager_request = new LinearLayoutManager(this);
        recyclerViewLayoutManager_following = new LinearLayoutManager(this);


        request_userJars = new ArrayList<>();
        following_userJars = new ArrayList<>();

        requestAdapter = new SimpleUserJarAdapter(request_userJars, 1);
        followingAdapter = new SimpleUserJarAdapter(following_userJars, 2);

        requestList.setLayoutManager(recyclerViewLayoutManager_request);
        followingList.setLayoutManager(recyclerViewLayoutManager_following);

        requestList.setAdapter(requestAdapter);
        followingList.setAdapter(followingAdapter);


        // setup realtime listener
        communicator.initManagementRequestList(requestList);
        communicator.initManagementFollowingList(followingList);

        //setup listeners

        sendRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new sendingRequestFragment().show(getSupportFragmentManager(), "request");
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}