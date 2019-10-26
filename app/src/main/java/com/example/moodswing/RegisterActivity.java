package com.example.moodswing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity {
    //
    TextView toLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registerview);

        // link all elements
        toLogin = findViewById(R.id.switchToLogin);




        // set listeners
        toLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
}

    /*
    public void createUser(){
        final CollectionReference conllectionReference = db.collection("Accounts");
        Button signup_Button = findViewById(R.id.signupButton);
        Button register_Button = findViewById(R.id.registerButton);
        EditText addUsernameEditText = findViewById(R.id.userField);
        EditText addPasswordEditText = findViewById(R.id.passField);


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
        });

    }*/