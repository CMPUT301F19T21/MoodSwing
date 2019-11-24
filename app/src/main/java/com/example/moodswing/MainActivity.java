package com.example.moodswing;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.moodswing.Fragments.EmptyNotificationFollowingFragment;
import com.example.moodswing.Fragments.EmptyNotificationFragment;
import com.example.moodswing.Fragments.FilterFragment;
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
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
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

    private ImageButton moodHistoryBtn;
    private ImageButton followingBtn;
    private FloatingActionButton profileBtn;
    private FrameLayout notificationBar;
    private CardView notificationBar_card;
    private FloatingActionButton notificationBar_closeBtn;

    private Boolean moodHistoryIsEmpty;
    private Boolean moodFollowingIsEmpty;


    /**
     * Creates the buttons and handles redirects to different fragments
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        communicator = FirestoreUserDocCommunicator.getInstance();
        moodHistoryIsEmpty = null;      // init to default value - true
        moodFollowingIsEmpty = null;    //


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
        toMoodHistory();
        initEmptyListener();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    /**
     * The functionality for transitioning to the MoodHistory fragment
     */
    public void toMoodHistory(){
        moodHistoryBtn.setColorFilter(getResources().getColor(R.color.nav_button_light));
        followingBtn.setColorFilter(getResources().getColor(R.color.nav_button_darkGrey));
        followingBtn.setScaleX(1.0f);
        followingBtn.setScaleY(1.0f);
        moodHistoryBtn.setScaleX(1.4f);
        moodHistoryBtn.setScaleY(1.4f);
        currentScreenPointer = MOOD_HISTORY_SCREEN;
        if ((moodHistoryIsEmpty != null) && (moodHistoryIsEmpty)){
            toMoodHistoryEmptyFragment();
        }else {

            getSupportFragmentManager()
                    .beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .replace(R.id.fragment_placeHolder, new MoodHistoryFragment(), "MoodHistoryFragment")
                    .commitAllowingStateLoss();
        }
    }

    /**
     * The functionality for transitioning to the toFollowing fragment
     */
    public void toFollowing() {
        followingBtn.setColorFilter(getResources().getColor(R.color.nav_button_light));
        moodHistoryBtn.setColorFilter(getResources().getColor(R.color.nav_button_darkGrey));
        followingBtn.setScaleX(1.4f);
        followingBtn.setScaleY(1.4f);
        moodHistoryBtn.setScaleX(1.0f);
        moodHistoryBtn.setScaleY(1.0f);
        currentScreenPointer = FOLLOWING_SCREEN;
        if ((moodHistoryIsEmpty != null) && (moodFollowingIsEmpty)){
            toFollowingEmptyFragment();
        }else {
            getSupportFragmentManager()
                    .beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .replace(R.id.fragment_placeHolder, new FollowingFragment(), "FollowingFragment")
                    .commitAllowingStateLoss();
        }
    }

    /**
     * The functionality for transitioning to the DetailedView fragment
     */
    public void toDetailedView(int moodPosition) {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_placeHolder, new MoodDetailFragment(moodPosition),"moodHistoryDetailedFrag")
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commitAllowingStateLoss();
    }

    public void toDetailedView_following(int moodPosition) {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_placeHolder, new MoodDetailFollowingFragment(moodPosition),"followingDetailedFrag")
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commitAllowingStateLoss();
    }

    public void openFilterFragment(int mode) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_fullScreenOverlay, new FilterFragment(mode),"filterFrag")
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commitAllowingStateLoss();
    }

    /**
     * The functionality for transitioning to the Profile fragment
     */
    public void openProfileFragment(){
        new ProfileFragment().show(getSupportFragmentManager(), "profile");
    }

    public void toMoodHistoryEmptyFragment(){
        getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.fragment_placeHolder, new EmptyNotificationFragment())
                .commitAllowingStateLoss();
    }

    public void toFollowingEmptyFragment(){
        getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.fragment_placeHolder, new EmptyNotificationFollowingFragment())
                .commitAllowingStateLoss();
    }

    /**
     * Signs the user out, used when logoutBtn is clicked
     */
    public void signOut() {
        FirestoreUserDocCommunicator.destroy();
        finishAffinity();
        startActivity(new Intent(this, LoginActivity.class));
    }

    private void initEmptyListener(){
        // fun feature, just for fun, need better implementation
        DocumentReference userDocRef = communicator.getUserDocRef();
        userDocRef.collection("MoodEvents")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (queryDocumentSnapshots != null){
                            if (queryDocumentSnapshots.isEmpty()){
                                if ((moodHistoryIsEmpty == null)||(!moodHistoryIsEmpty)){
                                    moodHistoryIsEmpty = true;
                                    if (currentScreenPointer == MOOD_HISTORY_SCREEN){
                                        // trigger a instant lock
                                        toMoodHistoryEmptyFragment();
                                    }
                                }
                            }else{
                                if ((moodHistoryIsEmpty == null)||(moodHistoryIsEmpty)){
                                    moodHistoryIsEmpty = false;
                                    if (currentScreenPointer == MOOD_HISTORY_SCREEN) {
                                        // trigger un lock
                                        toMoodHistory();
                                    }
                                }
                            }
                        }
                    }
                });
        userDocRef.collection("followingMoodList")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (queryDocumentSnapshots != null){
                            if (queryDocumentSnapshots.isEmpty()){
                                if ((moodFollowingIsEmpty == null)||(!moodFollowingIsEmpty)){
                                    moodFollowingIsEmpty = true;
                                    if (currentScreenPointer == FOLLOWING_SCREEN){
                                        // trigger a instant lock
                                        toFollowingEmptyFragment();
                                    }
                                }
                            }else{
                                if ((moodFollowingIsEmpty == null)||(moodFollowingIsEmpty)){
                                    moodFollowingIsEmpty = false;
                                    if (currentScreenPointer == FOLLOWING_SCREEN) {
                                        // trigger un lock
                                        toFollowing();
                                    }
                                }
                            }
                        }
                    }
                });
    }


}

