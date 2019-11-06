package com.example.moodswing;

import android.app.Activity;
import android.app.FragmentManager;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.espresso.matcher.BoundedMatcher;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import com.example.moodswing.customDataTypes.DateJar;
import com.example.moodswing.customDataTypes.FirestoreUserDocCommunicator;
import com.example.moodswing.customDataTypes.MoodEventUtility;
import com.example.moodswing.customDataTypes.TimeJar;


import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;

import static androidx.core.util.Preconditions.checkNotNull;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.swipeLeft;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

//Test assume current user is exist now
@RunWith(AndroidJUnit4ClassRunner.class)
public class MainactivityTest {
    private FirestoreUserDocCommunicator communicator;
    @Rule
    public IntentsTestRule<MainActivity> rule =
            new IntentsTestRule<>(MainActivity.class);
    @Before
    public void setUp() throws Exception{
        //Delete all mood
        communicator = FirestoreUserDocCommunicator.getInstance();
    }

    @Test
    public void CheckAddMood() throws InterruptedException {
        // check add mood activity
        onView(withId(R.id.home_add))
                .perform(click());
        intended(hasComponent(NewMoodActivity.class.getName()));
        onView(withId(R.id.moodSelect_recycler)).perform(
                RecyclerViewActions.actionOnItemAtPosition(2, click()));
        onView(withId(R.id.reason_EditView))
                .perform(typeText("Test Mood"),closeSoftKeyboard());
        onView(withId(R.id.add_confirm))
        .perform(click());
        // check if item correct shows

    }

    @Test
    public void CheckMoodDetail() throws InterruptedException {
        //add a mood to test
        onView(withId(R.id.home_add))
                .perform(click());
        intended(hasComponent(NewMoodActivity.class.getName()));
        onView(withId(R.id.moodSelect_recycler)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.reason_EditView))
                .perform(typeText("Test Mood"),closeSoftKeyboard());
        onView(withId(R.id.add_confirm))
                .perform(click());
        // Get current time
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        int hr = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);
        DateJar date = new DateJar(year,month,day);
        TimeJar time = new TimeJar(hr,min);
        // check if data matched
        onView(withId(R.id.mood_list)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.moodDetail_moodText)).check(matches(withText("HAPPY")));
        onView(withId(R.id.moodDetail_timeText)).check(matches(withText(MoodEventUtility.getTimeStr(time))));
        onView(withId(R.id.moodDetail_dateText)).check(matches(withText(MoodEventUtility.getDateStr(date))));
        onView(withId(R.id.detailedView_reasonText)).check(matches(withText("\"Test Mood\"")));
        // check if click back succeed
        onView(withId(R.id.detailedView_back)).perform(click());

    }

    @Test
    public void CheckEditMood(){
        // add a new mood to test
        onView(withId(R.id.home_add))
                .perform(click());
        intended(hasComponent(NewMoodActivity.class.getName()));
        onView(withId(R.id.moodSelect_recycler)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.reason_EditView))
                .perform(typeText("Test Mood"),closeSoftKeyboard());
        onView(withId(R.id.add_confirm))
                .perform(click());
        // go to edit mood screen
        onView(withId(R.id.mood_list)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.detailedView_edit))
                .perform(click());
        intended(hasComponent(EditMoodActivity.class.getName()));
        // editing mood
        onView(withId(R.id.moodSelect_recycler)).perform(
                RecyclerViewActions.actionOnItemAtPosition(1, click()));
        onView(withId(R.id.reason_EditView))
                .perform(typeText("Edited Test Mood"),closeSoftKeyboard());
        onView(withId(R.id.add_confirm)).perform(click());
        //Check if Mood being edited
        onView(withId(R.id.moodDetail_moodText)).check(matches(withText("SAD")));
        onView(withId(R.id.detailedView_reasonText)).check(matches(withText("\"Edited Test Mood\"")));
    }

    @Test
    public void CheckDeleteMood(){
        // add a new mood to test
        onView(withId(R.id.home_add))
                .perform(click());
        intended(hasComponent(NewMoodActivity.class.getName()));
        onView(withId(R.id.moodSelect_recycler)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.reason_EditView))
                .perform(typeText("Test Mood"),closeSoftKeyboard());
        onView(withId(R.id.add_confirm))
                .perform(click());
        //
        Integer oldSize = communicator.getMoodEvents().size();
        onView(withId(R.id.home_delete))
                .perform(click());
        onView(withId(R.id.mood_list)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, swipeLeft()));
        Integer newSize = communicator.getMoodEvents().size();
        assertTrue(newSize==(oldSize-1));
    }

    @Test
    public void CheckFollowing(){
        onView(withId(R.id.nav_followingBtn))
                .perform(click());
        //check if in the following screen shows

    }

    @Test
    public void CheckProfile(){
        onView(withId(R.id.nav_profile))
                .perform(click());
        //check if profile shows

    }

    public static Matcher<View> atPosition(final int position, @NonNull final Matcher<View> itemMatcher) {
        checkNotNull(itemMatcher);
        return new BoundedMatcher<View, RecyclerView>(RecyclerView.class) {
            @Override
            public void describeTo(org.hamcrest.Description description) {
                description.appendText("has item at position " + position + ": ");
                itemMatcher.describeTo(description);
            }
            @Override
            protected boolean matchesSafely(final RecyclerView view) {
                RecyclerView.Adapter adapter = view.getAdapter();
                int type = adapter.getItemViewType(position);
                RecyclerView.ViewHolder holder = adapter.createViewHolder(view, type);
                adapter.onBindViewHolder(holder, position);
                if (itemMatcher.matches(holder.itemView)) {
                    return true;
                }
                return false;
            }

                //return itemMatcher.matches(holder.itemView);
        };
    }

}
