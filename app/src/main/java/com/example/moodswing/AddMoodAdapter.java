package com.example.moodswing;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AddMoodAdapter extends RecyclerView.Adapter<AddMoodAdapter.ViewHolder> {

    private List<Integer> moodID;
    private List<String> moodText;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    AddMoodAdapter(Context context, List<Integer> moodID_, List<String> moodText_) {
        this.mInflater = LayoutInflater.from(context);
        this.moodID = moodID_;
        this.moodText = moodText_;
    }

    // inflates the row layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.addmood_content, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the view and textview in each row
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int id = moodID.get(position);
        String moodName = moodText.get(position);
        if (id == 1) {
            holder.myImageView.setImageResource(R.drawable.happymood);
        }
        else if (id == 2) {
            holder.myImageView.setImageResource(R.drawable.angrymood);
        }
        else if (id == 3) {
            holder.myImageView.setImageResource(R.drawable.emotionalmood);
        }
        else if (id == 4) {
            holder.myImageView.setImageResource(R.drawable.sadmood);
        }
        holder.myTextView.setText(moodName);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return moodText.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView myImageView;
        TextView myTextView;

        ViewHolder(View itemView) {
            super(itemView);
            myImageView = itemView.findViewById(R.id.add_mood_icon);
            myTextView = itemView.findViewById(R.id.add_Mood_text);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    public String getItem(int id) {
        return moodText.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}