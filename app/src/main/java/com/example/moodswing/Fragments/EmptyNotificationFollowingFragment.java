package com.example.moodswing.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.moodswing.ManagementActivity;
import com.example.moodswing.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * This class is the following fragment screen, accessed from mainactivity. redirects to following management
 */
public class EmptyNotificationFollowingFragment extends Fragment {
    private FloatingActionButton proBtn;
    private TextView promptText;

    /**
     * Initializes the UI buttons, the following/follower lists, the redirect to management fragment
     */
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_empty_notification, container, false);
        // the view is created after this

        proBtn = root.findViewById(R.id.emptyNotifcation_Btn);
        promptText = root.findViewById(R.id.emptyNotification_text);

        proBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ManagementActivity.class));
            }
        });

        proBtn.setImageDrawable(getResources().getDrawable(R.drawable.ic_person_add_black_24dp));

        promptText.setText("NO ONE IS SHARING THEIR MOOD CLICK TO FOLLOW PEOPLE");

        return root;
    }
}