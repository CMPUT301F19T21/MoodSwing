package com.example.moodswing;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MoodAdapter extends RecyclerView.Adapter<MoodAdapter.MyViewHolder> {

    private ArrayList<MoodEvent> moods;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void OnItemiClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView moodType;
        TextView dateText;
        TextView timeText;
        CheckBox checkBox;

        public MyViewHolder(View view, OnItemClickListener listener){
            super(view);
            this.moodType = view.findViewById(R.id.moodText);
            this.dateText = view.findViewById(R.id.dateText);
            this.timeText = view.findViewById(R.id.timeText);
            this.checkBox = view.findViewById(R.id.checkbox);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION);{
                            listener.OnItemiClick(position);
                        }
                    }
                }
            });
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
        MyViewHolder myViewHolder = new MyViewHolder(view, mListener);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder (final MyViewHolder holder, final int position) {
        TextView moodType = holder.moodType;
        TextView dateText = holder.dateText;
        TextView timeText = holder.timeText;
        CheckBox checkBox = holder.checkBox;

        final MoodEvent moodEvent = moods.get(position);
        DateJar date = moodEvent.getDate();
        String dateStr = String.format("%04d-%02d-%02d",date.getYear(),date.getMonth(),date.getDay());
        dateText.setText(dateStr);

        TimeJar time = moodEvent.getTime();
        String timeStr = String.format("%02d : %02d",time.getHr(),time.getMin());
        timeText.setText(timeStr);

        Integer moodTypeInt = moodEvent.getMoodType();
        moodType.setText(moodTypeInt.toString());

        checkBox.setOnCheckedChangeListener(null);
        checkBox.setSelected(moodEvent.isSelected());
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    moodEvent.setSelected(true);
                }else {
                    moodEvent.setSelected(false);
                }
            }
        });
        checkBox.setChecked(moodEvent.isSelected());
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