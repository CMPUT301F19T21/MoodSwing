package com.example.moodswing.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.moodswing.EditMoodActivity;
import com.example.moodswing.MainActivity;
import com.example.moodswing.R;
import com.example.moodswing.customDataTypes.DateJar;
import com.example.moodswing.customDataTypes.FirestoreUserDocCommunicator;
import com.example.moodswing.customDataTypes.MoodEvent;
import com.example.moodswing.customDataTypes.MoodEventUtility;
import com.example.moodswing.customDataTypes.TimeJar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormatSymbols;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

public class MoodDetailFragment extends Fragment{
    private FirestoreUserDocCommunicator communicator;
    MoodEvent moodEvent;

    private TextView dateText;
    private TextView timeText;
    private TextView moodText;
    private TextView reasonText;

    private FloatingActionButton delButton;
    private FloatingActionButton editButton;
    private FloatingActionButton backButton;
    private ImageView moodImage;

    private int moodPosition;

    public MoodDetailFragment(){}

    public MoodDetailFragment(int moodPosition) {
        this.communicator = FirestoreUserDocCommunicator.getInstance();
        this.moodPosition = moodPosition;
        this.moodEvent = communicator.getMoodEvent(moodPosition);
        // moodEvent

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.mood_details, container, false);

        // find view
        dateText = root.findViewById(R.id.moodDetail_dateText);
        timeText = root.findViewById(R.id.moodDetail_timeText);
        moodText = root.findViewById(R.id.moodDetail_moodText);
        reasonText = root.findViewById(R.id.detailedView_reasonText);
        delButton = root.findViewById(R.id.detailedView_delete);
        editButton = root.findViewById(R.id.detailedView_edit);
        backButton = root.findViewById(R.id.detailedView_back);
        moodImage = root.findViewById(R.id.detailedView_moodImg);

        initialElements();

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),EditMoodActivity.class);
                intent.putExtra("position",moodPosition);
                startActivityForResult(intent, 1);

            }
        });

        delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                communicator.removeMoodEvent(moodEvent);
                ((MainActivity)getActivity()).toMoodHistory();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        return root;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            initialElements();
        }
    }
    private void initialElements(){
        moodEvent = communicator.getMoodEvent(moodPosition);
        dateText.setText(MoodEventUtility.getDateStr(moodEvent.getDate()));
        timeText.setText(MoodEventUtility.getTimeStr(moodEvent.getTime()));
        moodText.setText(MoodEventUtility.getMoodType(moodEvent.getMoodType()));
        setMoodImage(moodEvent.getMoodType());
        setReasonText();


    }

    private void setReasonText(){
        if (moodEvent.getReason() != null){
            this.reasonText.setText(String.format(Locale.getDefault(), "\"%s\"",(moodEvent.getReason())));
        }else{
            this.reasonText.setText("");
        }
    }

    private void setMoodImage(int moodType){
        switch(moodType){
            case 1:
                moodText.setText("HAPPY");
                moodImage.setImageResource(R.drawable.mood1);
                break;
            case 2:
                moodText.setText("SAD");
                moodImage.setImageResource(R.drawable.mood2);
                break;
            case 3:
                moodText.setText("ANGRY");
                moodImage.setImageResource(R.drawable.mood3);
                break;
            case 4:
                moodText.setText("EDMOTIONAL");
                moodImage.setImageResource(R.drawable.mood4);
                break;
        }
    }

}
