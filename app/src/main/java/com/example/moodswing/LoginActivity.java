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
import android.widget.ImageButton;
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

    private EditText usernameEditText;
    private EditText passwordEditText;

    private TextView toRegister;
    private ImageButton loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginview);

        // init all elements
        usernameEditText = findViewById(R.id.userField);
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
                final String username = usernameEditText.getText().toString();
                final String password = passwordEditText.getText().toString();

                FirestoreUserDocCommunicator communicator = FirestoreUserDocCommunicator.getInstance();
                if (communicator.userLogin(username, password)== 0){
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("return_mode",RETURN_CODE_TO_MOOD);
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }
            }
        });

    }
}
