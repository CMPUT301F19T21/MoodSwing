package com.example.moodswing.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.moodswing.R;
import com.example.moodswing.customDataTypes.FirestoreUserDocCommunicator;
import com.example.moodswing.customDataTypes.UserJar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Locale;

/**
 * The screen to unfollow a user.
 */
public class UnfollowDialogFragment extends DialogFragment {
    private FirestoreUserDocCommunicator communicator;
    private UserJar userJar;


    private FloatingActionButton confirmBtn;
    private FloatingActionButton backBtn;
    private TextView displayText;


    public UnfollowDialogFragment(){}

    public UnfollowDialogFragment(UserJar userJar){
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
        View view = inflater.inflate(R.layout.fragment_unfollow_dialog, container, false);
        getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        // link elements
        confirmBtn = view.findViewById(R.id.unfollowFrag_confirm);
        backBtn = view.findViewById(R.id.unfollowFrag_back);
        displayText = view.findViewById(R.id.unfollowFrag_textView);


        // text
        String forDisplay = String.format(Locale.getDefault(), "Unfollow %s?", userJar.getUsername().toString());
        displayText.setText(forDisplay);

        // listeners
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                communicator.unfollow(userJar);
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