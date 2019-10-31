package com.example.moodswing.customDataTypes;


import androidx.annotation.NonNull;

import java.io.Serializable;

/**
 * This Class is the blueprint for a moodEvent object
 */
public class MoodEvent {
    private String uniqueID; // this value is used to identify moodEvent
    // required fields
    private int moodType; // for this design im assuming each moodType has been assigned an unique int
    private DateJar date;
    private TimeJar time;

    // optional fields
    private String reason;

    public MoodEvent() {
    }

    public MoodEvent(@NonNull String uniqueID, @NonNull Integer moodType, @NonNull DateJar date, @NonNull TimeJar time) {
        this.uniqueID = uniqueID;
        this.moodType = moodType;
        this.date = date;
        this.time = time;

        // init optional field to null
        this.reason = null;
    }

    public String getUniqueID() {
        return uniqueID;
    }

    public void setUniqueID(String uniqueID) {
        this.uniqueID = uniqueID;
    }

    public int getMoodType() {
        return moodType;
    }

    public void setMoodType(int moodType) {
        this.moodType = moodType;
    }

    public DateJar getDate() {
        return date;
    }

    public void setDate(DateJar date) {
        this.date = date;
    }

    public TimeJar getTime() {
        return time;
    }

    public void setTime(TimeJar time) {
        this.time = time;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}