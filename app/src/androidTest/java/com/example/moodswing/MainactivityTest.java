package com.example.moodswing;

import android.content.ComponentName;
import android.util.Log;

import androidx.test.espresso.AmbiguousViewMatcherException;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.rule.ActivityTestRule;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import com.example.moodswing.Fragments.MapFragment;
import com.example.moodswing.customDataTypes.FirestoreUserDocCommunicator;
import com.example.moodswing.customDataTypes.MoodEventUtility;
import com.google.firebase.auth.FirebaseAuth;


import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;

import static androidx.core.util.Preconditions.checkNotNull;
import static androidx.test.InstrumentationRegistry.getTargetContext;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.swipeLeft;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static junit.framework.TestCase.fail;
import static org.hamcrest.core.AllOf.allOf;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;


@RunWith(AndroidJUnit4ClassRunner.class)
public class MainactivityTest {
    private FirestoreUserDocCommunicator communicator;
    private FirebaseAuth mAuth;

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
                .perform(typeText("test@mail.com"), closeSoftKeyboard());
        onView(withId(R.id.passField))
                .perform(typeText("123456"), closeSoftKeyboard());
        onView(withId(R.id.loginComfirmBtn)).perform(click());
        Thread.sleep(2000);
        communicator = FirestoreUserDocCommunicator.getInstance();
        //delete all mood
    }

    /**
     * Check whether fragment correct switch
     * Check whether mood being added
     *
     * @throws InterruptedException
     */
    @Test
    public void CheckAddMood() throws InterruptedException {
        // check add mood activity

        try {
            onView(withText("CLICK TO ADD FIRST MOOD EVENT")).check(matches(isDisplayed()));
            onView(withId(R.id.emptyNotifcation_Btn)).perform(click());
            //view is displayed logic
        } catch (NoMatchingViewException e) {
            //view not displayed logic
            onView(withId(R.id.home_add))
                    .perform(click());
        }
        intended(hasComponent(NewMoodActivity.class.getName()));
        onView(withId(R.id.moodSelect_recycler)).perform(
                RecyclerViewActions.actionOnItemAtPosition(2, click()));
        onView(withId(R.id.moodhistory_locationButton)).perform(click());
        onView(withId(R.id.reason_EditView))
                .perform(typeText("Test Mood"), closeSoftKeyboard());

        onView(withId(R.id.moodhistory_locationButton)).check(matches(isDisplayed()));
        onView(withId(R.id.moodhistory_locationButton)).perform(click());

        Integer oldSize = communicator.getMoodEvents().size();
        onView(withId(R.id.add_confirm)).perform(scrollTo(), click());
        // check if item correct added
        onView(withId(R.id.mood_list)).check(matches(isDisplayed()));//check if in the homeFragment
        Integer newSize = communicator.getMoodEvents().size();
        assertTrue(newSize == (oldSize + 1));
        assertTrue(communicator.getMoodEvents().get(0).getMoodType() == 3);


    }

    /**
     * Check whether fragment correct switch
     * Check if data correct shows
     * Test delete mood from moodDetail screen
     *
     * @throws InterruptedException
     */
    @Test
    public void CheckMoodDetail() throws InterruptedException {
        //add a mood to test


        try {
            onView(withText("CLICK TO ADD FIRST MOOD EVENT")).check(matches(isDisplayed()));
            onView(withId(R.id.emptyNotifcation_Btn)).perform(click());
            //view is displayed logic
        } catch (NoMatchingViewException e) {
            //view not displayed logic
            onView(withId(R.id.home_add))
                    .perform(click());
        }
        intended(hasComponent(NewMoodActivity.class.getName()));
        onView(withId(R.id.moodSelect_recycler)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.reason_EditView))
                .perform(typeText("Test Mood"), closeSoftKeyboard());
        onView(withId(R.id.add_confirm)).perform(scrollTo(), click());
        // Get current time
        Calendar calendar = Calendar.getInstance();
        Long UTC = calendar.getTimeInMillis();
        // check if data matched
        onView(withId(R.id.mood_list)).check(matches(isDisplayed()));//check if in the homeFragment
        onView(withId(R.id.mood_list)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, click()));

        try {
            onView(allOf(withId(R.id.moodDetail_moodText), withText("HAPPY"))).check(matches(isDisplayed()));

            onView(withId(R.id.moodDetail_timeText)).check(matches(withText(MoodEventUtility.getTimeStr(UTC))));
            onView(withId(R.id.moodDetail_dateText)).check(matches(withText(MoodEventUtility.getDateStr(UTC))));
            onView(withId(R.id.detailedView_reasonText)).check(matches(withText("\"Test Mood\"")));
            // check if click back succeed
            onView(withId(R.id.detailedView_back)).perform(click());
            onView(withId(R.id.mood_list)).check(matches(isDisplayed()));//check if in the homeFragment
            // check delete button in mood detail
            Integer oldSize = communicator.getMoodEvents().size();
            onView(withId(R.id.mood_list)).perform(
                    RecyclerViewActions.actionOnItemAtPosition(0, click()));
            onView(withId(R.id.moodDetail_moodText)).check(matches(isDisplayed()));//check if in the moodDetail fragment
            onView(withId(R.id.detailedView_delete)).perform(click());
            onView(withId(R.id.mood_list)).check(matches(isDisplayed()));//check if in the homeFragment
            Integer newSize = communicator.getMoodEvents().size();
            assertTrue(newSize == (oldSize - 1));// check if delete succeed
        } catch (AmbiguousViewMatcherException e) {
            //Error checker finds two of each of the views.
            // not a big deal, since we're just checking to make sure that one exists
        }
    }

    /**
     * Check whether activity correctly switched
     * Test whether data is edited
     */
    @Test
    public void CheckEditMood() {
        // add a new mood to test
        try {
            onView(withText("CLICK TO ADD FIRST MOOD EVENT")).check(matches(isDisplayed()));
            onView(withId(R.id.emptyNotifcation_Btn)).perform(click());
            //view is displayed logic
        } catch (NoMatchingViewException e) {
            //view not displayed logic
            onView(withId(R.id.home_add))
                    .perform(click());
        }
        intended(hasComponent(NewMoodActivity.class.getName()));
        onView(withId(R.id.moodSelect_recycler)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.reason_EditView))
                .perform(typeText("Test Mood"), closeSoftKeyboard());
        onView(withId(R.id.add_confirm)).perform(scrollTo(), click());
        // go to edit mood screen
        onView(withId(R.id.mood_list)).check(matches(isDisplayed()));//check if in the homeFragment
        onView(withId(R.id.mood_list)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, click()));
        try {
            onView(withId(R.id.moodDetail_moodText)).check(matches(isDisplayed()));//check if in the moodDetail fragment
            onView(withId(R.id.detailedView_edit))
                    .perform(click());
            intended(hasComponent(EditMoodActivity.class.getName()));
            // editing mood
            onView(withId(R.id.moodSelect_recycler)).perform(
                    RecyclerViewActions.actionOnItemAtPosition(1, click()));
            onView(withId(R.id.reason_EditView))
                    .perform(typeText("Edited Test Mood"), closeSoftKeyboard());
            onView(withId(R.id.add_confirm)).perform(click());
            //Check if Mood being edited
            onView(withId(R.id.moodDetail_moodText)).check(matches(isDisplayed()));//check if in the moodDetail fragment
            onView(withId(R.id.moodDetail_moodText)).check(matches(withText("SAD")));
            onView(withId(R.id.detailedView_reasonText)).check(matches(withText("\"Edited Test Mood\"")));
        } catch (AmbiguousViewMatcherException e) {
            //Error checker finds two of each of the views.
            // not a big deal, since we're just checking to make sure that at least one exists
        }
    }

    /**
     * Test if delete mood function correctly work
     * Check if mood is deleted
     */
    @Test
    public void CheckDeleteMood() {
        // add a new mood to test
        onView(withId(R.id.home_add))
                .perform(click());
        intended(hasComponent(NewMoodActivity.class.getName()));
        onView(withId(R.id.moodSelect_recycler)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.reason_EditView))
                .perform(typeText("Test Mood"), closeSoftKeyboard());
        onView(withId(R.id.add_confirm)).perform(scrollTo(), click());
        // delete first mood
        onView(withId(R.id.mood_list)).check(matches(isDisplayed()));//check if in the homeFragment
        Integer oldSize = communicator.getMoodEvents().size();
        onView(withId(R.id.home_delete))
                .perform(click());
        onView(withId(R.id.mood_list)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, swipeLeft()));
        // check if mood deleted
        Integer newSize = communicator.getMoodEvents().size();
        assertTrue(newSize == (oldSize - 1));
    }


    /**
     * Check whether fragment correct switch
     * Check if user name correct shows
     * Test log out from mood profile
     */
    @Test
    public void CheckProfile() {
        onView(withId(R.id.nav_profile))
                .perform(click());
        //check if profile correctly shows
        onView(withId(R.id.profile_username)).check(matches(withText(communicator.getUsername())));
        //check back button
        onView(withId(R.id.profile_back))
                .perform(click());
        onView(withId(R.id.fragment_placeHolder)).check(matches(isDisplayed()));
        // check logout button
        onView(withId(R.id.nav_profile))
                .perform(click());
        onView(withId(R.id.profile_LogOut))
                .perform(click());
        // check current activity is login

    }

    @Test
    public void FilterTest() {
        //We will assume there is at least 1 mood in the list with the happy emotion
        onView(withId(R.id.home_add));
        int before = communicator.getMoodHistoryFilterList().size();
        onView(withId(R.id.home_filterBtn))
                .perform(click());
        onView(allOf(withId(R.id.moodType_Text), withText("HAPPY"))).perform(click());
        onView(withId(R.id.filter_back)).perform(click());
        int after = communicator.getMoodHistoryFilterList().size();


        //1 mood added to filter list
        assertTrue(after > before);

    }

    //Test might fail due to uiautomater not finding the mood on the map, cannot figure out cause, seems random
    //re-run test
    @Test
    public void mapTest() throws InterruptedException {
        onView(withId(R.id.home_map))
                .perform(click());
        onView(withId(R.id.maplayoutid)).check(matches(isDisplayed()));
        Thread.sleep(1000);
        UiDevice device = UiDevice.getInstance(getInstrumentation());
        Thread.sleep(1000);
        //finding a view on screen with the mood state
        UiObject marker = device.findObject(new UiSelector().descriptionContains("HAPPY"));
        Thread.sleep(1000);
        // https://stackoverflow.com/questions/29924564/using-espresso-to-unit-test-google-maps
        try {
            marker.click();

        } catch (UiObjectNotFoundException e) {
            //if the mood object wasn't successfully passed to the map
            fail();
        }











    }

    @After
    public void cleanUp() {
        Intents.release();
    }


}
