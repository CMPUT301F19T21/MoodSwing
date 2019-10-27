package com.example.moodswing;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is the blueprint to an object that serves as the
 * middle layer (or interface) between firestore user documents and local java objects
 * An object of this class will translate the date structure between local java object and
 * cloud firestore documents
 */
public class FirestoreUserDocCommunicator {
    private static final String TAG = "FirestoreUserDocCommuni";
    private FirebaseFirestore db;
    private DocumentReference userDocRef;
    private CollectionReference moodEventsCollection;

    public FirestoreUserDocCommunicator(String username){
        // init db
        db = FirebaseFirestore.getInstance();
        userDocRef = db.collection("Accounts").document(username); // will always exist so no error checking
        moodEventsCollection = userDocRef.collection("MoodEvents");
    }

    /* moodEvent related methods*/

    /**
     * This method will upload given moodEvent to the db and return an unique hash key
     * @param moodEvent
     * @return String - the id
     */
    public String addMoodEvent(MoodEvent moodEvent) {
        // lacking error returning code here
        //
        // required fields. no handling here, moodEvent class should handle it.

        DocumentReference newMoodRef = moodEventsCollection.document();
        String refID = newMoodRef.getId();
        TimeJar time = moodEvent.getTime();
        Map<String, Integer> timeInConstruct = new HashMap<>();
        timeInConstruct.put("hr",time.getHr());
        timeInConstruct.put("min",time.getMin());

        DateJar date = moodEvent.getDate();
        Map<String, Integer> dateInConstruct = new HashMap<>();
        dateInConstruct.put("year",date.getYear());
        dateInConstruct.put("month",date.getMonth());
        dateInConstruct.put("day",date.getDay());

        Map<String, Object> moodEventInConstruct = new HashMap<>();
        // required fields
        moodEventInConstruct.put("time",timeInConstruct);
        moodEventInConstruct.put("date",dateInConstruct);
        moodEventInConstruct.put("moodType",moodEvent.getMoodType());

        // optional fields
        moodEventInConstruct.put("reason",moodEvent.getReason());
        moodEventInConstruct.put("socialSituation",moodEvent.getSocialSituation());
        moodEventInConstruct.put("refID",refID);


        // upload
        newMoodRef
                .set(moodEventInConstruct)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "moodEvent upload successful");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "moodEvent upload fail");
                    }
                });

        return newMoodRef.getId();
    }

    public void removeMoodEvent(String moodEventID){
        // error code need to be created
        DocumentReference moodEvent = moodEventsCollection.document(moodEventID);

        moodEvent.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "Delete successful");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Delete fail");
            }
        });

    }

    /* user management related methods */

    public void editUserPassword() {
        //
    }

    public void editUsername() {
        //
    }

    public void deleteUser() {
        //
    }
}
