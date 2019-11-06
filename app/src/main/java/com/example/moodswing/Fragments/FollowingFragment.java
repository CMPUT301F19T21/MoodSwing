package com.example.moodswing.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moodswing.MainActivity;
import com.example.moodswing.R;
import com.example.moodswing.customDataTypes.FirestoreUserDocCommunicator;
import com.example.moodswing.customDataTypes.MoodAdapter;
import com.example.moodswing.customDataTypes.MoodEvent;
import com.example.moodswing.customDataTypes.UserJar;
import com.example.moodswing.customDataTypes.UserJarAdaptor;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class FollowingFragment extends Fragment {
    // communicator
    private FirestoreUserDocCommunicator communicator;

    // UI elements
    private FloatingActionButton mapButton;
    private FloatingActionButton managementButton;
    private FloatingActionButton filterButton;

    // recyclerView related
    private RecyclerView.LayoutManager recyclerViewLayoutManager;
    private UserJarAdaptor userJarAdaptor;
    private ArrayList<UserJar> userJars;
    private RecyclerView userJarList;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_following, container, false);
        // the view is created after this

        communicator = FirestoreUserDocCommunicator.getInstance();
        mapButton = root.findViewById(R.id.following_map);
        managementButton = root.findViewById(R.id.following_management);
        filterButton = root.findViewById(R.id.following_filterBtn);
        userJarList = root.findViewById(R.id.following_list);

        recyclerViewLayoutManager = new LinearLayoutManager(getContext());
        userJars = new ArrayList<>();
        userJarAdaptor = new UserJarAdaptor(userJars);
        userJarList.setLayoutManager(recyclerViewLayoutManager);
        userJarList.setAdapter(userJarAdaptor);

        // setup realtime listener
        communicator.initFollowingList(userJarList);

        //setup listeners

        managementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).toManagement();
            }
        });


        return root;
    }
}