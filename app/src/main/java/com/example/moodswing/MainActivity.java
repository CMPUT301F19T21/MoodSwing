package com.example.moodswing;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private static final int LOGIN_ACTIVITY_REQUEST_CODE = 1;
    private static final int REG_ACTIVITY_REQUEST_CODE = 2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_screen);

        /* login */
    }

    private void toLogin(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent, LOGIN_ACTIVITY_REQUEST_CODE);
    }

    private void toReg(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent, REG_ACTIVITY_REQUEST_CODE);
    }

    private void toMood(){
        Intent intent = new Intent (this, MoodHistoryActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOGIN_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                switch(data.getIntExtra("return_mode",0)){
                    case 1:
                        toReg();
                        break;
                    case 2:
                        toMood();
                        break;
                    case 0:
                        // should never happen
                }
            }
        }
        if (requestCode == REG_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                switch(data.getIntExtra("return_mode",0)){
                    case 1:
                        toLogin();
                        break;
                    case 0:
                        // should never happen
                }
            }
        }
    }

}
