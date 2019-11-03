package com.example.moodswing.customDataTypes;

import com.example.moodswing.R;

import java.util.ArrayList;

public class MoodType {
    ArrayList<MoodType> moodTypes;
    private int imageResource;
    private String MoodName;
    private int moodType;
    public MoodType(int imageResource,String MoodName,int moodType){
        this.imageResource = imageResource;
        this.MoodName = MoodName;
        this.moodType = moodType;
    }

    public ArrayList<MoodType> getMoodTypes() {
        return moodTypes;
    }

    public int getImageResource() {
        return imageResource;
    }

    public String getMoodName() {
        return MoodName;
    }

    public int getMoodType() {
        return moodType;
    }
}
