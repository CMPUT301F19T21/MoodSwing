package com.example.moodswing.navigationFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.moodswing.R;
import com.example.moodswing.customDataTypes.FirestoreUserDocCommunicator;

public class FollowingFragment extends Fragment {

    private FirestoreUserDocCommunicator communicator;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_following, container, false);
        // the view is created after this

        communicator = FirestoreUserDocCommunicator.getInstance();


        return root;
    }
}