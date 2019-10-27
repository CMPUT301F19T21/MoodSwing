package com.example.moodswing;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MoodAdapter extends RecyclerView.Adapter<MoodAdapter.MyViewHolder> {

    private ArrayList<MoodEvent> moods;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView moodType;
        TextView dateText;
        TextView timeText;

        public MyViewHolder(View view){
            super(view);
            this.moodType = view.findViewById(R.id.moodText);
            this.dateText = view.findViewById(R.id.dateText);
            this.timeText = view.findViewById(R.id.timeText);
        }


    }

    public MoodAdapter(ArrayList<MoodEvent> moods) {
        //Customize the list
        this.moods = moods;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mood_list_content, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder (final MyViewHolder holder, final int position) {
        TextView moodType = holder.moodType;
        TextView dateText = holder.dateText;
        TextView timeText = holder.timeText;

        MoodEvent moodEvent = moods.get(position);
        DateJar date = moodEvent.getDate();
        String dateStr = String.format("%04d-%02d-%02d",date.getYear(),date.getMonth(),date.getDay());
        dateText.setText(dateStr);

        TimeJar time = moodEvent.getTime();
        String timeStr = String.format("%02d : %02d",time.getHr(),time.getMin());
        timeText.setText(timeStr);

        Integer moodTypeInt = moodEvent.getMoodType();
        moodType.setText(moodTypeInt.toString());
    }

    @Override
    public int getItemCount() {
        return moods.size();
    }

    public void clearMoodEvents(){
        this.moods.clear();
    }


    public void addToMoods(MoodEvent moodEvent){
        this.moods.add(moodEvent);
    }
}