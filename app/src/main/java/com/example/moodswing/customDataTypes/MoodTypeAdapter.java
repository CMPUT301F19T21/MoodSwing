package com.example.moodswing.customDataTypes;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moodswing.R;

import java.util.ArrayList;
import java.util.List;

public class MoodTypeAdapter extends RecyclerView.Adapter<MoodTypeAdapter.MoodViewHolder> {

    private ArrayList<MoodType> moodTypesList;

    public static class MoodViewHolder extends RecyclerView.ViewHolder{
        public ImageView imageView;
        public MoodViewHolder(View view){
            super(view);
            imageView = view.findViewById(R.id.mood_image);
        }
    }

    public MoodTypeAdapter(ArrayList<MoodType> moodTypeList){
        this.moodTypesList = moodTypesList;
    }

    @NonNull
    @Override
    public MoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.mood_content,parent,false);
        MoodViewHolder holder = new MoodViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MoodViewHolder holder, int position) {
        MoodType moodType = moodTypesList.get(position);
        holder.imageView.setImageResource(moodType.getImageResource());
    }

    @Override
    public int getItemCount() {
        return 1;
    }
}
