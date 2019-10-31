package com.example.moodswing.ui.following;

import android.content.Intent;
import android.os.Bundle;
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

    private FollowingViewModel followingViewModel;
    private RecyclerView moodList;
    private Button addButton;
    private Button delButton;
    private RecyclerView.LayoutManager recyclerViewLayoutManager;
    private RecyclerView.Adapter moodListAdapter;
    private ArrayList<MoodEvent> moodDataList;

    private FirestoreUserDocCommunicator communicator;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.mood_history_screen, container, false);
        // the view is created after this

        // get viewModel
        followingViewModel =
                ViewModelProviders.of(this).get(FollowingViewModel.class);


        // first, init view model to retrieve saved data (if any)
        followingViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                // textView.setText(s);
            }
        });



        addButton = root.findViewById(R.id.addMoodButton);
        delButton = root.findViewById(R.id.delMoodButton);
        moodList = root.findViewById(R.id.mood_list);

        // recyclerView testing
        recyclerViewLayoutManager = new LinearLayoutManager(getContext());
        moodDataList = new ArrayList<>();
        moodListAdapter = new MoodAdapter(moodDataList);
        moodList.setAdapter(moodListAdapter);
        moodList.setLayoutManager(recyclerViewLayoutManager);

        communicator.initMoodEventsList(moodList);


        //
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), NewMoodActivity.class);
                startActivity(i);
            }
        });





        return root;
    }
}