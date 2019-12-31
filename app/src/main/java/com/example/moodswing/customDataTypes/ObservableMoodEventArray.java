package com.example.moodswing.customDataTypes;

import java.util.ArrayList;
import java.util.Collections;

public class ObservableMoodEventArray {
    private ArrayList<ObservableMoodEventArrayClient> clients;
    private ArrayList<MoodEvent> moodEvents;

    public interface ObservableMoodEventArrayClient {
        void MoodEventArrayChanged();
    }

    public ObservableMoodEventArray(){
        clients = new ArrayList<>();
        moodEvents = new ArrayList<>();
    }

    public ArrayList<MoodEvent> getMoodEvents(){
        return moodEvents;
    }

    public void sortList() {
        Collections.sort(moodEvents);
    }

    public void add(MoodEvent moodEvent) {
        moodEvents.add(moodEvent);
        sortList();
    }

    public void remove(MoodEvent moodEvent) {
        moodEvents.remove(moodEvent);
        sortList();
    }

    public boolean isEmpty() {
        return moodEvents.isEmpty();
    }

    public void clear() {
        moodEvents.clear();
    }

    public void addClient(ObservableMoodEventArrayClient client){
        this.clients.add(client);
    }

    public void removeClient(ObservableMoodEventArrayClient client) {
        this.clients.remove(client);
    }

    public boolean containClient(ObservableMoodEventArrayClient client) {
        return this.clients.contains(client);
    }

    public void notifyChange(){
        for (ObservableMoodEventArrayClient client : clients) {
            client.MoodEventArrayChanged();
        }
    }
}
