package com.example.moodswing.customDataTypes;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moodswing.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SelectMoodAdapter extends RecyclerView.Adapter<SelectMoodAdapter.MyViewHolder> {

//    private List<Integer> moodID;
//    private List<String> moodText;
//    private LayoutInflater mInflater;
//    private ItemClickListener mClickListener;
//    private Integer selectedPosition = -1;
//
//
//
//
//    // data is passed into the constructor
//    SelectMoodAdapter(Context context, List<Integer> moodID_, List<String> moodText_) {
//        this.mInflater = LayoutInflater.from(context);
//        this.moodID = moodID_;
//        this.moodText = moodText_;
//    }
    private ArrayList<Integer> moodTypes;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView moodTypeText;
        ImageView moodImage;

        public MyViewHolder(View view){
            super(view);
            this.moodTypeText = view.findViewById(R.id.moodType_Text);
            this.moodImage = view.findViewById(R.id.moodIcon);
        }

    }

    public  SelectMoodAdapter() {
        moodTypes = new ArrayList<>();
        for (int i = 0; i < 5; i++){
            moodTypes.add(i);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder (ViewGroup parent, int viewTYype) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.selectmood_content, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        TextView moodTypeText= holder.moodTypeText;
        ImageView moodImage= holder.moodImage;

        moodTypeText.setText("HAPPY");
        moodImage.setImageResource(R.drawable.mood1);

    }

    @Override
    public int getItemCount() {
        return moodTypes.size();
    }
}



    // binds the data to the view and textview in each row
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        int id = moodID.get(position);
//
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                selectedPosition = position;
//                notifyDataSetChanged();
//
//            }
//        });
//
//        String moodName = moodText.get(position);
//        if (id == 1) {
//            holder.myImageView.setImageResource(R.drawable.happymood);
//        }
//        else if (id == 2) {
//            holder.myImageView.setImageResource(R.drawable.angrymood);
//        }
//        else if (id == 3) {
//            holder.myImageView.setImageResource(R.drawable.emotionalmood);
//        }
//        else if (id == 4) {
//            holder.myImageView.setImageResource(R.drawable.sadmood);
//        }
//        holder.myTextView.setText(moodName);
//
//
//        if(selectedPosition==position) {
//            holder.itemView.setBackgroundColor(Color.LTGRAY);
//        }
//        else {
//            holder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
//        }
//
//
//
//    }
//
//    // total number of rows
//    @Override
//    public int getItemCount() {
//        return moodText.size();
//    }
//
//    // stores and recycles views as they are scrolled off screen
//    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
//        ImageView myImageView;
//        TextView myTextView;
//
//        ViewHolder(View itemView) {
//            super(itemView);
//            myImageView = itemView.findViewById(R.id.add_mood_icon);
//            myTextView = itemView.findViewById(R.id.add_Mood_text);
//            itemView.setOnClickListener(this);
//        }
//
//        @Override
//        public void onClick(View view) {
//            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
//        }
//    }
//
//    // convenience method for getting data at click position
//    public String getItem(int id) {
//        return moodText.get(id);
//    }
//
//    // allows clicks events to be caught
//    public void setClickListener(ItemClickListener itemClickListener) {
//        this.mClickListener = itemClickListener;
//    }
//
//    // parent activity will implement this method to respond to click events
//    public interface ItemClickListener {
//        void onItemClick(View view, int position);
//    }
//}