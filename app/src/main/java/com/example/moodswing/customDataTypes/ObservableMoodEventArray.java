package com.example.moodswing.customDataTypes;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ObservableMoodEventArray extends ArrayList<MoodEvent> {
    private ArrayList<ObservableMoodEventArrayClient> clients;

    public interface ObservableMoodEventArrayClient {
        void MoodEventArrayChanged();
    }

    public ObservableMoodEventArray(){
        clients = new ArrayList<>();
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
