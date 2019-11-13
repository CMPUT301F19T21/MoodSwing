package com.example.moodswing;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentTransaction;

import com.example.moodswing.Fragments.EmptyNotificationFragment;
import com.example.moodswing.Fragments.FollowingFragment;
import com.example.moodswing.Fragments.MoodDetailFollowingFragment;
import com.example.moodswing.Fragments.MoodDetailFragment;
import com.example.moodswing.Fragments.MoodHistoryFragment;
import com.example.moodswing.Fragments.ProfileFragment;
import com.example.moodswing.customDataTypes.FirestoreUserDocCommunicator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QuerySnapshot;

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
        setContentView(R.layout.activity_main);

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
                notificationBar.setVisibility(View.INVISIBLE);
            }
        });

        notificationBar_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ManagementActivity.class));
            }
        });

        // other action that need to be init

        communicator.setAutoDisplayViewForNewRequest(notificationBar);
        toMoodHistory(); // default view -> moodHistory
        displayEmptyNotificationIfEmpty();
    }

    /**
     * The functionality for transitioning to the MoodHistory fragment
     */
    public void toMoodHistory(){
        moodHistoryBtn.setBackgroundColor(getResources().getColor(R.color.color_button_lightGrey));
        followingBtn.setBackgroundColor(getResources().getColor(R.color.color_button_lightGrey_pressed));
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.nav_default_pop_enter_anim, R.anim.nav_default_pop_exit_anim)
                .replace(R.id.fragment_placeHolder, new MoodHistoryFragment())
                .commit();
    }
    /**
     * The functionality for transitioning to the DetailedView fragment
     */
    public void toDetailedView(int moodPosition) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_placeHolder, new MoodDetailFragment(moodPosition))
                .addToBackStack(null)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
    }

    public void toDetailedView_following(int moodPosition) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_placeHolder, new MoodDetailFollowingFragment(moodPosition))
                .addToBackStack(null)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
    }


    /**
     * The functionality for transitioning to the toFollowing fragment
     */
    public void toFollowing() {
        followingBtn.setBackgroundColor(getResources().getColor(R.color.color_button_lightGrey));
        moodHistoryBtn.setBackgroundColor(getResources().getColor(R.color.color_button_lightGrey_pressed));
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.nav_default_pop_enter_anim, R.anim.nav_default_pop_exit_anim)
                .replace(R.id.fragment_placeHolder, new FollowingFragment())
                .commit();
    }

    /**
     * The functionality for transitioning to the Profile fragment
     */
    public void openProfileFragment(){
        new ProfileFragment().show(getSupportFragmentManager(), "profile");
    }

    public void displayEmptyNotification(){
        getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .addToBackStack("EmptyNotification")
                .replace(R.id.notification_center, new EmptyNotificationFragment())
                .commit();
    }
    /**
     * Signs the user out, used when logoutBtn is clicked
     */
    public void signOut() {
        FirestoreUserDocCommunicator.destroy();
        finishAffinity();
        startActivity(new Intent(this, LoginActivity.class));
    }

    private void displayEmptyNotificationIfEmpty(){
        DocumentReference userDocRef = communicator.getUserDocRef();
        userDocRef.collection("MoodEvents")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.getResult().isEmpty()){
                            displayEmptyNotification();
                        }
                    }
                });
    }


}

