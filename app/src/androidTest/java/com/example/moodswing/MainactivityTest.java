package com.example.moodswing;

import android.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.moodswing.Fragments.HomeFragment;
import com.example.moodswing.customDataTypes.FirestoreUserDocCommunicator;
import com.google.firebase.auth.FirebaseAuth;


import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;
@RunWith(AndroidJUnit4ClassRunner.class)
public class MainactivityTest {
    private FirebaseAuth mAuth;
    @Rule
    public IntentsTestRule<MainActivity> rule =
            new IntentsTestRule<>(MainActivity.class);
    @Before
    public void setUp() throws Exception{
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser()==null){
            mAuth.signInWithEmailAndPassword("test@test.com", "123456");}
    }

    @Test
    public void CheckAddMood(){
        onView(withId(R.id.home_add))
                .perform(click());
        intended(hasComponent(NewMoodActivity.class.getName()));
        onView(withId(R.id.moodSelect_recycler)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.reason_EditView))
                .perform(typeText("Test Mood"),closeSoftKeyboard());
        onView(withId(R.id.add_confirm))
        .perform(click());
        intended(hasComponent(MainActivity.class.getName()));

    }

    @Test
    public void CheckMoodDetail(){
        onView(withId(R.id.mood_list)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, click()));
    }

    @Test
    public void CheckEditMood(){
        onView(withId(R.id.mood_list)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.detailedView_edit))
                .perform(click());
        intended(hasComponent(EditMoodActivity.class.getName()));
    }

    @Test
    public void CheckDeleteMood(){
        //
    }

}
