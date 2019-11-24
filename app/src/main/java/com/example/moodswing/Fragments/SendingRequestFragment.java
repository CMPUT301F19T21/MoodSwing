package com.example.moodswing.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.moodswing.R;
import com.example.moodswing.customDataTypes.FirestoreUserDocCommunicator;
import com.example.moodswing.customDataTypes.UserJar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


/**
 * The screen that is redirected to when the user presses the button in the following/follower management screen.
 * It will prompt the user for a username, and a request will be sent to that user to become their follower
 */
public class SendingRequestFragment extends DialogFragment {
    private static final String TAG = "SendingRequestFragment";
    private FirestoreUserDocCommunicator communicator;
    private EditText usernameEditText;

    private FloatingActionButton confirmBtn;
    private FloatingActionButton backBtn;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        communicator = FirestoreUserDocCommunicator.getInstance();
    }

    /**
     * Creates the confirm and back button and the EditText view to enter the username, sends the username typed
     * to the FirestoreUserDocCommunicator for requesting to follow
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_send_request, container, false);
        getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        // link elements
        usernameEditText = view.findViewById(R.id.requestFrag_requestEnter);
        confirmBtn = view.findViewById(R.id.requestFrag_confirm);
        backBtn = view.findViewById(R.id.requestFrag_back);

        // listeners
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString();
                sendingRequest(username);
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        // other
        return view;
    }

    private boolean ifInFollowing(String username){
        for (UserJar userJar : communicator.getUserJars()){
            if (userJar.getUsername().equals(username)){
                return true;
            }
        }
        return false;
    }

    private void sendingRequest(String username){
        if (username.isEmpty()) {
            Toast.makeText(getContext(), "Sorry, username cannot be empty", Toast.LENGTH_SHORT).show();
        }else{
            if (communicator.getUsername().equals(username)){
                Toast.makeText(getContext(), "Sorry, you cannot follow yourself", Toast.LENGTH_SHORT).show();
            }else{
                if (ifInFollowing(username)){
                    Toast.makeText(getContext(), "Sorry, user already in your following List", Toast.LENGTH_SHORT).show();
                }else{
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    Query findUserColQuery = db
                            .collection("users")
                            .whereEqualTo("username", username)
                            .limit(1);

                    findUserColQuery
                            .get()
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    if (task.getResult().isEmpty()) {
                                        Toast.makeText(getContext(), "Sorry, the username you entered doesn't exist",
                                                Toast.LENGTH_SHORT).show();
                                    } else {
                                        communicator.sendFollowingRequest(username);
                                        dismiss();
                                    }
                                } else {
                                    Log.d(TAG, "Error while executing finding username query: ", task.getException());
                                }
                            });
                }
            }
        }
    }
}