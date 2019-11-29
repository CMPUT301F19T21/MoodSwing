package com.example.moodswing.Fragments;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moodswing.MainActivity;
import com.example.moodswing.R;
import com.example.moodswing.customDataTypes.FirestoreUserDocCommunicator;
import com.example.moodswing.customDataTypes.SelectMoodAdapter;
import com.example.moodswing.customDataTypes.SelectMoodFilterAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class FilterFragment extends Fragment {
    private FloatingActionButton backBtn;
    private FloatingActionButton resetBtn;

    // filter recyclerView
    private RecyclerView moodSelectList;
    private RecyclerView.LayoutManager recyclerViewLayoutManager;
    private SelectMoodAdapter moodSelectAdapter;

    // filter moodType List
    private ArrayList<Integer> filterList;
    private FirestoreUserDocCommunicator communicator;
    private Integer mode;

    // Fragment itself
    private FilterFragment filterFragment; // a reference to itself



    public FilterFragment(){}

    public FilterFragment(Integer mode){
        // should always call filter fragment with this constructor, the empty one should never be used
        // the ArrayList<Integer> is passed by reference, so any change to it inside this fragment will also be changed inside Activity
        this.filterFragment = this;
        switch (mode) {
            case 1:
                // mode 1: moodHistory
                this.communicator = FirestoreUserDocCommunicator.getInstance();
                this.mode = mode;
                this.filterList = communicator.getMoodHistoryFilterList();
                break;
            case 2:
                // mode 2: followingMoodList
                this.communicator = FirestoreUserDocCommunicator.getInstance();
                this.mode = mode;
                this.filterList = communicator.getFollowingFilterList();
                break;
            default:
                break;
        }
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filter, container, false);

        // link elements
        backBtn = view.findViewById(R.id.filter_back);
        resetBtn = view.findViewById(R.id.filter_reset);

        moodSelectList = view.findViewById(R.id.recylerView_filterFrag);
        // recyclerView
        recyclerViewLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        moodSelectAdapter = new SelectMoodFilterAdapter(filterList, filterFragment);
        moodSelectList.setLayoutManager(recyclerViewLayoutManager);
        moodSelectList.setAdapter(moodSelectAdapter);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeFragment();
            }
        });

        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterList.clear();
                moodSelectAdapter = new SelectMoodFilterAdapter(filterList, filterFragment);
                moodSelectList.setAdapter(moodSelectAdapter);
                refreshMoodList();
                changeFilterButtonState();
            }
        });

        moodSelectList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshMoodList();
                changeFilterButtonState();
            }
        });

        return view;
    }

    /**
     * Closes the fragment
     */
    private void closeFragment(){
        getFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                .remove(this)
                .commit();
    }

    /**
     * utility method for pressing and deselecting a mood filter
     */
    public void changeFilterButtonState(){
        if (filterList.isEmpty()){
            popFilterButton();
        }else{
            pressFilterButton();
        }
    }

    /**
     * Refreshes the moodlist that is shown, used for when a filter is changed
     */
    public void refreshMoodList() {
        switch (mode){
            case 1:
                ((MoodHistoryFragment)getFragmentManager().findFragmentByTag("MoodHistoryFragment")).refreshMoodList();
                break;
            case 2:
                ((FollowingFragment)getFragmentManager().findFragmentByTag("FollowingFragment")).refreshMoodList();
                break;
        }
    }

    /**
     * Functionality for pressing a filter button
     */
    private void pressFilterButton(){
        switch (mode){
            case 1:
                ((MoodHistoryFragment)getFragmentManager().findFragmentByTag("MoodHistoryFragment")).filterButtonPressed();
                break;
            case 2:
                ((FollowingFragment)getFragmentManager().findFragmentByTag("FollowingFragment")).filterButtonPressed();
                break;
        }
    }

    /**
     * functionality for deselecting a filter button
     */
    private void popFilterButton(){
        switch (mode){
            case 1:
                ((MoodHistoryFragment)getFragmentManager().findFragmentByTag("MoodHistoryFragment")).filterButtonPopped();
                break;
            case 2:
                ((FollowingFragment)getFragmentManager().findFragmentByTag("FollowingFragment")).filterButtonPopped();
                break;
        }
    }


    @Override
    public void onResume() {
        super.onResume();

        if(getView() == null){
            return;
        }

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
                    closeFragment();
                    return true;
                }
                return false;
            }
        });
    }

}
