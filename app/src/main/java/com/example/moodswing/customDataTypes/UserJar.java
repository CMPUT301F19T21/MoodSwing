package com.example.moodswing.customDataTypes;

public class UserJar {
    private MoodEvent moodEvent;
    private String username;
    private String UID;

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
