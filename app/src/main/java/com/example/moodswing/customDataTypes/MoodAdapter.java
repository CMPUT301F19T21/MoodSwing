package com.example.moodswing.customDataTypes;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moodswing.MoodDetailActivity;
import com.example.moodswing.R;

import java.util.ArrayList;
import java.util.Locale;

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
            this.moodType = view.findViewById(R.id.moodText);
            this.dateText = view.findViewById(R.id.dateText);
            this.timeText = view.findViewById(R.id.timeText);
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

        dateText.setText(getDateStr(moodEvent.getDate()));
        timeText.setText(getTimeStr(moodEvent.getTime()));
        printMoodTypeToCard(moodEvent.getMoodType(),moodType, moodImage);

        holder.moodHistoryCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDetailedViewActivity(holder.getLayoutPosition(),v);
            }
        });
    }


    private void startDetailedViewActivity (int cardPosition,View v){
        // cardPosition will be passed to detailed view
        Intent intent = new Intent(v.getContext(), MoodDetailActivity.class);
        intent.putExtra("position",cardPosition);
        v.getContext().startActivity(intent);

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
                moodText.setText("EDMOTIONAL");
                moodImage.setImageResource(R.drawable.mood4);
                break;
        }
    }

    private String getDateStr (DateJar date) {
        String month = returnMonthStr(date.getMonth());
        return String.format(Locale.getDefault(), "%s %d, %d",month,date.getDay(),date.getYear());
    }

    private String getTimeStr (TimeJar time) {
        int hr = time.getHr();
        String period;
        if (hr>12){
            hr =hr-12;
            period = "PM";
        }
        else period = "AM";
        return String.format(Locale.getDefault(), "%d:%02d %s",hr,time.getMin(),period);
    }

    private String returnMonthStr(int monthInt){
        String monthStr = null;
        switch (monthInt){
            case 0:
                monthStr = "January";
                break;
            case 1:
                monthStr = "February";
                break;
            case 2:
                monthStr = "March";
                break;
            case 3:
                monthStr = "April";
                break;
            case 4:
                monthStr = "May";
                break;
            case 5:
                monthStr = "June";
                break;
            case 6:
                monthStr = "July";
                break;
            case 7:
                monthStr = "August";
                break;
            case 8:
                monthStr = "September";
                break;
            case 9:
                monthStr = "October";
                break;
            case 10:
                monthStr = "November";
                break;
            case 11:
                monthStr = "December";
                break;
        }
        return monthStr;
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