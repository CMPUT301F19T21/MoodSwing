package com.example.moodswing.customDataTypes;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moodswing.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.opencensus.resource.Resource;

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
    private Integer selectedPosition;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView moodTypeText;
        ImageView moodImage;
        CardView card;

        public MyViewHolder(View view){
            super(view);
            this.moodTypeText = view.findViewById(R.id.moodType_Text);
            this.moodImage = view.findViewById(R.id.moodIcon);
            this.card = view.findViewById(R.id.selectCard);
        }
    }

    public  SelectMoodAdapter() {
        selectedPosition = null;
        moodTypes = new ArrayList<>();
        for (int i = 1; i < 5; i++){
            moodTypes.add(i);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.selectmood_content, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        TextView moodTypeText= holder.moodTypeText;
        ImageView moodImage= holder.moodImage;
        int moodType = moodTypes.get(position);

        switch (moodType){
            case 1:
                moodTypeText.setText("HAPPY");
                moodImage.setImageResource(R.drawable.mood1);
                break;
            case 2:
                moodTypeText.setText("SAD");
                moodImage.setImageResource(R.drawable.mood2);
                break;
            case 3:
                moodTypeText.setText("ANGRY");
                moodImage.setImageResource(R.drawable.mood3);
                break;
            case 4:
                moodTypeText.setText("EMOTIONAL");
                moodImage.setImageResource(R.drawable.mood4);
                break;
        }

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedPosition == null) {
                    selectedPosition = holder.getLayoutPosition();
                    holder.card.setElevation(2f);
                    holder.card.setCardBackgroundColor(Color.parseColor("#F5F5F5"));
                }else if (selectedPosition == holder.getLayoutPosition()){
                    selectedPosition = null;
                    holder.card.setElevation(12f);
                    holder.card.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return moodTypes.size();
    }

    public Integer getSelectedMoodType(){
        if (selectedPosition != null) {
            return moodTypes.get(selectedPosition);
        }else{
            return null;
        }
    }
}