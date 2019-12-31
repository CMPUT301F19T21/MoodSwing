package com.example.moodswing.customDataTypes;


import java.util.ArrayList;
import java.util.Collections;

public class ObservableUserJarArray {
    private ArrayList<ObservableUserJarArrayClient> clients;
    private ArrayList<UserJar> userJars;

    public interface ObservableUserJarArrayClient {
        void userJarArrayChanged();
    }

    public ObservableUserJarArray(){
        clients = new ArrayList<>();
        userJars = new ArrayList<>();
    }

    public ArrayList<UserJar> getUserJars(){
        return userJars;
    }

    public void sortList() {
        Collections.sort(userJars);
    }

    public void add(UserJar userJar) {
        userJars.add(userJar);
        sortList();
    }

    public void remove(UserJar userJar) {
        userJars.remove(userJar);
        sortList();
    }

    public boolean isEmpty() {
        return userJars.isEmpty();
    }

    public void clear() {
        userJars.clear();
    }

    public void addClient(ObservableUserJarArrayClient client){
        this.clients.add(client);
    }

    public void removeClient(ObservableUserJarArrayClient client) {
        this.clients.remove(client);
    }

    public boolean containClient(ObservableUserJarArrayClient client) {
        return this.clients.contains(client);
    }

    public void notifyChange(){
        for (ObservableUserJarArrayClient client : clients) {
            client.userJarArrayChanged();
        }
    }
}
