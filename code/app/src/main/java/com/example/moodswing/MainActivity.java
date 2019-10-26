package com.example.moodswing;

import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;


import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private Button signup_Button, register_Button;
    private EditText addUsernameEditText, addPasswordEditText;
    private String TAG = "Account";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseFirestore.getInstance();
        final CollectionReference conllectionReference = db.collection("Accounts");
        signup_Button = findViewById(R.id.signupButton);
        addUsernameEditText = findViewById(R.id.userField);
        addPasswordEditText = findViewById(R.id.passField);
        register_Button = findViewById(R.id.registerButton);

        signup_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String userName = addUsernameEditText.getText().toString();
                final String passWord = addPasswordEditText.getText,getText().toString();
                HashMap<String, String> data = new HashMap<>();
                if (userName.length() > 4 && passWord.length() > 4) {
                    data.put("username", userName);
                    collectionReference
                            .document(userName)
                            .set(data)
                            .addOnSuccessListener(new OnSuccessListener<Void> () {
                                @Override
                                public void onSucess(Void aVoid) {
                                    Log.d(TAG, "Username has successfully been added");
                                }
                            })
                            .addOnFailureListened(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "Password has successfully been added");
                                }
                            });
                    signup_Button.setText("");
                    addPasswordEditText.setText("");
                }
            }
        })
    }


}
