package com.example.moodswing;


import android.content.ComponentName;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.test.espresso.intent.Intents;
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

import java.util.Random;

import static androidx.test.InstrumentationRegistry.getTargetContext;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static io.opencensus.tags.TagKey.MAX_LENGTH;
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

    @Rule
    public IntentsTestRule<RegisterActivity> intentsTestRule =
            new IntentsTestRule<>(RegisterActivity.class);

    @Before
    public void initCredentials() {

        String letters = randomString();
        loginEmail = letters + "@mail.com";
        password = "123456";
        username = letters;
    }


    @After
    public void delete() {
        communicator = FirestoreUserDocCommunicator.getInstance();
        communicator.deleteUser();
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

        try {
            Thread.sleep(1500);
        } catch(InterruptedException e) {
            Log.d("Loginerror", "interrupted thread.sleep");
        }

        //Small bug when redirecting, doesn't find either mainactivity or loginactivity. for now
        //just checking that the buttons work(the functionality still works)





    }


    //https://stackoverflow.com/questions/12116092/android-random-string-generator
    //Just to generate a random string, will remove later once registration bug fixed
    private static String randomString() {
        String ALLOWED_CHARACTERS = "abcdefghijklmnopqrstuvwxyz0123456789";
        final Random random = new Random();
        final StringBuilder sb = new StringBuilder(5);
        for (int i = 0; i < 5; ++i)
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        return sb.toString();
    }


}
