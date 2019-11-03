package com.example.moodswing.navigationFragments;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moodswing.GoogleMapActivity;
import com.example.moodswing.MainActivity;
import com.example.moodswing.MoodDetailActivity;
import com.example.moodswing.NewMoodActivity;
import com.example.moodswing.R;
import com.example.moodswing.customDataTypes.DateJar;
import com.example.moodswing.customDataTypes.FirestoreUserDocCommunicator;
import com.example.moodswing.customDataTypes.MoodAdapter;
import com.example.moodswing.customDataTypes.MoodEvent;
import com.example.moodswing.customDataTypes.TimeJar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
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

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // inflate fragment layout
        View root = inflater.inflate(R.layout.mood_history_screen, container, false);
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
        communicator.initMoodEventsList(moodList);

        // setup listeners
        MapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), GoogleMapActivity.class));
            }
        });
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), NewMoodActivity.class));


                communicator.addMoodEvent(new MoodEvent(communicator.generateMoodID(),4,new DateJar(1998,3,27),
                        new TimeJar(11,11)));
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

        // Able to click to mood detail
        moodListAdapter.setOnItemClickListener(new MoodAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getActivity(), MoodDetailActivity.class);
                intent.putExtra("MoodUID",moodDataList.get(position).getUniqueID());
                startActivity(intent);
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

}