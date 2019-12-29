package com.example.moodswing.customDataTypes;

import java.util.ArrayList;

public class ObservableMoodEventArray extends ArrayList<MoodEvent> {
    private ObservableMoodEventArrayClient currentClient;   // can be changed to an array later

    public interface ObservableMoodEventArrayClient {
        void MoodEventArrayChanged();
    }

    public ObservableMoodEventArray(){
        currentClient = null;
    }

    public void setCurrentClient(ObservableMoodEventArrayClient client){
        this.currentClient = client;
    }

    public void notifyChange(){
        if (currentClient != null) {
            currentClient.MoodEventArrayChanged();
        }
    }
}
