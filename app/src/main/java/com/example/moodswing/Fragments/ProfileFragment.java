package com.example.moodswing.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.moodswing.MainActivity;
import com.example.moodswing.R;
import com.example.moodswing.customDataTypes.FirestoreUserDocCommunicator;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


/**
 * The user profile. It will show the username of the user, have the logout button, and may eventually have functionality
 * related to the user such as change username, adding a profile picture
 */
public class ProfileFragment extends DialogFragment {
    private FirestoreUserDocCommunicator communicator;
    private TextView username;
    private FloatingActionButton logoutBtn;
    private FloatingActionButton backBtn;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        communicator = FirestoreUserDocCommunicator.getInstance();


    }

    /**
     * Profile buttons created and redirects to appropriate activities(logout and back). Shows the user's username as a TextView
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        // link elements
        username = view.findViewById(R.id.profile_username);
        logoutBtn = view.findViewById(R.id.profile_LogOut);
        backBtn = view.findViewById(R.id.profile_back);

        // listeners
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).signOut();
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        // other
        username.setText(communicator.getUsername());

        return view;
    }
}
