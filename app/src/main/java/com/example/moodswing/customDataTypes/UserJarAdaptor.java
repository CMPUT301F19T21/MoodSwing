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
import com.google.firebase.firestore.auth.User;

import java.lang.reflect.Array;
import java.util.ArrayList;
//

/**
 * This class is an adapter for UserJars to be shown, will be used for following/follower list
 *
 */
public class UserJarAdaptor extends RecyclerView.Adapter<UserJarAdaptor.MyViewHolder> {

    private ArrayList<UserJar> userJars;
//    private Integer selectedPosition; // note: use of this attribute MAY cause bug (not matching) because of realtime listner,
//    // need to invest more later! - Scott (especially on following screen, where the card at position can be changed in realtime)

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView moodType;
        TextView dateText;
        TextView timeText;
        TextView username;
        ImageView moodImage;
        CardView userJarCard;

        public MyViewHolder(View view){
            super(view);
            this.moodType = view.findViewById(R.id.followingList_moodDetail_moodText);
            this.dateText = view.findViewById(R.id.followingList_moodDetail_dateText);
            this.timeText = view.findViewById(R.id.followingList_moodDetail_timeText);
            this.username = view.findViewById(R.id.followingList_username);
            this.moodImage = view.findViewById(R.id.followingList_moodIcon_placeHolder);
            this.userJarCard = view.findViewById(R.id.followingList_moodCard);
        }
    }

    /**
     * intializes the arraylist of UserJars for the adapter
     * @param userJars the arraylist to display
     */
    public UserJarAdaptor(ArrayList<UserJar> userJars) {
        //Customize the list
        this.userJars = userJars;
//        this.selectedPosition = null;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mood_list_content_following, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder (final MyViewHolder holder, final int position) {
        TextView moodType = holder.moodType;
        TextView dateText = holder.dateText;
        TextView timeText = holder.timeText;
        TextView usernameTextView = holder.username;

        ImageView moodImage = holder.moodImage;

        UserJar userJar = userJars.get(position);
        MoodEvent moodEvent = userJar.getMoodEvent();

        dateText.setText(MoodEventUtility.getDateStr(moodEvent.getDate()));
        timeText.setText(MoodEventUtility.getTimeStr(moodEvent.getTime()));
        printMoodTypeToCard(moodEvent.getMoodType(),moodType, moodImage);
        usernameTextView.setText(userJar.getUsername());

        holder.userJarCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // start detailed moodEvent (will be slightly modified version).
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
        return userJars.size();
    }

    /**
     * clears the arraylist of UserJars
     */
    public void clearUserJars(){
        this.userJars.clear();
    }

    /**
     * adds a user to the UserJar arraylist
     * @param userJar the UserJar to add
     */
    public void addToUserJars(UserJar userJar){
        this.userJars.add(userJar);
    }

    public ArrayList<UserJar> getUserJars() {
        return userJars;
    }
}