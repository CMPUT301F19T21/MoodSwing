package com.example.moodswing.customDataTypes;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.common.collect.Queues;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;

public class RecentImagesBox {
    private ArrayList<Drawable> imageViews;
    private ArrayList<String> imageIDs;
    private ArrayList<Long> timeStamps;
    private Integer capacity;

    public RecentImagesBox(){
        imageViews = new ArrayList<>();
        imageIDs = new ArrayList<>();
        timeStamps = new ArrayList<>();
        capacity = 10;

        // init
        for (int i=0; i<capacity;i++){
            imageViews.add(null);
            imageIDs.add(null);
            timeStamps.add(null);
        }
    }

    private Integer getLatestUsed(){
        Long minUTC = null;
        Integer minPosition = null;
        int position = 0;
        for (Long timestamp : timeStamps){
            if (timestamp != null){
                if ((minUTC == null)||(minUTC>timestamp)){
                    minUTC = timestamp;
                    minPosition = position;
                }
            }
            position++;
        }

        if (minUTC == null){
            return null;
        }else{
            return minPosition;
        }
    }

    // return null full
    // return the position of next empty
    private Integer getNextEmpty(){
        int position = 0;
        for (String id : imageIDs){
            if (id == null){
                break;
            }
            position++;
        }

        if (capacity == position){
            return null;
        }else{
            return position;
        }
    }



    private void clearPosition(int position){
        timeStamps.set(position, null);
        imageViews.set(position, null);
        imageIDs.set(position, null);
    }

    private Integer findPosition(String imageID){
        // get position
        int position = 0;
        for (String id : imageIDs){
            if (id == imageID){
                break;
            }
            position++;
        }

        if (position == capacity){
            return null;
        }else{
            return position;
        }
    }

    public void delImage(String imageID){
        Integer position = findPosition(imageID);
        if (position != null){
            clearPosition(position);  // del
        }
    }

    public void addImage(String imageID, Drawable imageDrawable){
        Integer nextPosition = getNextEmpty();
        if (nextPosition == null){
            Integer position = getLatestUsed(); // impossible to be null in this case
            if (position != null){
                clearPosition(position);
                addImage(imageID, imageDrawable);
            }
        }else{
            timeStamps.set(nextPosition, Calendar.getInstance().getTimeInMillis());
            imageViews.set(nextPosition, imageDrawable);
            imageIDs.set(nextPosition, imageID);
        }
    }

    public Drawable getImage(String imageID){
        Integer position = findPosition(imageID);
        if (position != null){
            timeStamps.set(position, Calendar.getInstance().getTimeInMillis());
            return imageViews.get(position);
        }
        return null;
    }
}
