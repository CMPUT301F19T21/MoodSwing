package com.example.moodswing.customDataTypes;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * This is a utility class to handle functionality for mood objects and their arguments
 */
public final class MoodEventUtility {



    public MoodEventUtility(){
        // nothing
    }

    /**
     * Gets Long UTC(universal coordinated time) and returns a string date
     * @param utc the UTC object to be converted into a string
     * @return Returns a string from UTC in Month,Day,Year format
     */
    public static String getDateStr (Long utc) {
        SimpleDateFormat outputFormat = new SimpleDateFormat("MMMM d, yyyy");
        return outputFormat.format(utc);

    }

    /**
     * Gets Long UTC(universal coordinated time) and returns a string of the time stored
     * @param utc the UTC object to be converted into a string
     * @return Returns a string from UTC in am/pm time
     */
    public static String getTimeStr (Long utc) {

        SimpleDateFormat outputFormat = new SimpleDateFormat("h:mm a");
        return outputFormat.format(utc);
    }

    /**
     * Each mood is associated with a number, this method finds the string description when given the number
     * @param moodTypeInt the int value of the mood
     * @return The string value of the mood
     */
    public static String getMoodType (int moodTypeInt) {
        String moodTypeString = null;
        switch (moodTypeInt){
            case 1:
                moodTypeString = "Happy";
                break;
            case 2:
                moodTypeString = "Sad";
                break;
            case 3:
                moodTypeString = "Angry";
                break;
            case 4:
                moodTypeString = "Emotional";
                break;
        }
        return moodTypeString;
    }

}
