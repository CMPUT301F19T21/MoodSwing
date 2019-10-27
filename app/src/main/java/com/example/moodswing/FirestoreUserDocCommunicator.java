package com.example.moodswing;

import android.util.Log;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ------------ PLEASE READ --------------:
 * (dont worry these are just notes, it is here just in case the usage of this class confuses you)
 * (these notes will be deleted in final product)
 *
 *
 *      - this class manages all the transaction between app and firestore, it makes managing cloud user data easier
 *          for example: edit user profile info, edit mood, add a moodEvent to firestore
 *
 *      - you can create as many instance as you want (in same/different activities) :D
 *
 *      - it is important to understand the structure of how our app store data on firestore before modifying/adding methods to this class
 *          basically, we have a root level collection called "Accounts"
 *              In "Accounts", we have documents of users(each document in Accounts represent one user profile) and Document ID is the username
 *                  Each user document has one sub collection of "MoodEvents"
 *                      In "MoodEvents", we have documents of one single moodEvent, each document in MoodEvents represent one moodEvent object
 *
 *                      Structure:
 *                      Collection - "Accounts"
 *                              Document - "Users"
 *                                      SubCollection - "MoodEvents"
 *                                              Document - "MoodEvenets"
 *
 *      - listview's real time update is also maintained in a communicator objects
 *      - when one user logged in, a communicator object is created.
 *
 * ------------- not so important ideas for this class --------------:
 *      - following/follower management features should be added
 *      - user management feature should be added
 *      - but dont make this class too big.
 *
 *      - can be used between fragments/activities, ill try to make this class implements Serializable, So it will be very easy to pass this object
 *          by using Intent/Bundle!!! :D
 *      - please add more/implement new stuff!
 */
public class FirestoreUserDocCommunicator {
    private static final String TAG = "FirestoreUserDocCommuni";
    private FirebaseFirestore db;
    private DocumentReference userDocRef;
    private CollectionReference moodEventsCollection;
    private String userID;

    public FirestoreUserDocCommunicator(String username){
        // init db
        db = FirebaseFirestore.getInstance();
        userDocRef = db.collection("Accounts").document(username); // will always exist so no error checking
        moodEventsCollection = userDocRef.collection("MoodEvents");

        this.userID = username;
    }

    public String getUsername(){
        return this.userID;
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
        // required fields. no handling here, moodEvent class should handle it

        DocumentReference newMoodRef = moodEventsCollection.document();
        String refID = newMoodRef.getId();

        // time info
        TimeJar time = moodEvent.getTime();
        Map<String, Object> timeInConstruct = new HashMap<>();
        {
            timeInConstruct.put("hr", time.getHr());
            timeInConstruct.put("min", time.getMin());
        }

        // date info
        DateJar date = moodEvent.getDate();
        Map<String, Object> dateInConstruct = new HashMap<>();
        {
            dateInConstruct.put("year", date.getYear());
            dateInConstruct.put("month", date.getMonth());
            dateInConstruct.put("day", date.getDay());
        }

        // moodEvent info
        Map<String, Object> moodEventInConstruct = new HashMap<>();
        // required fields
        {
            moodEventInConstruct.put("time", timeInConstruct);
            moodEventInConstruct.put("date", dateInConstruct);
            moodEventInConstruct.put("moodType", (Integer) moodEvent.getMoodType());
        }
        // optional fields
        {
            moodEventInConstruct.put("reason", moodEvent.getReason());
            moodEventInConstruct.put("socialSituation", moodEvent.getSocialSituation());
            moodEventInConstruct.put("refID", refID);
        }

        // putMoodEvent in place! yeeeaaaaaaa!
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

    public void showListView(final ListView listView){
        moodEventsCollection.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                ((MoodAdapter)listView.getAdapter()).clearMoodEvents();
                for (QueryDocumentSnapshot event : queryDocumentSnapshots) {
                    Integer year = ((Long)((Map<String, Object>) event.get("date")).get("year")).intValue();
                    Integer month = ((Long)((Map<String, Object>) event.get("date")).get("month")).intValue();
                    Integer day = ((Long)((Map<String, Object>) event.get("date")).get("day")).intValue();

                    Integer hr = ((Long)((Map<String, Object>) event.get("time")).get("hr")).intValue();
                    Integer min = ((Long)((Map<String, Object>) event.get("time")).get("min")).intValue();

                    Integer moodtype = ((Long) (event.get("moodType"))).intValue();
                    String refID = (String) event.get("refID");

                    MoodEvent moodEvent = new MoodEvent(moodtype, new DateJar(year,month,day), new TimeJar(hr,min));
                    moodEvent.setUniqueID(refID);

                    ((MoodAdapter)listView.getAdapter()).addToMoods(moodEvent);
                }
                ((MoodAdapter) listView.getAdapter()).notifyDataSetChanged();
            }
        });
    }

    /* user management related methods */

    /*public MoodEvent grabMood(String moodEventID){
        MoodEvent mood;
        return mood;
    }*/

    public void editMood(MoodEvent moodEvent){
        //
    }

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
