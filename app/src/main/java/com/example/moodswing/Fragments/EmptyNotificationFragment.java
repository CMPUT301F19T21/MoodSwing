package com.example.moodswing.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.moodswing.MainActivity;
import com.example.moodswing.NewMoodActivity;
import com.example.moodswing.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * This class is the following fragment screen, accessed from mainactivity. redirects to following management
 */
public class EmptyNotificationFragment extends Fragment {
    private FloatingActionButton addBtn;

    /**
     * Initializes the UI buttons, the following/follower lists, the redirect to management fragment
     */
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_empty_notification, container, false);
        // the view is created after this

        addBtn = root.findViewById(R.id.emptyNotifcation_addBtn);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), NewMoodActivity.class));
            }
        });

        return root;
    }
}