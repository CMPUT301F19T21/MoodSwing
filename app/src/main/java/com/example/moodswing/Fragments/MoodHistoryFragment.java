package com.example.moodswing.Fragments;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moodswing.GoogleMapActivity;
import com.example.moodswing.MainActivity;
import com.example.moodswing.NewMoodActivity;
import com.example.moodswing.R;
import com.example.moodswing.customDataTypes.FirestoreUserDocCommunicator;
import com.example.moodswing.customDataTypes.MoodAdapter;
import com.example.moodswing.customDataTypes.MoodEvent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

/**
 * The first screen after logging in, part of mainActivity. User will see their own moodlist here
 */
public class MoodHistoryFragment extends Fragment {
    // communicator
    private FirestoreUserDocCommunicator communicator;

    // UI elements
    private FloatingActionButton MapButton;
    private FloatingActionButton addButton;
    private FloatingActionButton delButton;
    private FloatingActionButton filterButton;

    // recyclerView related
    private RecyclerView.LayoutManager recyclerViewLayoutManager;
    private MoodAdapter moodListAdapter;
    private ArrayList<MoodEvent> moodDataList;
    private RecyclerView moodList;

    // other
    private boolean deleteEnabled;
    /**
     * initializes the UI buttons and navigation on the Home screen(the one right after logging in),
     * the mood history and adapters,and swipe-delete functionality
     */
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // inflate fragment layout
        View root = inflater.inflate(R.layout.fragment_mood_history, container, false);
        // note: write your code after this msg

        // init
        communicator = FirestoreUserDocCommunicator.getInstance();
        addButton = root.findViewById(R.id.home_add);
        delButton = root.findViewById(R.id.home_delete);
        MapButton = root.findViewById(R.id.home_map);
        filterButton = root.findViewById(R.id.home_filterBtn);
        moodList = root.findViewById(R.id.mood_list);
        deleteEnabled = false;

        // construct recyclerView
        recyclerViewLayoutManager = new LinearLayoutManager(getContext());
        moodDataList = new ArrayList<>();
        moodListAdapter = new MoodAdapter(moodDataList);
        moodList.setLayoutManager(recyclerViewLayoutManager);
        moodList.setAdapter(moodListAdapter);

        // setup realTime listener
        communicator.initMoodEventsList(moodList, communicator.getMoodHistoryFilterList());

        // setup button state
        if (communicator.getMoodHistoryFilterList().isEmpty()){
            filterButtonPopped();
        }else{
            filterButtonPressed();
        }

        // setup listeners
        MapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).openMapFragment();
            }
        });
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), NewMoodActivity.class));
            }
        });
        delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deleteEnabled){
                    // turn off
                    deleteEnabled = false;
                    delButton.setCompatElevation(12f);
                    delButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.color_button_lightGrey)));
                    delButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_delete_black_24dp));
                }else{
                    deleteEnabled = true;
                    delButton.setCompatElevation(0f);
                    delButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.color_button_lightGrey_pressed)));
                    delButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_delete_sweep_black_24dp));
                }
            }
        });

        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).openFilterFragment(1);
            }
        });

        ItemTouchHelper.SimpleCallback itemTouchHelper = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                MoodEvent moodEvent = moodDataList.get(viewHolder.getAdapterPosition());
                communicator.removeMoodEvent(moodEvent);
            }

            @Override
            public boolean isItemViewSwipeEnabled() {
                return deleteEnabled;
            }
        };
        new ItemTouchHelper(itemTouchHelper).attachToRecyclerView(moodList);

        return root;
    }

    public void refreshMoodList(){
        communicator.initMoodEventsList(moodList, communicator.getMoodHistoryFilterList());
    }

    public void filterButtonPressed(){
        filterButton.setCompatElevation(0f);
        filterButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.color_button_lightGrey_pressed)));

    }

    public void filterButtonPopped(){
        filterButton.setCompatElevation(12f);
        filterButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.color_button_lightGrey)));
    }
}