package com.example.moodswing;


import android.content.ComponentName;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.test.espresso.intent.rule.IntentsTestRule;

import com.example.moodswing.customDataTypes.FirestoreUserDocCommunicator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.InstrumentationRegistry.getTargetContext;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;

/**
 * Test class for RegisterActivity. All the UI tests are written here. Espresso test framework is
 * used
 */
public class Registertest {

    private String loginEmail;
    private String password;
    private String username;
    private FirestoreUserDocCommunicator communicator;
    private FirebaseFirestore db;

    @Rule
    public IntentsTestRule<RegisterActivity> intentsTestRule =
            new IntentsTestRule<>(RegisterActivity.class);

    @Before
    public void initCredentials() {
        loginEmail = "testmail@mail.com";
        password = "123456";
        username = "testUser";
        communicator = FirestoreUserDocCommunicator.getInstance();
        db = FirebaseFirestore.getInstance();
    }


    @After
    public void delete() {

    }

    @Test
    public void RegisterUser() {
        onView(withId(R.id.regEmail))
                .perform(typeText(loginEmail), closeSoftKeyboard());
        onView(withId(R.id.regUsername))
                .perform(typeText(username), closeSoftKeyboard());
        onView(withId(R.id.regPassword))
                .perform(typeText(password), closeSoftKeyboard());
        onView(withId(R.id.regButton))
                .perform(click());




    }



}
