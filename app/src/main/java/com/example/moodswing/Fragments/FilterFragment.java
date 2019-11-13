package com.example.moodswing.Fragments;

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


/**
 * The user profile. It will show the username of the user, have the logout button, and may eventually have functionality
 * related to the user such as change username, adding a profile picture
 */
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



    public FilterFragment(){}

    public FilterFragment(RecyclerView moodListRecyclerView, ArrayList<Integer> filterList){
        // should always call filter fragment with this constructor, the empty one should never be used
        // the ArrayList<Integer> is passed by reference, so any change to it inside this fragment will also be changed inside Activity
        this.communicator = FirestoreUserDocCommunicator.getInstance();
        this.moodListRecyclerView = moodListRecyclerView;
        this.filterList = filterList;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Profile buttons created and redirects to appropriate activities(logout and back). Shows the user's username as a TextView
     */
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

        setUpFilterBtnListeners();



        return view;
    }

    private void setUpFilterBtnListeners(){
        //
    }
}
