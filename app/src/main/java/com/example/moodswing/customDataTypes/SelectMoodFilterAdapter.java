package com.example.moodswing.customDataTypes;

import android.content.res.ColorStateList;
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

        moodTypeText.setText(MoodEventUtility.getMoodType(moodType));
        moodImage.setImageResource(MoodEventUtility.getMoodDrawableInt(moodType));
        holder.color.setBackgroundTintList(
                ColorStateList.valueOf(holder
                        .holderView
                        .getResources()
                        .getColor(MoodEventUtility.getMoodColorResInt(moodType))));

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
