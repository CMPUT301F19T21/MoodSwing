package com.example.moodswing.customDataTypes;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moodswing.R;

import java.util.ArrayList;

import static com.example.moodswing.customDataTypes.MoodEventUtility.TOTAL_MOOD_TYPE_COUNTS;


/**
 * this class is an adapter for selecting the mood
 */

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

    /**
     * initializes an array of numbers that correspond to each mood in the view, sets the one selected to null
     */
    public SelectMoodAdapter() {
        selectedPosition = null;
        moodTypes = new ArrayList<>();
        for (int i = 1; i <= TOTAL_MOOD_TYPE_COUNTS; i++){
            moodTypes.add(i);
        }
    }

    public SelectMoodAdapter(int moodType) {
        this();
        this.selectedPosition = getSelectedPosition(moodType);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.content_selectmood, parent, false);
        return new MyViewHolder(view);
    }

    /**
     * Sets the viewable card on screen to have the mood text(ie HAPPY), and the
     * associated picture(ie. a happy face)
     */
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
        }
        // preSelect
        if (selectedPosition != null) {
            if (selectedPosition == position){
                holder.card.setElevation(2f);
                holder.card.setCardBackgroundColor(Color.parseColor("#F5F5F5"));
            }
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
                    holder.card.setElevation(5f);
                    holder.card.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                }
            }
        });
    }

    // useful for its subclass
    public ArrayList<Integer> getMoodTypes() {
        return moodTypes;
    }

    @Override
    public int getItemCount() {
        return moodTypes.size();
    }

    /**
     * returns the selected mood
     * @return the selected mood
     */
    public Integer getSelectedMoodType(){
        if (selectedPosition != null) {
            return moodTypes.get(selectedPosition);
        }else{
            return null;
        }
    }

    private Integer getSelectedPosition(int moodType){
        return moodTypes.indexOf(moodType);
    }
}