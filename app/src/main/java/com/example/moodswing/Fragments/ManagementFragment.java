package com.example.moodswing.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moodswing.R;
import com.example.moodswing.customDataTypes.FirestoreUserDocCommunicator;
import com.example.moodswing.customDataTypes.MoodAdapter;
import com.example.moodswing.customDataTypes.MoodEvent;
import com.example.moodswing.customDataTypes.SimpleUserJarAdapter;
import com.example.moodswing.customDataTypes.UserJar;
import com.example.moodswing.customDataTypes.UserJarAdaptor;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ManagementFragment extends Fragment {
    // communicator
    private FirestoreUserDocCommunicator communicator;

    // UI elements
    private FloatingActionButton sendRequestButton;

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

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_management, container, false);
        // the view is created after this



        communicator = FirestoreUserDocCommunicator.getInstance();

        sendRequestButton = root.findViewById(R.id.request_button);
        requestList = root.findViewById(R.id.management_request);
        followingList = root.findViewById(R.id.managment_following);


        recyclerViewLayoutManager_request = new LinearLayoutManager(getContext());
        recyclerViewLayoutManager_following = new LinearLayoutManager(getContext());


        request_userJars = new ArrayList<>();
        following_userJars = new ArrayList<>();

        requestAdapter = new SimpleUserJarAdapter(request_userJars, 1);
        followingAdapter = new SimpleUserJarAdapter(following_userJars, 2);

        requestList.setLayoutManager(recyclerViewLayoutManager_request);
        followingList.setLayoutManager(recyclerViewLayoutManager_following);

        requestList.setAdapter(requestAdapter);
        followingList.setAdapter(followingAdapter);


        // setup realtime listener
//        communicator.initManagementRequestList(requestList);
//        communicator.initManagementFollowingList(followingList);

        //setup listeners

        sendRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new sendingRequestFragment().show(getActivity().getSupportFragmentManager(), "request");
            }
        });


        return root;
    }
}