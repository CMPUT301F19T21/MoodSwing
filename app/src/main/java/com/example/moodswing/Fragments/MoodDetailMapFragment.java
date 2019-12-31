package com.example.moodswing.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.moodswing.R;
import com.example.moodswing.customDataTypes.MoodEvent;
import com.example.moodswing.customDataTypes.MoodEventUtility;
import com.example.moodswing.customDataTypes.UserJar;

public class MoodDetailMapFragment extends Fragment {
    private MoodEvent moodEvent;

    private ImageView moodImage;
    private TextView dateText;
    private TextView timeText;

    public MoodDetailMapFragment(){}
    
    public MoodDetailMapFragment(MoodEvent moodEvent){
        this.moodEvent = moodEvent;
    }

    public MoodDetailMapFragment(UserJar userJar){
        this.moodEvent = userJar.getMoodEvent();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.content_mood_detail_map, container, false);

        moodImage = root.findViewById(R.id.moodDetail_map_image);
        dateText = root.findViewById(R.id.moodDetail_map_date);
        timeText = root.findViewById(R.id.moodDetail_map_time);

        this.setUpViewElements();

        return root;
    }

    private void setUpViewElements() {
        moodImage.setImageResource(MoodEventUtility.getMoodDrawableInt(moodEvent.getMoodType()));
        dateText.setText(MoodEventUtility.getDateStr(moodEvent.getTimeStamp()));
        timeText.setText(MoodEventUtility.getTimeStr(moodEvent.getTimeStamp()));
    }
}
