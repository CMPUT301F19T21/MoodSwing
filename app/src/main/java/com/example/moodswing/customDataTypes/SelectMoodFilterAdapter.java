package com.example.moodswing.customDataTypes;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.moodswing.Fragments.FilterFragment;
import com.example.moodswing.R;

import java.util.ArrayList;

public class SelectMoodFilterAdapter extends SelectMoodAdapter{
    private ArrayList<Integer> filterList;
    private ArrayList<Integer> moodTypes;
    private FilterFragment filterFragment;

//    public SelectMoodFilterAdapter(){
//        super();
//    }
    public SelectMoodFilterAdapter(ArrayList<Integer> filterList, FilterFragment filterFragment){
        super();
        this.filterList = filterList;
        this.moodTypes = super.getMoodTypes();
        this.filterFragment = filterFragment;
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
            case 5:
                moodTypeText.setText("HEART BROKEN");
                moodImage.setImageResource(R.drawable.mood5);
                break;
            case 6:
                moodTypeText.setText("IN LOVE");
                moodImage.setImageResource(R.drawable.mood6);
                break;
            case 7:
                moodTypeText.setText("SCARED");
                moodImage.setImageResource(R.drawable.mood7);
                break;
            case 8:
                moodTypeText.setText("SURPRISED");
                moodImage.setImageResource(R.drawable.mood8);
                break;
        }
        // preSelect
        if (filterList.contains(super.getMoodTypes().get(position))){
            holder.card.setElevation(2f);
            holder.card.setCardBackgroundColor(Color.parseColor("#F5F5F5"));
        }

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer moodTypeInt = moodTypes.get(holder.getLayoutPosition());
                if (filterList.contains(moodTypeInt)) {
                    filterList.remove(moodTypeInt);
                    holder.card.setElevation(5f);
                    holder.card.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                    filterFragment.refreshMoodList();
                    filterFragment.changeFilterButtonState();
                }else{
                    filterList.add(moodTypeInt);
                    holder.card.setElevation(2f);
                    holder.card.setCardBackgroundColor(Color.parseColor("#F5F5F5"));
                    filterFragment.refreshMoodList();
                    filterFragment.changeFilterButtonState();
                }
            }
        });
    }

}
