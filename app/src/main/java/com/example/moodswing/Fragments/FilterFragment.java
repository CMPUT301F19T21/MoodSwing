package com.example.moodswing.Fragments;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moodswing.MainActivity;
import com.example.moodswing.R;
import com.example.moodswing.customDataTypes.FirestoreUserDocCommunicator;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class FilterFragment extends DialogFragment {
    private FloatingActionButton backBtn;
    private FloatingActionButton resetBtn;

    // filter buttons
    private FloatingActionButton happyBtn;
    private FloatingActionButton sadBtn;
    private FloatingActionButton angryBtn;
    private FloatingActionButton emotionalBtn;

    // filter moodType List
    private ArrayList<Integer> filterList;
    private RecyclerView moodListRecyclerView;
    private FirestoreUserDocCommunicator communicator;
    private Integer mode;



    public FilterFragment(){}

    public FilterFragment(RecyclerView moodListRecyclerView, Integer mode){
        // should always call filter fragment with this constructor, the empty one should never be used
        // the ArrayList<Integer> is passed by reference, so any change to it inside this fragment will also be changed inside Activity

        switch (mode) {
            case 1:
                // mode 1: moodHistory
                this.communicator = FirestoreUserDocCommunicator.getInstance();
                this.moodListRecyclerView = moodListRecyclerView;
                this.mode = mode;
                this.filterList = communicator.getMoodHistoryFilterList();
                break;
            case 2:
                // mode 2: followingMoodList
                this.communicator = FirestoreUserDocCommunicator.getInstance();
                this.moodListRecyclerView = moodListRecyclerView;
                this.mode = mode;
                this.filterList = communicator.getFollowingFilterList();
                break;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filter, container, false);
        getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        // link elements
        backBtn = view.findViewById(R.id.filter_back);
        resetBtn = view.findViewById(R.id.filter_reset);

        happyBtn = view.findViewById(R.id.filter_happyBtn);
        sadBtn = view.findViewById(R.id.filter_sadBtn);
        angryBtn = view.findViewById(R.id.filter_angryBtn);
        emotionalBtn = view.findViewById(R.id.filter_emotionalBtn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // do something
                // 1. remove all types from the list
                // 2. pop all buttons
                // 3. refresh recyclerView
            }
        });

        loadBtnState();
        setUpFilterBtnListeners();

        return view;
    }

    private void loadBtnState(){
        if (!(filterList.isEmpty())){
            for (Integer moodType : filterList){
                switch (moodType){
                    case 1:
                        happyBtn.setCompatElevation(0f);
                        happyBtn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.color_button_lightGrey)));
                        break;
                    case 2:
                        sadBtn.setCompatElevation(0f);
                        sadBtn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.color_button_lightGrey)));
                        break;
                    case 3:
                        angryBtn.setCompatElevation(0f);
                        angryBtn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.color_button_lightGrey)));
                        break;
                    case 4:
                        emotionalBtn.setCompatElevation(0f);
                        emotionalBtn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.color_button_lightGrey)));
                        break;
                }
            }
        }
    }

    private void refreshMoodList() {
        switch (mode){
            case 1:
                communicator.initMoodEventsList(moodListRecyclerView, filterList);
                break;
            case 2:
                communicator.initFollowingList(moodListRecyclerView, filterList);
                break;
        }
    }

    private void setUpFilterBtnListeners(){

        happyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filterList.contains(1)){
                    // pop up
                    happyBtn.setCompatElevation(12f);
                    happyBtn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(android.R.color.white)));
                    // remove
                    filterList.remove(filterList.indexOf(1));
                    refreshMoodList();
                }else{
                    // press down
                    happyBtn.setCompatElevation(0f);
                    happyBtn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.color_button_lightGrey)));
                    // add
                    filterList.add(1);
                    refreshMoodList();
                }
            }
        });

        sadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filterList.contains(2)){
                    // pop up
                    sadBtn.setCompatElevation(12f);
                    sadBtn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(android.R.color.white)));
                    // remove
                    filterList.remove(filterList.indexOf(2));
                    refreshMoodList();
                }else{
                    // press down
                    sadBtn.setCompatElevation(0f);
                    sadBtn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.color_button_lightGrey)));
                    // add
                    filterList.add(2);
                    refreshMoodList();
                }
            }
        });

        angryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filterList.contains(3)){
                    // pop up
                    angryBtn.setCompatElevation(12f);
                    angryBtn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(android.R.color.white)));
                    // remove
                    filterList.remove(filterList.indexOf(3));
                    refreshMoodList();
                }else{
                    // press down
                    angryBtn.setCompatElevation(0f);
                    angryBtn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.color_button_lightGrey)));
                    // add
                    filterList.add(3);
                    refreshMoodList();
                }
            }
        });

        emotionalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filterList.contains(4)){
                    // pop up
                    emotionalBtn.setCompatElevation(12f);
                    emotionalBtn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(android.R.color.white)));
                    // remove
                    filterList.remove(filterList.indexOf(4));
                    refreshMoodList();
                }else{
                    // press down
                    emotionalBtn.setCompatElevation(0f);
                    emotionalBtn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.color_button_lightGrey)));
                    // add
                    filterList.add(4);
                    refreshMoodList();
                }
            }
        });
    }
}
