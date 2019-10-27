package com.example.moodswing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    //
    private static final String TAG = "RegisterActivity";
    TextView toLogin;
    Button registerButton;
    EditText usernameEditText;
    EditText passwordEditText;
    FirebaseFirestore db;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registerview);

        // link all elements
        toLogin = findViewById(R.id.switchToLogin);
        registerButton = findViewById(R.id.registerButton);
        usernameEditText = findViewById(R.id.regUser);
        passwordEditText = findViewById(R.id.regPassword);
        db = FirebaseFirestore.getInstance();

        // set listeners
        toLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerNewUser();
                /**
                 * missing error handling, for now
                 *      - what if register failed? dont close screen, do something
                 */
            }
        });


    }

    private void registerNewUser(){
        // error handling missing here, for now - gengyuan

        final String userName = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        /**
         * error checking here, some ideas:
         *      - check if username already exists
         *      - check if username and password valid
         */

        // key value pair
        final DocumentReference userRef = db.collection("Accounts").document(userName);
        Map<String, String> newUser = new HashMap<>();
        newUser.put("password", password);

        // add to database
        userRef
                .set(newUser)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "User creation successful :" + userName);
                        finish(); // return to signup.
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Error adding user", e);
                    }
                });
    }
}