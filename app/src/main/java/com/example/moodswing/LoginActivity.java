package com.example.moodswing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

public class LoginActivity extends AppCompatActivity {
    //
    private static final String TAG = "LoginActivity";

    EditText usernameEditText;
    EditText passwordEditText;

    TextView toRegister;
    Button loginButton;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginview);

        // init all elements

        usernameEditText = findViewById(R.id.userField);
        passwordEditText = findViewById(R.id.passField);
        toRegister = findViewById(R.id.switchToReg);
        loginButton = findViewById(R.id.loginButton);
        db = FirebaseFirestore.getInstance();

        // set listeners
        toRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginProcess();
            }
        });

    }

    /**
     * This method is used for login username and password checking
     */
    private void loginProcess() {


        final String username = usernameEditText.getText().toString();
        final String password = passwordEditText.getText().toString();

        /**
         * Possible error handling missing here, for now
         *
         */

        final DocumentReference userRef = db.collection("Accounts").document(username);
        userRef.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot userDoc = task.getResult();
                            if (userDoc.exists()) {
                                Log.d(TAG,"userDoc exist");
                                // check password
                                if (userDoc.get("password").equals(password)){
                                    //loginSuccessful
                                    loginOnSuccessful(username);
                                }else{
                                    //loginCheckFail
                                }
                            }else{
                                Log.d(TAG, "userDoc not exist");
                                // print something
                            }
                        }else{
                            Log.d(TAG, "Error finding user doc");
                            // print something
                        }
                    }
                });

    }

    /**
     * This method will be called when login process success, it will return to MainActivity with
     * @param username
     *      A shallow DocumentSnapshot object contains basic user info. will be used to construct profile page
     *      in main Activity
     */
    private void loginOnSuccessful(String username){
        Intent returnIntent = new Intent();
        returnIntent.putExtra("username",username);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

}
