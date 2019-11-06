package com.example.moodswing.customDataTypes;

import android.content.Context;
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
import com.google.firebase.firestore.Query;
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
    private DocumentSnapshot userDocSnapshot;

    private static FirestoreUserDocCommunicator instance = null;

    private static ArrayList<MoodEvent> moodEvents;
    // reference

    protected FirestoreUserDocCommunicator(){
        // init db
        this.db = FirebaseFirestore.getInstance();
        this.mAuth = FirebaseAuth.getInstance();
        this.user = mAuth.getCurrentUser();
        this.userDocSnapshot = null;
        this.getUserSnapShot();
    }

    private boolean ifLogin(){
        if (user == null) {
            return false;
        }else{
            return true;
        }
    }

    private void getUserSnapShot(){
        // throw exception here if not login
        db.collection("users")
                .document(user.getUid())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    userDocSnapshot = task.getResult();
                    Log.d(TAG, "get userDocSnap success");
                }else{
                    Log.d(TAG, "get userDocSnap failed with", task.getException());
                }
            }
        });
    }

    private void userSignOut(){
        mAuth.signOut();
        user = null;
    }

    public String getUsername(){
        if (userDocSnapshot != null) {
            return (String) userDocSnapshot.get("username");
        }else{
            return null; // something wrong, possible not enough time to finish query
        }
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


    public String generateMoodID(){
        String refID = db
                .collection("users")
                .document(user.getUid())
                .collection("MoodEvents")
                .document()
                .getId();

        return refID;
    }


    public void addMoodEvent(MoodEvent moodEvent) {
        // lacking error returning code here
        //
        // required fields. no handling here, moodEvent class should handle it

        DocumentReference newMoodEventRef = db
                .collection("users")
                .document(user.getUid())
                .collection("MoodEvents")
                .document(moodEvent.getUniqueID());

        newMoodEventRef.set(moodEvent)
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
    }

    public void removeMoodEvent(MoodEvent moodEvent){
        // error code need to be created
        DocumentReference moodEventRef = db
                .collection("users")
                .document(user.getUid())
                .collection("MoodEvents")
                .document(moodEvent.getUniqueID());

        moodEventRef
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
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
        @NonNull
        MoodAdapter moodAdapter = (MoodAdapter) moodList.getAdapter();

        Query moodEventColQuery = db
                .collection("users")
                .document(user.getUid())
                .collection("MoodEvents")
                .orderBy("timeStamp", Query.Direction.DESCENDING);

        moodEventColQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                moodAdapter.clearMoodEvents();
                for (QueryDocumentSnapshot moodEventDoc : queryDocumentSnapshots){
                    MoodEvent moodEvent = moodEventDoc.toObject(MoodEvent.class);
                    moodAdapter.addToMoods(moodEvent);
                }
                moodAdapter.notifyDataSetChanged();
                moodEvents = moodAdapter.getMoods();
            }
        });
    }

    /* user management related methods */
    public void updateMoodEvent(MoodEvent moodEvent){
        DocumentReference moodEventRef = db
                .collection("users")
                .document(user.getUid())
                .collection("MoodEvents")
                .document(moodEvent.getUniqueID());
        moodEventRef.set(moodEvent)
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
    }

    public MoodEvent getMoodEvent(int position) {
        return moodEvents.get(position);
    }

    // following feature

    public void isUsernameUnique() {
        // this is critical for following feature, will implement later
        // need a workaround, since no callback
    }


    public void sendFollowingRequest (String username) {
        // should first check if uid exist
        Query findUserColQuery = db
                .collection("users")
                .whereEqualTo("username",username)
                .limit(1);

        findUserColQuery
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().isEmpty()){
                                unlockRequestButton();
                                // do something
                            }else{
                                unlockRequestButton();
                                // not empty, proceed
                                // should be only one
                                DocumentSnapshot doc = task.getResult().toObjects(DocumentSnapshot.class).get(0);
                                String UID = doc.getId();
                                addRequestToMailBox(UID);
                            }
                        }else{
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void unlockRequestButton(){
        // this method is empty for now, it will be used in sendRequestMethod to implement a lock
        // idea: system lock UI, then, system will wait to check if username exist, if it exist, it will unlock the UI.

    }

    private void addRequestToMailBox(String targetUID){
        // given an UID, add request to his mailBox
        DocumentReference requestRef = db
                .collection("users")
                .document(targetUID) // enter other user's doc
                .collection("mailBox")
                .document(user.getUid()); // doc name is your UID

        Map<String, String> usernameEntry = new HashMap<>();
        usernameEntry.put("username",getUsername());

        requestRef.set(usernameEntry)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "sending request successful");
                        // maybe do something
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "sending request failed");
                    }
                });
    }

    // 


    public void addFollowing(String uid) {
        // at this point UID should be always correct, since it is checked in sendingRequest method

        DocumentReference followingListReference = db
                .collection("users")
                .document(user.getUid())
                .collection("MoodEvents")
                .document(uid);

        followingListReference.set(uid)
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
