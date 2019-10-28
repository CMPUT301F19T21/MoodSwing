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
public class FirestoreUserDocCommunicator{

    private static final String TAG = "FirestoreUserDocCommuni";
    private FirebaseFirestore db;
    private String userID;

    // reference
    private static class FireStoreUserDocCommunicatorHolder {
        private static final FirestoreUserDocCommunicator instance = new FirestoreUserDocCommunicator();
    }

    private class LoginCallBackChecker {
        private boolean isDone = false;
        private int loginResultCode;
        private void done(int loginResultCode){
            this.isDone = true;
            this.loginResultCode = loginResultCode;
        }
    }

    private FirestoreUserDocCommunicator(){
        // init db
        this.db = FirebaseFirestore.getInstance();
        this.userID = null;

    }
    private boolean ifLogin(){
        if (userID == null) {
            return false;
        }else{
            return true;
        }
    }

    /**
     * return 0 if login successful
     * return 1 if wrong password
     * return -1 if user not exist
     * @param username
     * @param password
     * @return
     */
    public int userLogin(String username, String password) {
        LoginCallBackChecker callBackChecker = new LoginCallBackChecker();
        db.collection("Accounts").document("username")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot userDoc = task.getResult();
                            if (userDoc.exists()){

                                if (userDoc.get("password").equals(password)){
                                    // login successful
                                    callBackChecker.done(0);
                                }else{
                                    // wrong password
                                    callBackChecker.done(1);
                                }
                            }else{
                                // user not exist
                                callBackChecker.done(-1);
                            }
                        }else{
                            Log.d(TAG, "login fail, query not successful"); // for debugging
                            callBackChecker.done(-2);
                        }
                    }
                });
        // await loop
        while (!(callBackChecker.isDone)){}
        if (callBackChecker.loginResultCode == 0){
            userID = username;
        }
        return callBackChecker.loginResultCode;
    }

    public static FirestoreUserDocCommunicator getInstance() {
        return FireStoreUserDocCommunicatorHolder.instance;
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
                .collection("Accounts")
                .document(userID)
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
                .collection("Accounts")
                .document(userID)
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
                .collection("Accounts")
                .document(userID)
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
