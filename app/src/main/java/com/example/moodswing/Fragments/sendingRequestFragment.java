package com.example.moodswing.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.moodswing.MainActivity;
import com.example.moodswing.R;
import com.example.moodswing.customDataTypes.FirestoreUserDocCommunicator;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


/**
 * The screen that is redirected to when the user presses the button in the following/follower management screen.
 * It will prompt the user for a username, and a request will be sent to that user to become their follower
 */
public class sendingRequestFragment extends DialogFragment {
    private FirestoreUserDocCommunicator communicator;
    private EditText usernameEditText;

    private FloatingActionButton confirmBtn;
    private FloatingActionButton backBtn;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        communicator = FirestoreUserDocCommunicator.getInstance();
    }

    /**
     * Creates the confirm and back button and the EditText view to enter the username, sends the username typed
     * to the FirestoreUserDocCommunicator for requesting to follow
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_send_request, container, false);
        getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        // link elements
        usernameEditText = view.findViewById(R.id.requestFrag_requestEnter);
        confirmBtn = view.findViewById(R.id.requestFrag_confirm);
        backBtn = view.findViewById(R.id.requestFrag_back);

        // listeners
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString();
                communicator.sendFollowingRequest(username);
                dismiss();
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        // other
        return view;
    }
}