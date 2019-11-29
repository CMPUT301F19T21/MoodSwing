package com.example.moodswing.customDataTypes;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.common.collect.Queues;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;

/**
 * Used as a cache for hosting images locally
 */

public class RecentImagesBox {
    private static final String TAG = "RecentImagesBox";
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

    /**
     * gets the latest used position
     * @return the minimum position
     */
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

    /**
     *  return null full
     *     return the position of next empty
     * @return the next empty position
     */
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


    /**
     * Clears the position
     * @param position the position to be cleared
     */
    private void clearPosition(int position){
        timeStamps.set(position, null);
        imageViews.set(position, null);
        imageIDs.set(position, null);
    }

    /**
     * finding the position of the image with the associated image ID
     * @param imageID the image name
     * @return the position of the image
     */
    private Integer findPosition(String imageID){
        Log.d(TAG, "this is imageID");
        Log.d(TAG, imageID);
        // get position
        int position = 0;
        for (String id : imageIDs){
            if ((id != null)&&(id.equals(imageID))){
                Log.d(TAG, "this is id inside imageID");
                Log.d(TAG, id);
                Log.d(TAG, "this is position");
                Log.d(TAG, ((Integer) position).toString());
                return position;
            }
            position++;
        }
        return null;
    }

    /**
     * Deletes the image
     * @param imageID the image to be deleted
     */
    public void delImage(String imageID){
        Integer position = findPosition(imageID);
        if (position != null){
            clearPosition(position);  // del
        }
    }

    /**
     * adds an image to the next empty position
     * @param imageID the image ID of the image to be added
     * @param imageDrawable the graphic to be drawn
     */
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

    /**
     * gets an image with a specific string
     * @param imageID the image ID
     * @return returns the image if found, null if not
     */
    public Drawable getImage(String imageID){
        Integer position = findPosition(imageID);
        if (position != null){
            timeStamps.set(position, Calendar.getInstance().getTimeInMillis());
            Log.d(TAG, "getImage: YES!");
            return imageViews.get(position);
        }
        return null;
    }
}
