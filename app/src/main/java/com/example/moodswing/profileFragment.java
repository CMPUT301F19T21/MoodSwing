package com.example.moodswing;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.moodswing.customDataTypes.FirestoreUserDocCommunicator;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class profileFragment extends DialogFragment {
    private FirestoreUserDocCommunicator communicator;
    private TextView username;
    private FloatingActionButton logoutBtn;
    private FloatingActionButton backBtn;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        communicator = FirestoreUserDocCommunicator.getInstance();


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
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
