package com.example.moodswing;

import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.rule.IntentsTestRule;

import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.anything;

public class AddMoodUITest {

    @Rule
    public IntentsTestRule<NewMoodActivity> intentsTestRule =
            new IntentsTestRule<>(NewMoodActivity.class);


    @Test
    public void testMoodSelect() {
        onView(withId(R.id.moodSelect_recycler))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.moodSelect_recycler))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
        onView(withId(R.id.moodSelect_recycler))
                .perform(RecyclerViewActions.actionOnItemAtPosition(2, click()));
        onView(withId(R.id.moodSelect_recycler))
                .perform(RecyclerViewActions.actionOnItemAtPosition(3, click()));






    }
}
