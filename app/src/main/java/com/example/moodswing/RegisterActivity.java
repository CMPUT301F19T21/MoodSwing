package com.example.moodswing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    private FloatingActionButton registerButton;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText usernameEditText;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registerview);
        mAuth = FirebaseAuth.getInstance();

        // link all elements
        toLogin = findViewById(R.id.switchToLog);
        registerButton = findViewById(R.id.regButton);
        emailEditText = findViewById(R.id.regEmail);
        usernameEditText = findViewById(R.id.regUsername);
        passwordEditText = findViewById(R.id.regPassword);

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

        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String username = usernameEditText.getText().toString();

        /**
         * error checking here, some ideas:
         *      - check if username already exists
         *      - check if username and password valid
         */

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            Map<String, Object> newUser = new HashMap<>();
                            newUser.put("username",username);
                            db.collection("users").document(user.getUid())
                                    .set(newUser);
                            toLogin();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            // do something, but for now do nothing.
                        }
                    }
                });
    }

    private void toLogin(){
        finish();
    }
}