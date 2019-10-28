package com.example.moodswing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
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


/**
 * Notes:
 *      - working fine for now
 *      - lacking error handling, if empty program will crash. but will deal error cases later
 */
public class RegisterActivity extends AppCompatActivity {
    private static final int RETURN_CODE_TO_LOGIN = 1;
    //
    private static final String TAG = "RegisterActivity";
    private TextView toLogin;
    private Button registerButton;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private FirebaseFirestore db;
    private static boolean alreadyLoggedIn = false;


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
                toLogin();
            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerNewUser();
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
                        //
                        toLogin();
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

    private void toLogin(){
        Intent returnIntent = new Intent();
        returnIntent.putExtra("return_mode",RETURN_CODE_TO_LOGIN);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }
}