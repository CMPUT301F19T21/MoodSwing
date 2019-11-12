package com.example.moodswing;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.moodswing.customDataTypes.FirestoreUserDocCommunicator;

//The filter for the moodlist, to be completed
// this class is not in use

public class FilterFragment extends DialogFragment {

    private FirestoreUserDocCommunicator communicator;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.filter_fragment, container, false);
        // the view is created after this

        communicator = FirestoreUserDocCommunicator.getInstance();

        return root;
    }
}