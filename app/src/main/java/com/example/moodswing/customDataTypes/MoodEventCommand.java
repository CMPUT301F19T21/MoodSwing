package com.example.moodswing.customDataTypes;

import java.util.ArrayList;

/**
 * used to enable multiple device sync (since communicator is now local)
 */
public class MoodEventCommand {
    private MoodEvent moodEvent;    // moodEvent
    private String appInstanceID;   // used to identify a running app
    private Integer actionID;       // used to identify command action

    public MoodEventCommand(MoodEvent moodEvent, String appID, Integer actionType){
        this.setActionID(actionType);
        this.setAppInstanceID(appID);
        this.setMoodEvent(moodEvent);
    }

    public void execute(ArrayList<MoodEvent> moodEvents){
        switch (this.actionID) {
            case MoodEventUtility.COMMAND_ACTION_ADD:
                this.addMoodEvent(moodEvents);
                break;
            case MoodEventUtility.COMMAND_ACTION_DELETE:
                this.deleteMoodEvent(moodEvents);
                break;
            case MoodEventUtility.COMMAND_ACTION_UPDATE:
                this.deleteMoodEvent(moodEvents);
                this.addMoodEvent(moodEvents);
                break;
        }
    }

    private void deleteMoodEvent(ArrayList<MoodEvent> moodEvents){
        String ToBeDeleteID = this.getMoodEvent().getUniqueID();
        for (MoodEvent moodEvent : moodEvents){
            if (moodEvent.getUniqueID() == ToBeDeleteID){
                moodEvents.remove(moodEvent);
                break;
            }
        }
    }

    private void addMoodEvent(ArrayList<MoodEvent> moodEvents){
        moodEvents.add(this.moodEvent);
    }

    // setters and getters for firestore

    public MoodEvent getMoodEvent() {
        return moodEvent;
    }

    public void setMoodEvent(MoodEvent moodEvent) {
        this.moodEvent = moodEvent;
    }

    public String getAppInstanceID() {
        return appInstanceID;
    }

    public void setAppInstanceID(String appInstanceID) {
        this.appInstanceID = appInstanceID;
    }

    public Integer getActionID() {
        return actionID;
    }

    public void setActionID(Integer actionID) {
        this.actionID = actionID;
    }
}
