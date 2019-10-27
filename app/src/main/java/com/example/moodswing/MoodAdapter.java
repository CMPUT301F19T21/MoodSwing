package com.example.moodswing;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class MoodAdapter extends ArrayAdapter<MoodEvent> {

    private ArrayList<MoodEvent> moods;
    private Context context;

    public MoodAdapter(Context context, ArrayList<MoodEvent> moods) {
        //Customize the list
        super(context, 0, moods);
        this.moods = moods;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @Nullable ViewGroup parent) {
        //Customize the view
        View view = convertView;
        MoodEvent moodEvent = moods.get(position);

        if(view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.mood_list_content, parent, false);
        }

        TextView moodType = view.findViewById(R.id.moodText);
        TextView dateText = view.findViewById(R.id.dateText);
        TextView timeText = view.findViewById(R.id.timeText);

        DateJar date = moodEvent.getDate();
        String dateStr = String.format("%04d-%02d-%02d",date.getYear(),date.getMonth(),date.getDay());
        dateText.setText(dateStr);

        TimeJar time = moodEvent.getTime();
        String timeStr = String.format("%02d : %02d",time.getHr(),time.getMin());
        timeText.setText(timeStr);

        return view;
    }

    public void clearMoodEvents(){
        this.moods.clear();
    }


    public void addToMoods(MoodEvent moodEvent){
        this.moods.add(moodEvent);
    }
}