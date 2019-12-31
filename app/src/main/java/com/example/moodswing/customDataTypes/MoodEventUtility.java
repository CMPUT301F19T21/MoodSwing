package com.example.moodswing.customDataTypes;

import com.example.moodswing.R;

import java.text.SimpleDateFormat;

/**
 * This is a utility class to handle functionality for mood objects and their arguments
 */
public final class MoodEventUtility {
    public static final int MOODHISTORY_MODE = 1;
    public static final int FOLLOWING_MODE = 2;
    public static final int TOTAL_MOOD_TYPE_COUNTS = 8;

    // storage
    private static final String moodText[] = {
            "Happy",
            "Sad",
            "Angry",
            "Emotional",
            "Heart Broken",
            "In Love",
            "Scared",
            "Surprised",
    };
    private static final Integer moodDrawableInt[] = {
            R.drawable.mood1,
            R.drawable.mood2,
            R.drawable.mood3,
            R.drawable.mood4,
            R.drawable.mood5,
            R.drawable.mood6,
            R.drawable.mood7,
            R.drawable.mood8,
    };
    private static final Integer moodColorResInt[] = {
            R.color.mood1_color,
            R.color.mood2_color,
            R.color.mood3_color,
            R.color.mood4_color,
            R.color.mood5_color,
            R.color.mood6_color,
            R.color.mood7_color,
            R.color.mood8_color,
    };
    private static final Integer moodDrawableMapInt[] = {
            R.drawable.moodm1,
            R.drawable.moodm2,
            R.drawable.moodm3,
            R.drawable.moodm4,
            R.drawable.moodm5,
            R.drawable.moodm6,
            R.drawable.moodm7,
            R.drawable.moodm8,
    };


    // constance for Command Object
    public static final int COMMAND_ACTION_ADD = 1;
    public static final int COMMAND_ACTION_DELETE = 2;
    public static final int COMMAND_ACTION_UPDATE = 3;

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
        return moodText[moodTypeInt-1];
    }

    public static Integer getMoodDrawableInt (int moodTypeInt) {
        return moodDrawableInt[moodTypeInt-1];
    }

    public static Integer getMoodDrawableMapInt (int moodTypeInt) {
        return moodDrawableMapInt[moodTypeInt-1];
    }

    public static Integer getMoodColorResInt (int moodTypeInt) {
        return moodColorResInt[moodTypeInt-1];
    }

}
