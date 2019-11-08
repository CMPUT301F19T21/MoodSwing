package com.example.moodswing;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;

import com.example.moodswing.Fragments.ManageRequestFragment;
import com.example.moodswing.Fragments.ManagementFragment;
import com.example.moodswing.Fragments.MoodDetailFragment;
import com.example.moodswing.customDataTypes.FirestoreUserDocCommunicator;
import com.example.moodswing.Fragments.FollowingFragment;
import com.example.moodswing.Fragments.HomeFragment;
import com.example.moodswing.Fragments.profileFragment;
import com.example.moodswing.customDataTypes.UserJar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.transition.Slide;
import androidx.transition.Transition;

//This activity holds the following, profile, and Home fragments, and holds the functionality for
// redirecting the user to other fragments

/**
 * This class is the main Activity, it handles the fragments navigation
 *
 */
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
    }

    public void toMoodHistory(){
        moodHistoryBtn.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        followingBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        FragmentTransaction fragTrans = getSupportFragmentManager().beginTransaction();
        fragTrans.replace(R.id.fragment_placeHolder, new HomeFragment());
        fragTrans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        // fragTrans.setCustomAnimations(android.R.animator.fade_in,android.R.animator.fade_out);
        // will add animation and back stack later
        fragTrans.commit();
    }

    public void toDetailedView(int moodPosition) {
        FragmentTransaction fragTrans = getSupportFragmentManager().beginTransaction();
        fragTrans.replace(R.id.fragment_placeHolder, new MoodDetailFragment(moodPosition));
        fragTrans.addToBackStack(null);
        fragTrans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragTrans.commit();
    }

    public void toFollowing() {
        followingBtn.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        moodHistoryBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        FragmentTransaction fragTrans = getSupportFragmentManager().beginTransaction();
        fragTrans.replace(R.id.fragment_placeHolder, new FollowingFragment());
        fragTrans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        // fragTrans.setCustomAnimations(android.R.animator.fade_in,android.R.animator.fade_out);
        // will add back stack and animation later
        fragTrans.commit();
    }

    public void toManagement() {
        FragmentTransaction fragTrans = getSupportFragmentManager().beginTransaction();
        fragTrans.replace(R.id.fragment_placeHolder, new ManagementFragment());
        fragTrans.addToBackStack(null);
        fragTrans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragTrans.commit();
    }

    public void openProfileFragment(){
        new profileFragment().show(getSupportFragmentManager(), "profile");
    }

    public void openManageRequestFragment(UserJar userJar) {
        new ManageRequestFragment(userJar).show(getSupportFragmentManager(), "manage_request");
    }

    public void signOut() {
        FirestoreUserDocCommunicator.destroy();
        finishAffinity();
        startActivity(new Intent(this, LoginActivity.class));
    }

}

