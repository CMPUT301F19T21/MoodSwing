package com.example.moodswing.ui.following;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moodswing.DateJar;
import com.example.moodswing.FirestoreUserDocCommunicator;
import com.example.moodswing.MainActivity;
import com.example.moodswing.MoodAdapter;
import com.example.moodswing.MoodEvent;
import com.example.moodswing.NewMoodActivity;
import com.example.moodswing.R;
import com.example.moodswing.TimeJar;

import java.util.ArrayList;

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