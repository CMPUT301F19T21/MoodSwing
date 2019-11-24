package com.example.moodswing.customDataTypes;
/**
 * This class creates a User object which can be used for followers
 * Each user has a moodevent, their username, and their generated UID as fields
 *
 */
public class UserJar {
    private MoodEvent moodEvent;
    private String username;
    private String UID;

    /**
     * empty constructor, fields will be given values using setter methods
     */
    public UserJar(){}

    public MoodEvent getMoodEvent() {
        return moodEvent;
    }

    public void setMoodEvent(MoodEvent moodEvent) {
        this.moodEvent = moodEvent;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }
}
