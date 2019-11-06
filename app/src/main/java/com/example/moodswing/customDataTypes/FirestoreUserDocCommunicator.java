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
import com.google.firebase.firestore.auth.User;

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

    private ArrayList<MoodEvent> moodEvents;
    private ArrayList<UserJar> userJars;
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

    // mailBox feature
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
                                // do something
                            }else{
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


        UserJar userJar = new UserJar();
        userJar.setUsername(getUsername());
        userJar.setUID(user.getUid());

        requestRef.set(userJar)
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

    // mailBox feature
    public void removeRequest (UserJar userJar) {
        // error code need to be created
        DocumentReference requestRef = db
                .collection("users")
                .document(user.getUid())
                .collection("mailBox")
                .document(userJar.getUID());

        requestRef
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "request delete successful");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "request delete fail");
                    }
                });
    }

    public void acceptRequest(UserJar userJar) {
        // responding to sender's request
        // two action to do here,
        // 1. adding the uid&username (current entry) to user's permitted list
        // 2. pack uid and RecentMood, send recentMood to sender
        //    a. send uid/username to sender's following list  b. trigger one recentMood update

        removeRequest(userJar);
        DocumentReference myPermittedListRef = db
                .collection("users")
                .document(user.getUid())
                .collection("permittedList")
                .document(userJar.getUID());
        myPermittedListRef
                .set(userJar)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "accept Step 1 Success");
                        addToSendersFollowing(userJar);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "accept Fail");
                    }
                });
    }
    private void addToSendersFollowing(UserJar sendersUserJar) {
        // at this point UID should be always correct, since it is checked in sendingRequest method
        String sendersUID = sendersUserJar.getUID();
        UserJar myUserJar = new UserJar();
        myUserJar.setUID(user.getUid());
        myUserJar.setUsername(user.getUid());

        DocumentReference followingListReference = db
                .collection("users")
                .document(sendersUID)
                .collection("following")
                .document(user.getUid());

        followingListReference.set(myUserJar)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "following upload successful");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "following upload fail");
                    }
                });
    }
    private void updateRecentMoodToFollowers(){

        CollectionReference permittedList = db
                .collection("users")
                .document(user.getUid())
                .collection("permittedList");

        permittedList.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            if (task.getResult().isEmpty()){
                                Log.d(TAG, "no followers");
                            }else{
                                for (DocumentSnapshot userJarDoc : task.getResult().getDocuments()){
                                    UserJar userJar = userJarDoc.toObject(UserJar.class);
                                    pushRecentMoodEventToUser(userJar.getUID());
                                }
                            }
                        }else{
                            Log.d(TAG, "failed finding permittedList");
                        }
                    }
                });
    }

    private void pushRecentMoodEventToUser(String uid){
        // grab recentMoodEvent
        MoodEvent mostRecentMoodEvent = moodEvents.get(0);

        // construct UserJar
        UserJar myUserJarWithMood = new UserJar();
        myUserJarWithMood.setUsername(getUsername());
        myUserJarWithMood.setUID(user.getUid());
        myUserJarWithMood.setMoodEvent(mostRecentMoodEvent);

        // send it to target
        CollectionReference followingMoodListCol = db
                .collection("users")
                .document(uid)
                .collection("followingMoodList");

        followingMoodListCol.add(myUserJarWithMood)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "sending mood to target uid, done");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "sending mood to target uid, failed");
                    }
                });
    }

    public void initFollowingList(final RecyclerView userJarList){
        @NonNull
        UserJarAdaptor userJarAdaptor = (UserJarAdaptor) userJarList.getAdapter();

        Query followingMoodListColQuery = db
                .collection("users")
                .document(user.getUid())
                .collection("followingMoodList")
                .orderBy("timeStamp", Query.Direction.DESCENDING);

        followingMoodListColQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                userJarAdaptor.clearUserJars();
                for (QueryDocumentSnapshot userJarDoc : queryDocumentSnapshots){
                    UserJar userJar = userJarDoc.toObject(UserJar.class);
                    userJarAdaptor.addToUserJars(userJar);
                }
                userJarAdaptor.notifyDataSetChanged();
                userJars = userJarAdaptor.getUserJars();
            }
        });
    }

    public void initManagementFollowingList(final RecyclerView userJarList){
        @NonNull
        SimpleUserJarAdapter userJarAdaptor = (SimpleUserJarAdapter) userJarList.getAdapter();

        Query followingMoodListColQuery = db
                .collection("users")
                .document(user.getUid())
                .collection("following");

        followingMoodListColQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                userJarAdaptor.clearUserJars();
                for (QueryDocumentSnapshot userJarDoc : queryDocumentSnapshots){
                    UserJar userJar = userJarDoc.toObject(UserJar.class);
                    userJarAdaptor.addToUserJars(userJar);
                }
                userJarAdaptor.notifyDataSetChanged();
            }
        });
    }

    public void initManagementRequestList(final RecyclerView userJarList){
        @NonNull
        SimpleUserJarAdapter userJarAdaptor = (SimpleUserJarAdapter) userJarList.getAdapter();

        Query followingMoodListColQuery = db
                .collection("users")
                .document(user.getUid())
                .collection("mailBox");

        followingMoodListColQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                userJarAdaptor.clearUserJars();
                for (QueryDocumentSnapshot userJarDoc : queryDocumentSnapshots){
                    UserJar userJar = userJarDoc.toObject(UserJar.class);
                    userJarAdaptor.addToUserJars(userJar);
                }
                userJarAdaptor.notifyDataSetChanged();
            }
        });
    }



    public ArrayList<MoodEvent> getMoodEvents() {
        return moodEvents;
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
