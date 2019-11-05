package com.example.moodswing.customDataTypes;

import java.util.Locale;

public final class MoodEventUtility {

    private MoodEventUtility(){
        // nothing
    }
    public static String getDateStr (DateJar date) {
        String month = returnMonthStr(date.getMonth());
        return String.format(Locale.getDefault(), "%s %d, %d",month,date.getDay(),date.getYear());
    }

    public static String getTimeStr (TimeJar time) {
        int hr = time.getHr();
        String period;
        if (hr>12){
            hr =hr-12;
            period = "PM";
        }
        else period = "AM";
        return String.format(Locale.getDefault(), "%d:%02d %s",hr,time.getMin(),period);
    }

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

    private static String returnMonthStr(int monthInt){
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
