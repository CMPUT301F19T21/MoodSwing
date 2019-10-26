package com.example.moodswing;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * MainActivity
 */
public class MainActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private static final int USER_ID_REQUEST = 1;
    DocumentReference userRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseFirestore.getInstance(); // init db

        /* login */
        Intent intentLoginActivity = new Intent(this, LoginActivity.class);
        startActivityForResult(intentLoginActivity, USER_ID_REQUEST);
        /* life cycle reminder
         * the end of onCreate, all the object that should be init before user login should be
         * written in onPostLogin
         */


    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == USER_ID_REQUEST) {
            if (resultCode == RESULT_OK) {
                String username = data.getStringExtra("username");
                onPostLogin(username);
            }
        }
    }





    private void onPostLogin(String username){
        // initUserRef
        DocumentReference userRef = db.collection("Accounts").document(username);
        // other actions after login:
    }
}
