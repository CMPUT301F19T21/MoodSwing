package com.example.moodswing.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.moodswing.NewMoodActivity;
import com.example.moodswing.R;
import com.example.moodswing.customDataTypes.FirestoreUserDocCommunicator;
import com.example.moodswing.customDataTypes.MoodEvent;
import com.example.moodswing.customDataTypes.UserJar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MapDetailAdapterFragment extends Fragment {
    private static final int MOODHISTORY_MODE = 1;
    private static final int FOLLOWING_MODE = 2;

    private FrameLayout MapDetailAdapterLayout;
    private int mode;
    private String ID;
    private FirestoreUserDocCommunicator communicator;

    public MapDetailAdapterFragment(){}

    public MapDetailAdapterFragment(int mode, String ID){
        this.mode = mode;
        this.ID = ID;
        this.communicator = FirestoreUserDocCommunicator.getInstance();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_detailed_map, container, false);
        // the view is created after this

        MapDetailAdapterLayout = root.findViewById(R.id.MapDetailedFrag_placeholder);

        switch (mode){
            case MOODHISTORY_MODE:
                getFragmentManager().beginTransaction()
                        .replace(MapDetailAdapterLayout.getId(), new MoodDetailFragment(communicator.getMoodPosition(ID),this))
                        .commitAllowingStateLoss();
                break;
            case FOLLOWING_MODE:
                //
                break;
        }

        return root;
    }

    private int getMoodPosition(String ID) {
        if(mode == 1) {
            ArrayList<MoodEvent> moodEvents = communicator.getMoodEvents();
            int position = 0;
            for (MoodEvent moodEvent : moodEvents) {
                if(moodEvent.getUniqueID() == ID) {
                    return position;
                }
                position ++;
            }
        }
        return -1;
    }
}
