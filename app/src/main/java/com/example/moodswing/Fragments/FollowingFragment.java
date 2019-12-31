package com.example.moodswing.Fragments;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moodswing.MainActivity;
import com.example.moodswing.ManagementActivity;
import com.example.moodswing.R;
import com.example.moodswing.customDataTypes.FirestoreUserDocCommunicator;
import com.example.moodswing.customDataTypes.MoodEventUtility;
import com.example.moodswing.customDataTypes.ObservableUserJarArray;
import com.example.moodswing.customDataTypes.UserJar;
import com.example.moodswing.customDataTypes.UserJarAdaptor;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * This class is the following fragment screen, accessed from mainactivity. redirects to following management
 */
public class FollowingFragment extends Fragment implements ObservableUserJarArray.ObservableUserJarArrayClient {
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


    @Override
    public void onDestroy() {
        super.onDestroy();
        communicator.getUserJarArrayObs().removeClient(this);
    }

    /**
     * Initializes the UI buttons, the following/follower lists, the redirect to management fragment
     */

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_following, container, false);
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

        // testing
        if (!(communicator.getUserJarArrayObs().containClient(this))){
            communicator.getUserJarArrayObs().addClient(this);
        }
        this.refreshMoodList();

        // setup button state
        if (communicator.getFollowingFilterList().isEmpty()){
            filterButtonPopped();
        }else{
            filterButtonPressed();
        }

        //setup listeners
        managementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ManagementActivity.class));
            }
        });

        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).openFilterFragment(2);
            }
        });

        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).openMapFragment(MoodEventUtility.FOLLOWING_MODE);
            }
        });

        return root;
    }


    /**
     * Refreshes the moodlist
     */
    public void refreshMoodList(){
        userJars.clear();
        for (UserJar userJar : communicator.getUserJars()){
            if (!(communicator.getFollowingFilterList().contains(userJar.getMoodEvent().getMoodType()))){
                userJars.add(userJar);
            }
        }
        userJarAdaptor.notifyDataSetChanged();
    }

    /**
     * changes the look of the button when pressed
     */
    public void filterButtonPressed(){
        filterButton.setCompatElevation(0f);
        filterButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.color_button_lightGrey_pressed)));
    }
    /**
     * changes the look of the button when deselected
     */
    public void filterButtonPopped(){
        filterButton.setCompatElevation(12f);
        filterButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.color_button_lightGrey)));
    }

    @Override
    public void userJarArrayChanged() {
        refreshMoodList();
    }

}