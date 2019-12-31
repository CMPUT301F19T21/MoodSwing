package com.example.moodswing.customDataTypes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moodswing.MainActivity;
import com.example.moodswing.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

/**
 * the adapter for the moodlist
 */
public class MoodAdapter extends RecyclerView.Adapter<MoodAdapter.MyViewHolder> {

    private ArrayList<MoodEvent> moods;
//    private Integer selectedPosition; // note: use of this attribute MAY cause bug (not matching) because of realtime listner,
//    // need to invest more later! - Scott (especially on following screen, where the card at position can be changed in realtime)

    /**
     * Holds all the views for the fields
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView moodType;
        TextView dateText;
        TextView timeText;
        ImageView moodImage;
        ImageView locationImage;
        CardView moodHistoryCard;
        FloatingActionButton colorCyc;

        public MyViewHolder(View view){
            super(view);
            this.moodType = view.findViewById(R.id.moodDetail_moodText);
            this.dateText = view.findViewById(R.id.moodDetail_dateText);
            this.timeText = view.findViewById(R.id.moodDetail_timeText);
            this.moodImage = view.findViewById(R.id.moodIcon_placeHolder);
            this.locationImage = view.findViewById(R.id.location_moodListCard);
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
                .inflate(R.layout.content_mood_list, parent, false);
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder (final MyViewHolder holder, final int position) {
        TextView moodType = holder.moodType;
        TextView dateText = holder.dateText;
        TextView timeText = holder.timeText;
        ImageView moodImage = holder.moodImage;
        ImageView locationImage = holder.locationImage;
        MoodEvent moodEvent = moods.get(position);
        Integer moodTypeInt = moodEvent.getMoodType();

        dateText.setText(MoodEventUtility.getDateStr(moodEvent.getTimeStamp()));
        timeText.setText(MoodEventUtility.getTimeStr(moodEvent.getTimeStamp()));
        moodType.setText(MoodEventUtility.getMoodType(moodTypeInt));
        moodImage.setImageResource(MoodEventUtility.getMoodDrawableInt(moodTypeInt));

        if (moodEvent.getLatitude() == null) {
            locationImage.setImageResource(R.drawable.ic_location_off_grey_24dp);
        }else{
            locationImage.setImageResource(R.drawable.ic_location_on_accent_red_24dp);
        }

        holder.moodHistoryCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDetailedViewActivity(holder.getLayoutPosition(),v);
            }
        });
    }


    /**
     * Starts the detailed view activity with the position of the mood card
     * @param cardPosition This is the position of the card, currently it can be in range 1-4 inc.
     * @param view the current view passed
     */
    private void startDetailedViewActivity (int cardPosition,View view){
        // cardPosition will be passed to detailed view
        ((MainActivity) view.getContext()).toDetailedView(cardPosition);
    }

    @Override
    public int getItemCount() {
        return moods.size();
    }

}