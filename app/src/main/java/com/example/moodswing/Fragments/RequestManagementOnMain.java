package com.example.moodswing.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.moodswing.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * This class is the following fragment screen, accessed from mainactivity. redirects to following management
 */
public class RequestManagementOnMain extends Fragment {
    private FloatingActionButton backBtn;

    /**
     * Initializes the UI buttons, the following/follower lists, the redirect to management fragment
     */
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_request_management_on_main, container, false);
        // the view is created after this

        backBtn = root.findViewById(R.id.fragmentrequestmanagementonmain_backBtn);

        return root;
    }
}