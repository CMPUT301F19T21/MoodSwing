package com.example.moodswing;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.platform.app.InstrumentationRegistry;

import com.google.api.Authentication;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

public class LoginTest {

    private String loginEmail;
    private String password;

    @Rule
    public IntentsTestRule<LoginActivity> intentsTestRule =
            new IntentsTestRule<>(LoginActivity.class);

    @Before
    public void initCredentials() {
        loginEmail = "baijoo@ualberta.ca";
        password = "123456";

    }

    @Test
    public void enterUsernameTest() {
        onView(withId(R.id.userEmailField))
                .perform(typeText(loginEmail), closeSoftKeyboard());
        onView(withId(R.id.userEmailField))
                .check(matches(withText(loginEmail)));

    }

    @Test
    public void enterPasswordTest() {
        onView(withId(R.id.passField))
                .perform(typeText(password), closeSoftKeyboard());
        onView(withId(R.id.passField))
                .check(matches(withText(password)));
    }




    @Test
    public void validateIntentSentToPackage() {

    }


    @Test
    public void testLogin() {

    }


}
