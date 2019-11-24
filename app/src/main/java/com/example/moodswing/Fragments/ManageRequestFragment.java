package com.example.moodswing.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.moodswing.R;
import com.example.moodswing.customDataTypes.FirestoreUserDocCommunicator;
import com.example.moodswing.customDataTypes.UserJar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * The screen to show pending requests for being followed.
 */
public class ManageRequestFragment extends DialogFragment {
    private FirestoreUserDocCommunicator communicator;
    private UserJar userJar;


    private FloatingActionButton confirmBtn;
    private FloatingActionButton rejectBtn;
    private FloatingActionButton backBtn;


    public ManageRequestFragment(){}

    public ManageRequestFragment(UserJar userJar){
        this.userJar = userJar;
    }

    /**
     * Just the communicator being instantiated
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        communicator = FirestoreUserDocCommunicator.getInstance();
    }

    /**
     * creates the confirm following request, reject request, and back button, and handles
     * redirects to the FirestoreUserDocCommunicator which handles the update of the request
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_check_requests, container, false);
        getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        // link elements
        rejectBtn = view.findViewById(R.id.checkrequest_reject);
        confirmBtn = view.findViewById(R.id.checkrequest_confirm);
        backBtn = view.findViewById(R.id.checkrequest_back);

        // listeners
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                communicator.acceptRequest(userJar);
                dismiss();
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        rejectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                communicator.removeRequest(userJar);
                dismiss();
            }
        });

        // other
        return view;
    }
}