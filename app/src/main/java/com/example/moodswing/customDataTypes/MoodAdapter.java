package com.example.moodswing.customDataTypes;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moodswing.Fragments.MoodDetailFragment;
import com.example.moodswing.MainActivity;
import com.example.moodswing.R;

import java.util.ArrayList;

public class MoodAdapter extends RecyclerView.Adapter<MoodAdapter.MyViewHolder> {

    private ArrayList<MoodEvent> moods;
//    private Integer selectedPosition; // note: use of this attribute MAY cause bug (not matching) because of realtime listner,
//    // need to invest more later! - Scott (especially on following screen, where the card at position can be changed in realtime)

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView moodType;
        TextView dateText;
        TextView timeText;
        ImageView moodImage;
        CardView moodHistoryCard;

        public MyViewHolder(View view){
            super(view);
            this.moodType = view.findViewById(R.id.moodDetail_moodText);
            this.dateText = view.findViewById(R.id.moodDetail_dateText);
            this.timeText = view.findViewById(R.id.moodDetail_timeText);
            this.moodImage = view.findViewById(R.id.moodIcon_placeHolder);
            this.moodHistoryCard = view.findViewById(R.id.moodhistory_card);
        }
    }

    public MoodAdapter(ArrayList<MoodEvent> moods) {
        //Customize the list
        this.moods = moods;
//        this.selectedPosition = null;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mood_list_content, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder (final MyViewHolder holder, final int position) {
        TextView moodType = holder.moodType;
        TextView dateText = holder.dateText;
        TextView timeText = holder.timeText;
        ImageView moodImage = holder.moodImage;

        MoodEvent moodEvent = moods.get(position);

        dateText.setText(MoodEventUtility.getDateStr(moodEvent.getDate()));
        timeText.setText(MoodEventUtility.getTimeStr(moodEvent.getTime()));
        printMoodTypeToCard(moodEvent.getMoodType(),moodType, moodImage);

        holder.moodHistoryCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDetailedViewActivity(holder.getLayoutPosition(),v);
            }
        });
    }


    private void startDetailedViewActivity (int cardPosition,View view){
        // cardPosition will be passed to detailed view
        ((MainActivity) view.getContext()).toDetailedView(cardPosition);
    }

    private void printMoodTypeToCard(int moodTypeInt, TextView moodText, ImageView moodImage) {
        switch(moodTypeInt){
            case 1:
                moodText.setText("HAPPY");
                moodImage.setImageResource(R.drawable.mood1);
                break;
            case 2:
                moodText.setText("SAD");
                moodImage.setImageResource(R.drawable.mood2);
                break;
            case 3:
                moodText.setText("ANGRY");
                moodImage.setImageResource(R.drawable.mood3);
                break;
            case 4:
                moodText.setText("EMOTIONAL");
                moodImage.setImageResource(R.drawable.mood4);
                break;
        }
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

    public ArrayList<MoodEvent> getMoods() {
        return moods;
    }
}