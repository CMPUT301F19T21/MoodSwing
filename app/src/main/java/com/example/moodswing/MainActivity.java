package com.example.moodswing;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.example.moodswing.customDataTypes.FirestoreUserDocCommunicator;
import com.example.moodswing.navigationFragments.FollowingFragment;
import com.example.moodswing.navigationFragments.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {

    private static final int MOOD_HISTORY_SCREEN = 1;
    private static final int FOLLOWING_SCREEN = 2;

    private int currentScreenPointer;

    private Button moodHistoryBtn;
    private Button followingBtn;
    private FloatingActionButton profileBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);


        // link all elements
        moodHistoryBtn = findViewById(R.id.nav_homeBtn);
        followingBtn = findViewById(R.id.nav_followingBtn);
        profileBtn = findViewById(R.id.nav_profile);

        // listeners
        moodHistoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentScreenPointer != MOOD_HISTORY_SCREEN) {
                    currentScreenPointer = MOOD_HISTORY_SCREEN;
                    toMoodHistory();
                }
            }
        });

        followingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentScreenPointer != FOLLOWING_SCREEN) {
                    currentScreenPointer = FOLLOWING_SCREEN;
                    toFollowing();
                }
            }
        });

        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openProfileFragment();
            }
        });

        // other action that need to be init
        toMoodHistory(); // default view -> moodHistory
        currentScreenPointer = MOOD_HISTORY_SCREEN;
    }

    private void toMoodHistory(){
        moodHistoryBtn.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        followingBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        FragmentTransaction fragTrans = getSupportFragmentManager().beginTransaction();
        fragTrans.replace(R.id.fragment_placeHolder, new HomeFragment());
        // fragTrans.setCustomAnimations(android.R.animator.fade_in,android.R.animator.fade_out);
        // will add animation and back stack later
        fragTrans.commit();
    }

    private void toFollowing() {
        followingBtn.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        moodHistoryBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        FragmentTransaction fragTrans = getSupportFragmentManager().beginTransaction();
        fragTrans.replace(R.id.fragment_placeHolder, new FollowingFragment());
        // fragTrans.setCustomAnimations(android.R.animator.fade_in,android.R.animator.fade_out);
        // will add back stack and animation later
        fragTrans.commit();
    }

    public void openProfileFragment(){
        new profileFragment().show(getSupportFragmentManager(), "profile");
    }

    public void signOut() {
        FirestoreUserDocCommunicator.destroy();
        finishAffinity();
        startActivity(new Intent(this, LoginActivity.class));
    }

}

