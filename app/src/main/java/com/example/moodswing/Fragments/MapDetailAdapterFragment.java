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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MapDetailAdapterFragment extends Fragment {
    private FrameLayout MapDetailAdapterLayout;
    private int mode;

    public MapDetailAdapterFragment(){}

    public MapDetailAdapterFragment(int mode){
        this.mode = mode;
    }

    /**
     * Initializes the UI buttons, the following/follower lists, the redirect to management fragment
     */
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_detailed_map, container, false);
        // the view is created after this

        MapDetailAdapterLayout = root.findViewById(R.id.MapDetailedFrag_placeholder);

        switch (mode){
            case 1:
                getFragmentManager().beginTransaction()
                        .replace(MapDetailAdapterLayout.getId(), new MoodDetailFragment(1), "innerDetailedView")
                        .commitAllowingStateLoss();
                break;
            case 2:
                //
                break;
        }

        return root;
    }
}
