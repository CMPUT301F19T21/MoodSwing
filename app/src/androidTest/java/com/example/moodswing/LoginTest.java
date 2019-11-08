package com.example.moodswing;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.moodswing.customDataTypes.FirestoreUserDocCommunicator;
import com.google.api.Authentication;
import com.google.firebase.auth.FirebaseAuth;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;


//Will fail if the user is already signed in before running the test and firestore doesn't
// handle the call FirestoreUserDocCommunicator.destroy() in time. All other cases work. possible
// fix is callback
public class LoginTest {

    private String loginEmail;
    private String password;
    private FirestoreUserDocCommunicator communicator;
    private FirebaseAuth mAuth;

    @Rule
    public ActivityTestRule<LoginActivity> intentsTestRule =
            new ActivityTestRule<>(LoginActivity.class);

    /**
     * log out if currently login
     */
    @Before
    public void initCredentials() {
        loginEmail = "test@mail.com";
        password = "123456";
        Intents.init();
        // login to test account
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser()!=null){
            onView(withId(R.id.nav_profile)).perform(click());
            onView(withId(R.id.profile_LogOut)).perform(click());}



    }

    /**
     * logout current account
     */
    @After
    public void logout() {
        mAuth = FirebaseAuth.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FirestoreUserDocCommunicator.destroy();
        Intents.release();

    }


    /**
     * test login function
     * test if activity correct switch
     */

    @Test
    public void testLogin() {
        onView(withId(R.id.userEmailField))
                .perform(typeText(loginEmail), closeSoftKeyboard());
        onView(withId(R.id.userEmailField))
                .check(matches(withText(loginEmail)));

        onView(withId(R.id.passField))
                .perform(typeText(password), closeSoftKeyboard());
        onView(withId(R.id.passField))
                .check(matches(withText(password)));
        onView(withId(R.id.loginComfirmBtn)).perform(click());

        https://stackoverflow.com/questions/2663419/sleep-from-main-thread-is-throwing-interruptedexception
        try {
            Thread.sleep(1500);
        } catch(InterruptedException e) {
            Log.d("Loginerror", "interrupted thread.sleep");
        }
        intended(hasComponent(MainActivity.class.getName()));






    }


}
