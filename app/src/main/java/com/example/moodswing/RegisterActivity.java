package com.example.moodswing;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.Map;


/**
 * This is the reg activity
 * Notes:
 *      - will add code here in part4 to make sure username uniqueness
 */
//This activity handles registration of a new user
public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";
    private TextView toLogin;
    private FloatingActionButton registerButton;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText usernameEditText;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;


    /**
     * initializes the email, username, and password fields as well as the OnClickListener for
     * going back to the login screen and to the registration screen
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

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
                finish();
            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerNewUser();
            }
        });
    }

    /**
     * registration handling with firestore
     */
    private void registerNewUser(){
        // error handling missing here, for now - gengyuan

        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String username = usernameEditText.getText().toString();

        if ((username.isEmpty())||(password.isEmpty())||(email.isEmpty())) {
            Toast.makeText(RegisterActivity.this, "Sorry, empty email/username/password",
                    Toast.LENGTH_SHORT).show();
        }else{
            Query findUserColQuery = db
                    .collection("users")
                    .whereEqualTo("username", username)
                    .limit(1);

            findUserColQuery
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            if (task.getResult().isEmpty()) {
                                createUser(email, username, password);
                            } else {
                                Toast.makeText(RegisterActivity.this, "Sorry, the username already exist",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.d(TAG, "Error while executing finding username query: ", task.getException());
                        }
                    });
        }
    }

    private void createUser(String email, String username, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            createFirestoreUserDoc(username);
                            // all done, successful!!
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Authentication failed, email may already be used",
                                    Toast.LENGTH_SHORT).show();
                            // do something, but for now do nothing.
                        }
                    }
                });
    }

    private void createFirestoreUserDoc(String username){
        Map<String, Object> newUser = new HashMap<>();
        newUser.put("username",username);
        db.collection("users").document(mAuth.getCurrentUser().getUid())
                .set(newUser);
    }
}