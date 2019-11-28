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
     * Gets a DateJar object and returns a string of the date stored
     * @param date The DateJar object to be converted into a string
     * @return Returns a string of the DateJar's date
     */
    public static String getDateStr (DateJar date) {
        String month = returnMonthStr(date.getMonth());
        return String.format(Locale.getDefault(), "%s %d, %d",month,date.getDay(),date.getYear());
    }

    /**
     * Gets a TimeJar object and returns a string of the time stored
     * @param utc the UTC object to be converted into a string
     * @return Returns a string of the TimeJar's time
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

    /**
     * Converts the numeric month into the word
     * @param monthInt the month as an int, starting at 0 for January
     * @return the month as a string
     */
    public static String returnMonthStr(int monthInt){
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
}
