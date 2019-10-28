package com.example.moodswing;


import androidx.annotation.NonNull;

import java.io.Serializable;

/**
 * This Class is the blueprint for a moodEvent object
 */
public class MoodEvent implements Serializable {
    private String uniqueID; // this value is used to identify moodEvent
    // required fields
    private int moodType; // for this design im assuming each moodType has been assigned an unique int
    private DateJar date;
    private TimeJar time;

    // optional fields
    private String reason;
    private Integer socialSituation; // assigned an unique int
    // ignore other fields for now
    // photograph
    // location

    public void setMoodType(int moodType) {
        this.moodType = moodType;
    }

    public void setDate(DateJar date) {
        this.date = date;
    }

    public void setTime(TimeJar time) {
        this.time = time;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setSocialSituation(Integer socialSituation) {
        this.socialSituation = socialSituation;
    }

    public MoodEvent(int moodType, @NonNull DateJar date, @NonNull TimeJar time){
        setMoodRequired(moodType, date, time);

        // init optional field to null
        this.reason = null;
        this.socialSituation = null;
    }

    public int getMoodType() {
        return moodType;
    }

    public DateJar getDate() {
        return date;
    }

    public TimeJar getTime() {
        return time;
    }

    public String getReason() {
        return reason;
    }

    public Integer getSocialSituation() {
        return socialSituation;
    }

    public String getUniqueID() {
        return uniqueID;
    }

    public void setMoodRequired(int moodType, @NonNull DateJar date, @NonNull TimeJar time) {
        this.moodType = moodType;
        this.date = date;
        this.time = time;
    }

    /**
     * set optional field. any object in the parameter can points to null.
     * will be manually checked when upload to cloud
     * @param reason
     * @param socialSituation
     */
    public void setMoodOptional(String reason, Integer socialSituation){
        this.reason = reason;
        this.socialSituation = socialSituation;

    }


    public void setUniqueID(String id){
        this.uniqueID = id;
    }
}
