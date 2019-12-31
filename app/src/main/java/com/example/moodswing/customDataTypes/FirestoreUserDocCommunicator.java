package com.example.moodswing.customDataTypes;

import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * This class Handles all the functionality related to firestore, a go-between for the app and firestore
 *
 */
public class FirestoreUserDocCommunicator{
    // TAG
    private static final String TAG = "FirestoreUserDocCommuni";

    // singleton instance
    private static FirestoreUserDocCommunicator instance = null;

    // FireStore related
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private DocumentSnapshot userDocSnapshot;
    private FirebaseStorage storage;

    // Communicator Managers (module/helper)
    private CommunicatorFollowingManager followingManager;
    private CommunicatorMoodListManager moodListManager;

    // for filter
    private ArrayList<Integer> moodTypeFilterList_moodHistory;
    private ArrayList<Integer> moodTypeFilterList_following;

    // for caching system
    private RecentImagesBox recentImagesBox;

    // properties
    private String communicatorID;

    // constructor
    protected FirestoreUserDocCommunicator(){
        // firestore related
        this.db = FirebaseFirestore.getInstance();
        this.mAuth = FirebaseAuth.getInstance();
        this.storage = FirebaseStorage.getInstance();
        this.userDocSnapshot = null;

        // communicator managers
        this.followingManager = new CommunicatorFollowingManager(this);
        this.moodListManager = new CommunicatorMoodListManager(this);

        // init filter
        this.moodTypeFilterList_moodHistory = new ArrayList<>();
        this.moodTypeFilterList_following = new ArrayList<>();

        // for caching system
        recentImagesBox = new RecentImagesBox();

        // init
        this.generateCommunicatorID();

        // listens
        this.initUserDocListener();
        this.announceChangeToMoodList();
    }

    // init singleton
    public static FirestoreUserDocCommunicator getInstance() {
        if (instance == null) {
            instance = new FirestoreUserDocCommunicator();
        }
        return instance;
    }

    // destroy singleton instance when called
    public static void destroy(){
        instance.mAuth.signOut();
        instance = null;
    }

    /* ** User Management Related ** */

    public FirebaseUser getUser(){
        return this.mAuth.getCurrentUser();
    }

    public String getUserUID(){
        return getUser().getUid();
    }

    public String getUsername(){
        if (userDocSnapshot != null) {
            return (String) userDocSnapshot.get("username");
        }else{
            return null;
            // something wrong, possible not enough time to finish query
        }
    }

    public FirebaseFirestore getDB(){
        return this.db;
    }

    public ObservableUserJarArray getUserJarArrayObs(){
        return followingManager.getUserJarArray();
    }

    public ObservableMoodEventArray getMoodEventArrayObs(){
        return moodListManager.getMoodEvents();
    }

    public ArrayList<UserJar> getUserJars(){
        return this.getUserJarArrayObs().getUserJars();
    }

    public ArrayList<MoodEvent> getMoodEvents() {
        return this.getMoodEventArrayObs().getMoodEvents();
    }

    public ArrayList<Integer> getMoodHistoryFilterList(){
        return this.moodTypeFilterList_moodHistory;
    }

    public ArrayList<Integer> getFollowingFilterList(){
        return this.moodTypeFilterList_following;
    }

    /**
     * set up a realtime listener on user's document
     * this includes current user's username and dominant communicator ID (used for multi device sync)
     * this listener will be triggered by two conditions:
     *      1. there is a change to user doc (e.g. change of username or other info)
     *      2. two or more device is sharing one account and one device triggered an mood list update
     * if condition number 2, then this listener will trigger an update to local mood list array by sync all mood events from firestore
     * this is obvious not ideal, but settled for now
     */
    private void initUserDocListener(){
        // throw exception here if not login
        db.collection("users")
                .document(getUserUID())
                .addSnapshotListener((documentSnapshot, e) -> {
                    userDocSnapshot = documentSnapshot;
                    if (documentSnapshot.get("mostRecentAppID") != communicatorID) {
                        moodListManager.getMoodEventListInstance();
                    }
                });
    }

    private void generateCommunicatorID(){
        this.communicatorID = this.generateUniqueID();
    }

    public void announceChangeToMoodList(){
        this.generateCommunicatorID();
        db.collection("users")
                .document(getUserUID())
                .update("mostRecentAppID", communicatorID);
    }

    /* moodEvent related methods*/

    /**
     * Generates the users Mood ID
     * @return Returns the ID as a string
     */
    public String generateUniqueID(){
        return getDB()
                .collection("users")
                .document()
                .getId();
    }

    /* ** MoodEvent List Related Methods ** */

    /**
     * Adds a mood event to the user's list of moods in firestore
     * @param moodEvent the moodEvent to be added
     */
    public void addMoodEvent(MoodEvent moodEvent) {
        moodListManager.addMoodEvent(moodEvent);
    }

    /**
     * Deletes a mood event from in the users list and their followers in the firestore database
     * @param moodEvent The mood event to be deleted
     */
    public void removeMoodEvent(MoodEvent moodEvent){
        moodListManager.removeMoodEvent(moodEvent);
    }

    /**
     * Updates/edits an existing moodEvent
     * @param moodEvent The moodEvent to edit
     */
    public void updateMoodEvent(MoodEvent moodEvent){
        moodListManager.updateMoodEvent(moodEvent);
    }

    public MoodEvent getMoodEvent(int position) {
        return this.getMoodEventArrayObs().getMoodEvents().get(position);
    }

    public Integer getMoodPosition(String moodID){
        int position = 0;
        for (MoodEvent moodEvent : getMoodEvents()) {
            if(moodEvent.getUniqueID() == moodID) {
                return position;
            }
            position ++;
        }
        return null;
    }

    public UserJar getUserJar(int position) {
        return this.getUserJarArrayObs().getUserJars().get(position);
    }

    public Integer getUserJarPosition(String moodID){
        int position = 0;
        for (UserJar userJar : getUserJars()) {
            if(userJar.getMoodEvent().getUniqueID() == moodID) {
                return position;
            }
            position ++;
        }
        return null;    // not found
    }

    // following feature

    /**
     * Sends a following request to another user
     * @param username The username of the user that is going to receive the request
     */
    public void sendFollowingRequest (String username) {
        followingManager.sendFollowingRequest(username);
    }

    /**
     * Deletes a request from the user's mailbox collection
     * @param userJar The userJar of the user to be deleted
     */
    public void removeRequest (UserJar userJar) {
        followingManager.removeRequest(userJar);
    }

    /**
     * Accepts an incoming follow request
     * @param userJar The userJar of the user sending the request
     */
    public void acceptRequest(UserJar userJar) {
        followingManager.acceptRequest(userJar);
    }

    /**
     * This method refreshes the recent mood(ie. when a user deletes, edits, or adds a new mood to
     * their list) and sends the updated status to firestore
     */
    public void updateRecentMoodToFollowers(){
        followingManager.updateRecentMoodToFollowers();
    }

    /**
     * unfollows the user given in the userJar
     * @param userJar the user to unFollow
     */
    public void unFollow(UserJar userJar){
        followingManager.unfollow(userJar);
    }

    /**
     * Populates the Following list for the management screen
     * @param userJarList The view to be populated with users that we're following
     */
    public void initManagementFollowingList(final RecyclerView userJarList){
        @NonNull
        SimpleUserJarAdapter userJarAdaptor = (SimpleUserJarAdapter) userJarList.getAdapter();

        CollectionReference followingListColRef = db
                .collection("users")
                .document(mAuth.getCurrentUser().getUid())
                .collection("following");

        followingListColRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
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

    /**
     * Initiatizes the request list on the management screen by getting the unresolved request from the users mailbox in firestore
     * @param userJarList the view to be populated with requests
     */
    public void initManagementRequestList(final RecyclerView userJarList){
        @NonNull
        SimpleUserJarAdapter userJarAdaptor = (SimpleUserJarAdapter) userJarList.getAdapter();

        CollectionReference requestListColRef = db
                .collection("users")
                .document(mAuth.getCurrentUser().getUid())
                .collection("mailBox");

        requestListColRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
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

    /**
     * deletes an image in the firestore storage(not database)
     * @param imageId the ID of the image
     */
    public void deleteFirestoreImage(String imageId){
        if (recentImagesBox.getImage(imageId) != null){
            recentImagesBox.delImage(imageId);
        }

        // Create a storage reference from our app
        StorageReference storageRef = storage.getReference();

        // Create a reference to the file to delete
        StorageReference desertRef = storageRef.child("Images/" + mAuth.getCurrentUser().getUid() + "/" + imageId);

        // Delete the file
        desertRef
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "File deleted successfully");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "File deleted error");
                    }
                });
    }

    /**
     * uploads a photo to firebase storage
     * @param uniqueImageID the unique image ID
     * @param filePath the local filepath
     * @param imageView imageView for cache
     */
    public void uploadPhotoToStorage(String uniqueImageID, Uri filePath, ImageView imageView) {
        recentImagesBox.addImage(uniqueImageID, imageView);
        StorageReference storageRef = storage.getReference();
        StorageReference storageName = storageRef.child("Images/" + mAuth.getCurrentUser().getUid() + "/" + uniqueImageID);

        storageName
                .putFile(filePath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.d(TAG, "onSuccess: upload image");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: upload image");
                    }
                });
    }

    /**
     * gets a photo from firebase storage
     * @param imageId the image id of the photo
     * @param imageView the imageview the photo will be set to
     */
    // retrieve image from firebase storage and set into imageView
    public void getPhoto(String imageId, ImageView imageView){

        ImageView imageViewTemp = recentImagesBox.getImage(imageId);
        if (imageViewTemp != null){
            imageView.setImageDrawable(imageViewTemp.getDrawable());
            return;
        }

        StorageReference storageRef = storage.getReference();
        StorageReference storageName = storageRef.child("Images/" + mAuth.getCurrentUser().getUid() + "/" + imageId);

        storageName
                .getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String url = uri.toString();
                        Log.d(TAG, "onSuccess: download Photo successful");
                        Picasso.get().load(url).into(imageView);
                        recentImagesBox.addImage(imageId, imageView);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: download Photo");
                    }
                });

    }

    /**
     * gets a photo from firebase storage
     * @param imageId the image ID
     * @param imageView the imageview to display the image
     * @param uid the user's UID
     */
    public void getPhoto(String imageId, ImageView imageView, String uid){

        ImageView imageViewTemp = recentImagesBox.getImage(imageId);
        if (imageViewTemp != null){
            imageView.setImageDrawable(imageViewTemp.getDrawable());
            return;
        }


        StorageReference storageRef = storage.getReference();
        StorageReference storageName = storageRef.child("Images/" + uid + "/" + imageId);

        storageName
                .getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String url = uri.toString();
                        Log.d(TAG, "onSuccess: download Photo successful");
                        Picasso.get().load(url).into(imageView);
                        recentImagesBox.addImage(imageId, imageView);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: download Photo");
                    }
                });
    }

    /**
     * Starts an async task
     * @return returns the database
     */
    public Task<DocumentSnapshot> getAsynchronousTask(){
        return db
                .collection("users")
                .document(mAuth.getCurrentUser().getUid())
                .get();
    }
}
