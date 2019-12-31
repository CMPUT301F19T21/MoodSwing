package com.example.moodswing.customDataTypes;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

public class CommunicatorMoodListManager {
    private FirestoreUserDocCommunicator communicator;
    private ObservableMoodEventArray moodEvents;
    private CollectionReference moodEventsRef;

    public CommunicatorMoodListManager (FirestoreUserDocCommunicator communicator) {
        this.communicator = communicator;
        this.moodEvents = new ObservableMoodEventArray();
        this.moodEventsRef = communicator.getDB()
                .collection("users")
                .document(communicator.getUser().getUid())
                .collection("MoodEvents");

        // init
        this.getMoodEventListInstance();
    }

    public void addMoodEvent(MoodEvent moodEvent) {
        moodEventsRef
                .document(moodEvent.getUniqueID())
                .set(moodEvent)
                .addOnSuccessListener(aVoid -> {
                    moodEvents.add(moodEvent);
                    moodEvents.notifyChange();
                    communicator.announceChangeToMoodList();
                    communicator.updateRecentMoodToFollowers();
                })
                .addOnFailureListener(e -> {
                    // nothing to do for now
                });
    }

    public void removeMoodEvent(MoodEvent moodEvent){
        moodEventsRef
                .document(moodEvent.getUniqueID())
                .delete()
                .addOnSuccessListener(aVoid -> {
                    moodEvents.remove(moodEvent);
                    moodEvents.notifyChange();
                    communicator.announceChangeToMoodList();
                    communicator.updateRecentMoodToFollowers();
                    if (moodEvent.getImageId() != null) {
                        communicator.deleteFirestoreImage(moodEvent.getImageId());
                    }
                }).addOnFailureListener(e -> {
                    //
                });
    }

    public void updateMoodEvent(MoodEvent moodEvent){
        moodEventsRef
                .document(moodEvent.getUniqueID())
                .set(moodEvent)
                .addOnSuccessListener(aVoid -> {
                    moodEvents.remove(moodEvent);
                    moodEvents.add(moodEvent);
                    moodEvents.notifyChange();
                    communicator.announceChangeToMoodList();
                    communicator.updateRecentMoodToFollowers();
                })
                .addOnFailureListener(e -> {
                    // roll back
                });
    }

    public void getMoodEventListInstance() {
        moodEventsRef
                .orderBy("timeStamp", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    moodEvents.clear();
                    if (task.getResult() != null){
                        if (!(task.getResult().isEmpty())){
                            for (DocumentSnapshot moodEventDoc : task.getResult().getDocuments()){
                                MoodEvent moodEvent = moodEventDoc.toObject(MoodEvent.class);
                                moodEvents.add(moodEvent);
                            }
                        }
                    }
                    moodEvents.notifyChange();
                });
    }

    public ObservableMoodEventArray getMoodEvents(){
        return moodEvents;
    }
}
