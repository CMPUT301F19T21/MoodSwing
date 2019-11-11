package com.example.moodswing;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.example.moodswing.Fragments.ManageRequestFragment;
import com.example.moodswing.Fragments.MoodDetailFragment;
import com.example.moodswing.customDataTypes.FirestoreUserDocCommunicator;
import com.example.moodswing.Fragments.FollowingFragment;
import com.example.moodswing.Fragments.HomeFragment;
import com.example.moodswing.Fragments.profileFragment;
import com.example.moodswing.customDataTypes.UserJar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentTransaction;

//This activity holds the following, profile, and Home fragments, and holds the functionality for
// redirecting the user to other fragments

/**
 * This class is the main Activity, it handles the fragments navigation
 *
 */
public class MainActivity extends AppCompatActivity {

    private FirestoreUserDocCommunicator communicator;

    private static final int MOOD_HISTORY_SCREEN = 1;
    private static final int FOLLOWING_SCREEN = 2;

    private int currentScreenPointer;
    private boolean ifDisplayNotification;

    private Button moodHistoryBtn;
    private Button followingBtn;
    private FloatingActionButton profileBtn;
    private FrameLayout notificationBar;
    private CardView notificationBar_card;
    private FloatingActionButton notificationBar_closeBtn;


    /**
     * Creates the buttons and handles redirects to different fragments
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        communicator = FirestoreUserDocCommunicator.getInstance();


        // link all elements
        moodHistoryBtn = findViewById(R.id.nav_homeBtn);
        followingBtn = findViewById(R.id.nav_followingBtn);
        profileBtn = findViewById(R.id.nav_profile);
        notificationBar = findViewById(R.id.notificationBar);
        notificationBar_card = findViewById(R.id.notificationBar_card);
        notificationBar_closeBtn = findViewById(R.id.notificationBar_close_button);

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

        notificationBar_closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notificationBar.setVisibility(View.GONE);
            }
        });

        // other action that need to be init

        communicator.setAutoDisplayViewForNewRequest(notificationBar);
        toMoodHistory(); // default view -> moodHistory
    }

    /**
     * The functionality for transitioning to the MoodHistory fragment
     */
    public void toMoodHistory(){
        moodHistoryBtn.setBackgroundColor(getResources().getColor(R.color.color_button_lightGrey));
        followingBtn.setBackgroundColor(getResources().getColor(R.color.color_button_lightGrey_pressed));
        FragmentTransaction fragTrans = getSupportFragmentManager().beginTransaction();
        fragTrans.replace(R.id.fragment_placeHolder, new HomeFragment());
        fragTrans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        // fragTrans.setCustomAnimations(android.R.animator.fade_in,android.R.animator.fade_out);
        // will add animation and back stack later
        fragTrans.commit();
    }
    /**
     * The functionality for transitioning to the DetailedView fragment
     */
    public void toDetailedView(int moodPosition) {
        FragmentTransaction fragTrans = getSupportFragmentManager().beginTransaction();
        fragTrans.replace(R.id.fragment_placeHolder, new MoodDetailFragment(moodPosition));
        fragTrans.addToBackStack(null);
        fragTrans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragTrans.commit();
    }

    /**
     * The functionality for transitioning to the toFollowing fragment
     */
    public void toFollowing() {
        followingBtn.setBackgroundColor(getResources().getColor(R.color.color_button_lightGrey));
        moodHistoryBtn.setBackgroundColor(getResources().getColor(R.color.color_button_lightGrey_pressed));
        FragmentTransaction fragTrans = getSupportFragmentManager().beginTransaction();
        fragTrans.replace(R.id.fragment_placeHolder, new FollowingFragment());
        fragTrans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        // fragTrans.setCustomAnimations(android.R.animator.fade_in,android.R.animator.fade_out);
        // will add back stack and animation later
        fragTrans.commit();
    }

    /**
     * The functionality for transitioning to the Profile fragment
     */
    public void openProfileFragment(){
        new profileFragment().show(getSupportFragmentManager(), "profile");
    }

    public void openManageRequestFragment(UserJar userJar) {
        new ManageRequestFragment(userJar).show(getSupportFragmentManager(), "manage_request");
    }
    /**
     * Signs the user out, used when logoutBtn is clicked
     */
    public void signOut() {
        FirestoreUserDocCommunicator.destroy();
        finishAffinity();
        startActivity(new Intent(this, LoginActivity.class));
    }

}

