package com.example.moodswing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


/**
 * Notes:
 *      - working fine for now
 *      - lacking error handling, if empty program will crash. but will deal error cases later
 */
public class LoginActivity extends AppCompatActivity {
    private static final int RETURN_CODE_TO_REG = 1;
    private static final int RETURN_CODE_TO_MOOD = 2;

    //
    private static final String TAG = "LoginActivity";
    private FirebaseFirestore db;

    private EditText emailEditText;
    private EditText passwordEditText;

    private TextView toRegister;
    private ImageButton loginBtn;


    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginview);
        mAuth = FirebaseAuth.getInstance();
        //

        // init all elements
        emailEditText = findViewById(R.id.userEmailField);
        passwordEditText = findViewById(R.id.passField);
        toRegister = findViewById(R.id.switchToReg);
        loginBtn = findViewById(R.id.confirmbtn);

        // set listeners
        toRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("return_mode",RETURN_CODE_TO_REG);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginProcess();

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // check if user is signed in (non-null) and update UI accordingly.
        if (mAuth.getCurrentUser()!=null){
            loginPost();
        }
    }

    private void loginPost() {
        FirestoreUserDocCommunicator communicator = FirestoreUserDocCommunicator.getInstance();
        Intent returnIntent = new Intent();
        returnIntent.putExtra("return_mode",RETURN_CODE_TO_MOOD);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    private void loginProcess(){
        final String email = emailEditText.getText().toString();
        final String password = passwordEditText.getText().toString();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            loginPost();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //
                        }
                    }
                });
    }
    public boolean onKeyDown(int keyCode, KeyEvent event){
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory(Intent.CATEGORY_HOME);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (keyCode == KeyEvent.KEYCODE_BACK){
            startActivity(homeIntent);
            return true;
        }
        return super.onKeyDown(keyCode,event);
    }
}
