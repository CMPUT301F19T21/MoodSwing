package com.example.moodswing;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * This class is the blueprint to an object that serves as the
 * middle layer (or interface) between firestore user documents and local java objects
 * An object of this class will translate the date structure between local java object and
 * cloud firestore documents
 */
public class FirestoreUserDocCommunicator {
    private FirebaseFirestore db;
    private DocumentReference userDocRef;
    private CollectionReference user

    public FirestoreUserDocCommunicator(String username){
        // init db
        db = FirebaseFirestore.getInstance();
        userDocRef = db.collection("Accounts").document(username); // will always exist so no error checking
    }

    // methods

    public void addMoodEvent(MoodEvent moodEvent) {
        // lacking error returning code here





    }
}
