package com.example.moodswing;

import android.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.moodswing.Fragments.HomeFragment;
import com.example.moodswing.customDataTypes.FirestoreUserDocCommunicator;
import com.google.firebase.auth.FirebaseAuth;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MainactivityTest {
    Solo solo;
    private FirebaseAuth mAuth;
    @Rule
    public IntentsTestRule<MainActivity> rule =
            new IntentsTestRule<>(MainActivity.class);
    @Before
    public void setUp() throws Exception{
        mAuth = FirebaseAuth.getInstance();
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
        if (mAuth.getCurrentUser()==null){
            mAuth.signInWithEmailAndPassword("test@test.com", "123456");}

        //   solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        //   solo.enterText((EditText) solo.getView(R.id.userEmailField), "test@test.com");
        //   solo.enterText((EditText) solo.getView(R.id.passField), "123456");
        //  View view = solo.getCurrentActivity().findViewById(R.id.loginComfirmBtn);
        //  solo.clickOnView(view);

    }

    @Test
    public void CheckAddMood(){
        solo.clickOnView(solo.getCurrentActivity().findViewById(R.id.home_add));
        solo.assertCurrentActivity("Not in AddMoodActivity", MainActivity.class);
        
    }

}
