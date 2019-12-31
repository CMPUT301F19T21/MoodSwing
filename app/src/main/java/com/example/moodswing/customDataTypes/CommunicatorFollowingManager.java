package com.example.moodswing.customDataTypes;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class CommunicatorFollowingManager {
    private FirestoreUserDocCommunicator communicator;
    private ObservableUserJarArray userJarArray;

    public CommunicatorFollowingManager (FirestoreUserDocCommunicator communicator) {
        this.communicator = communicator;
        this.userJarArray = new ObservableUserJarArray();

        // init
        this.setUpFollowingListListener();
    }

    public void sendFollowingRequest (String username) {
        // check if uid exist
        communicator.getDB()
                .collection("users")
                .whereEqualTo("username",username)
                .limit(1)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.getResult() != null) {
                        if (!(task.getResult().isEmpty())){
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String UID = document.getId();
                                addRequestToMailBox(UID);
                            }
                        }
                    }
                });
    }

    private void addRequestToMailBox(String targetUID){
        UserJar userJar = new UserJar();
        userJar.setUsername(communicator.getUsername());
        userJar.setUID(communicator.getUser().getUid());

        communicator.getDB()
                .collection("users")
                .document(targetUID) // enter other user's doc
                .collection("mailBox")
                .document(communicator.getUser().getUid())
                .set(userJar)
                .addOnSuccessListener(aVoid -> {
                    //
                })
                .addOnFailureListener(e -> {
                    //
                });
    }

    public void removeRequest (UserJar userJar) {
        // error code need to be created
        communicator.getDB()
                .collection("users")
                .document(communicator.getUser().getUid())
                .collection("mailBox")
                .document(userJar.getUID())
                .delete()
                .addOnSuccessListener(aVoid -> {
                    //
                })
                .addOnFailureListener(e -> {
                    //
                });
    }

    public void acceptRequest(UserJar userJar) {
        // responding to sender's request
        // two action to do here,
        // 1. adding the uid&username (current entry) to user's permitted list
        // 2. pack uid and RecentMood, send recentMood to sender
        //    a. send uid/username to sender's following list  b. trigger one recentMood update

        communicator.getDB()
                .collection("users")
                .document(communicator.getUser().getUid())
                .collection("permittedList")
                .document(userJar.getUID())
                .set(userJar)
                .addOnSuccessListener(aVoid -> {
                    addToSendersFollowing(userJar);
                    //
                })
                .addOnFailureListener(e -> {
                    //
                });
    }

    private void addToSendersFollowing(UserJar sendersUserJar) {
        UserJar myUserJar = new UserJar();
        myUserJar.setUID(communicator.getUser().getUid());
        myUserJar.setUsername(communicator.getUsername());

        communicator.getDB()
                .collection("users")
                .document(sendersUserJar.getUID())
                .collection("following")
                .document(communicator.getUser().getUid())
                .set(myUserJar)
                .addOnSuccessListener(aVoid -> {
                    removeRequest(sendersUserJar);
                    if (communicator.getMoodEvents().isEmpty()){
                        pullRecentMoodEventToUser(sendersUserJar.getUID());
                    }else{
                        pushRecentMoodEventToUser(sendersUserJar.getUID());
                    }
                })
                .addOnFailureListener(e -> {
                    //
                });
    }

    public void updateRecentMoodToFollowers(){
        communicator.getDB()
                .collection("users")
                .document(communicator.getUser().getUid())
                .collection("permittedList")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.getResult() != null){
                        if (!(task.getResult().isEmpty())){
                            for (DocumentSnapshot userJarDoc : task.getResult().getDocuments()){
                                UserJar userJar = userJarDoc.toObject(UserJar.class);
                                if (communicator.getMoodEvents().isEmpty()){
                                    pullRecentMoodEventToUser(userJar.getUID());
                                }else{
                                    pushRecentMoodEventToUser(userJar.getUID());
                                }
                            }
                        }
                    }
                });
    }

    private void pushRecentMoodEventToUser(String uid){
        // construct UserJar
        UserJar myUserJarWithMood = new UserJar();
        myUserJarWithMood.setUsername(communicator.getUsername());
        myUserJarWithMood.setUID(communicator.getUser().getUid());
        myUserJarWithMood.setMoodEvent(communicator.getMoodEvents().get(0));

        // send it to target
        communicator.getDB()
                .collection("users")
                .document(uid)
                .collection("followingMoodList")
                .document(communicator.getUser().getUid())
                .set(myUserJarWithMood)
                .addOnSuccessListener(aVoid -> {
                    //
                })
                .addOnFailureListener(e -> {
                    //
                });
    }

    private void pullRecentMoodEventToUser(String uid) {
        communicator.getDB()
                .collection("users")
                .document(uid)
                .collection("followingMoodList")
                .document(communicator.getUser().getUid())
                .delete()
                .addOnSuccessListener(aVoid -> {
                    //
                })
                .addOnFailureListener(e -> {
                    //
                });
    }

    private void setUpFollowingListListener (){
        communicator.getDB()
                .collection("users")
                .document(communicator.getUser().getUid())
                .collection("followingMoodList")
                .orderBy("moodEvent.timeStamp",Query.Direction.DESCENDING)
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    userJarArray.clear();
                    for (QueryDocumentSnapshot userJarDoc : queryDocumentSnapshots) {
                        UserJar userJar = userJarDoc.toObject(UserJar.class);
                        userJarArray.add(userJar);
                    }
                    userJarArray.notifyChange();
                });
    }

    public void unfollow(UserJar userJar){
        // 从对方的permittedList中移除自己
        // 把对方从自己的followingList中移除
        // 把对方从自己的followingMoodList中移除

        DocumentReference followingListUserDocRef = communicator
                .getDB()
                .collection("users")
                .document(communicator.getUser().getUid())
                .collection("following")
                .document(userJar.getUID());

        DocumentReference followingMoodListUserDocRef = communicator
                .getDB()
                .collection("users")
                .document(communicator.getUser().getUid())
                .collection("followingMoodList")
                .document(userJar.getUID());

        communicator.getDB()
                .collection("users")
                .document(userJar.getUID())
                .collection("permittedList")
                .document(communicator.getUser().getUid())
                .delete()
                .addOnSuccessListener(aVoid -> {
                    followingListUserDocRef
                            .delete()
                            .addOnSuccessListener(aVoid1 -> {
                                followingMoodListUserDocRef
                                        .delete()
                                        .addOnSuccessListener(aVoid11 -> {
                                            //
                                        })
                                        .addOnFailureListener(e -> {
                                            // add back two
                                        });
                            })
                            .addOnFailureListener(e -> {
                                // add back
                            });
                })
                .addOnFailureListener(e -> {
                    //
                });
    }

    public ObservableUserJarArray getUserJarArray(){
        return userJarArray;
    }
}
