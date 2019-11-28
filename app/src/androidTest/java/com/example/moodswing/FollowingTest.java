package com.example.moodswing;

import android.util.Log;

import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.rule.ActivityTestRule;

import com.example.moodswing.customDataTypes.FirestoreUserDocCommunicator;
import com.google.firebase.auth.FirebaseAuth;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static junit.framework.TestCase.assertTrue;


public class FollowingTest {

    private FirestoreUserDocCommunicator communicator;
    private FirebaseAuth mAuth;

    private String followera = "followertesta@mail.com";
    private String followeraUsername = "follower";
    private String followerb = "followertestb@mail.com";
    private String followerbUsername  = "followerb";
    private String password = "123456";


    @Rule
    public ActivityTestRule<LoginActivity> rule =
            new ActivityTestRule<>(LoginActivity.class);

    /**
     * Runs before all tests
     * Log out current account and login to test account
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        Intents.init();
        // login to test account
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            onView(withId(R.id.nav_profile)).perform(click());
            onView(withId(R.id.profile_LogOut)).perform(click());
        }
        onView(withId(R.id.userEmailField))
                .perform(typeText(followera), closeSoftKeyboard());
        onView(withId(R.id.passField))
                .perform(typeText(password), closeSoftKeyboard());
        onView(withId(R.id.loginComfirmBtn)).perform(click());
        Thread.sleep(2000);
        communicator = FirestoreUserDocCommunicator.getInstance();
    }


    @Test
    public void CheckFollowingAddReceive() throws InterruptedException{
        onView(withId(R.id.nav_followingBtn))
                .perform(click());
        //check if in the following screen shows
        try {
            onView(withId(R.id.following_list)).check(matches(isDisplayed()));//check if in the FollowingFragment
            onView(withId(R.id.following_management)).perform(click());
        }
        catch(NoMatchingViewException e) {
            onView(withText("NO ONE IS SHARING THEIR MOOD CLICK TO FOLLOW PEOPLE")).check(matches(isDisplayed()));
            onView(withId(R.id.emptyNotifcation_Btn)).perform(click());
        }

        //user a sends request to user B to follow
        onView(withId(R.id.managment_following)).check(matches(isDisplayed()));//check if in the FollowingFragment
        onView(withId(R.id.request_button))
                .perform(click());
        onView(withId(R.id.requestFrag_requestEnter))
                .perform(typeText(followerbUsername),closeSoftKeyboard());
        onView(withId(R.id.requestFrag_confirm))
                .perform(click());
        //espresso checking before screen comes up here, sleep to slow it down
        Thread.sleep(2000);
        onView(withId(R.id.request_backBtn))
                .perform(click());
        Thread.sleep(2000);
        onView(withId(R.id.nav_profile)).perform(click());
        Thread.sleep(2000);
        onView(withId(R.id.profile_LogOut)).perform(click());

        //user B checks the request and accepts it
        onView(withId(R.id.userEmailField))
                .perform(typeText(followerb), closeSoftKeyboard());
        onView(withId(R.id.passField))
                .perform(typeText(password), closeSoftKeyboard());
        onView(withId(R.id.loginComfirmBtn)).perform(click());

        Thread.sleep(2000);

        onView(withId(R.id.nav_followingBtn)).perform(click());
        try {
            onView(withId(R.id.following_list)).check(matches(isDisplayed()));//check if in the FollowingFragment
            onView(withId(R.id.following_management)).perform(click());
        }
        catch(NoMatchingViewException e) {
            onView(withText("NO ONE IS SHARING THEIR MOOD CLICK TO FOLLOW PEOPLE")).check(matches(isDisplayed()));
            onView(withId(R.id.emptyNotifcation_Btn)).perform(click());
        }
        //The sending user's username will be on the screen, otherwise error
        onView(withId(R.id.management_request))
                .perform(RecyclerViewActions.actionOnItem(hasDescendant(withText(followeraUsername)), click()));
        onView(withId(R.id.checkrequest_confirm)).perform(click());



        //Checking the user is now following, then deleting so the test can be run in the future
        onView(withId(R.id.request_backBtn)).perform(click());
        Thread.sleep(2000);
        onView(withId(R.id.nav_profile)).perform(click());
        Thread.sleep(2000); // Without sleep, espresso won't wait for the view to change, not sure why
        onView(withId(R.id.profile_LogOut)).perform(click());


        onView(withId(R.id.userEmailField))
                .perform(typeText(followera),closeSoftKeyboard());
        onView(withId(R.id.passField))
                .perform(typeText(password),closeSoftKeyboard());
        onView(withId(R.id.loginComfirmBtn)).perform(click());
        Thread.sleep(2000);
        onView(withId(R.id.nav_followingBtn)).perform(click());
        try {
            onView(withId(R.id.following_list)).check(matches(isDisplayed()));//check if in the FollowingFragment
            onView(withId(R.id.following_management)).perform(click());
        }
        catch(NoMatchingViewException e) {
            onView(withText("NO ONE IS SHARING THEIR MOOD CLICK TO FOLLOW PEOPLE")).check(matches(isDisplayed()));
            onView(withId(R.id.emptyNotifcation_Btn)).perform(click());
        }

        onView(withId(R.id.managment_following))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.unfollowFrag_confirm)).perform(click());

    }




    @After
    public void cleanUp(){
        Intents.release();
    }


}
