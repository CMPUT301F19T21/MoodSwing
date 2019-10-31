package com.example.moodswing;

import android.util.Log;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.internal.FallbackServiceBroker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ------------ PLEASE READ --------------
 *  missing check ifLogin.
 *
 */
public class FirestoreUserDocCommunicator{

    private static final String TAG = "FirestoreUserDocCommuni";
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    private static FirestoreUserDocCommunicator instance = null;

    // reference

    protected FirestoreUserDocCommunicator(){
        // init db
        this.db = FirebaseFirestore.getInstance();
        this.mAuth = FirebaseAuth.getInstance();
        this.user = mAuth.getCurrentUser();

    }
    private boolean ifLogin(){
        if (user == null) {
            return false;
        }else{
            return true;
        }
    }

    private void userSignOut(){
        mAuth.signOut();
        user = null;
    }

    public static FirestoreUserDocCommunicator getInstance() {
        if (instance == null) {
            instance = new FirestoreUserDocCommunicator();
        }
        return instance;
    }

    public static void destroy(){
        instance.userSignOut();
        instance = null;
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

        DocumentReference newMoodEventRef = db
                .collection("users")
                .document(user.getUid())
                .collection("MoodEvents")
                .document();
        String refID = newMoodEventRef.getId();

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
        newMoodEventRef
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

        return refID;
    }

    public void removeMoodEvent(String moodEventID){
        // error code need to be created
        DocumentReference moodEvent = db
                .collection("users")
                .document(user.getUid())
                .collection("MoodEvents")
                .document(moodEventID);

        moodEvent.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "moodEvent delete successful");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "moodEvent delete fail");
            }
        });

    }

    public void initMoodEventsList(final RecyclerView moodList){

        CollectionReference moodEventCol = db
                .collection("users")
                .document(user.getUid())
                .collection("MoodEvents");

        moodEventCol.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                ((MoodAdapter)moodList.getAdapter()).clearMoodEvents();
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

                    ((MoodAdapter)moodList.getAdapter()).addToMoods(moodEvent);
                }
                moodList.getAdapter().notifyDataSetChanged();
        }
        });
    }

    /* user management related methods */


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
