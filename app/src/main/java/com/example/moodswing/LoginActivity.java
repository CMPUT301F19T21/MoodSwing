package com.example.moodswing;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.moodswing.customDataTypes.FirestoreUserDocCommunicator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;


//
/**
 * The login screen. This is the entrypoint for our app. User will be prompted for login credentials or can register.
 *
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
    private FloatingActionButton loginBtn;


    private FirebaseAuth mAuth;


    /**
     * Initializes the buttons and editTexts
     */
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
        loginBtn = findViewById(R.id.loginComfirmBtn);

        // set listeners
        toRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),RegisterActivity.class));
            }
        });
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginProcess();
            }
        });
    }

    /**
     * If the user is already logged in, they will skip the login screen and go straight to the
     * main activity
     */
    @Override
    public void onStart() {
        super.onStart();
        // check if user is signed in (non-null) and update UI accordingly.
        if (mAuth.getCurrentUser()!=null){
            loginPost();
        }
    }

    /**
     * Moving to the main activity upon successful login
     */
    private void loginPost() {
        FirestoreUserDocCommunicator communicator = FirestoreUserDocCommunicator.getInstance();
        //Intent returnIntent = new Intent();
        //returnIntent.putExtra("return_mode",RETURN_CODE_TO_MOOD);
        //setResult(Activity.RESULT_OK, returnIntent);
        // finish(); ------- notes --------
        // now MainActivity Screen is kinda useless, but may be useful if we want make login fragment
        Intent intent = new Intent (this, MainActivity.class);
        finishAffinity();
        startActivity(intent);
    }


    /**
     * This method stores the new user in firestore
     */
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
}
