package com.example.moodswing;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moodswing.customDataTypes.FirestoreUserDocCommunicator;
import com.example.moodswing.customDataTypes.MoodEvent;
import com.example.moodswing.customDataTypes.SelectMoodAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

// import com.example.moodswing.customDataTypes.AddMoodAdapter;

//The edit mood screen, contains all the functionality for editing an existing mood
/**
 * The screen for edit a mood, accessed from the mood detail screen.
 */
// Some restrictions on fields to be completed, and photograph
public class EditMoodActivity extends AppCompatActivity {

    FirestoreUserDocCommunicator communicator;
    MoodEvent moodEvent;

    private FloatingActionButton confirmButton;
    private FloatingActionButton closeBtn;
    private EditText reasonEditText;

    private RecyclerView moodSelectList;
    private RecyclerView.LayoutManager recyclerViewLayoutManager;
    private SelectMoodAdapter moodSelectAdapter;

    private FloatingActionButton social_aloneBtn;
    private FloatingActionButton social_oneBtn;
    private FloatingActionButton social_twoMoreBtn;
    private Integer socialSituation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_mood);
        communicator = FirestoreUserDocCommunicator.getInstance();
        Intent moodIntent = getIntent();
        int position = moodIntent.getIntExtra("position",-1);
        moodEvent = communicator.getMoodEvent(position);

        // find view
        confirmButton = findViewById(R.id.editMood_add_confirm);
        closeBtn = findViewById(R.id.editMood_close);
        reasonEditText = findViewById(R.id.editMood_reason_EditView);

        moodSelectList = findViewById(R.id.editMood_moodSelect_recycler);


        // recyclerView
        recyclerViewLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        moodSelectAdapter = new SelectMoodAdapter(moodEvent.getMoodType());
        moodSelectList.setLayoutManager(recyclerViewLayoutManager);
        moodSelectList.setAdapter(moodSelectAdapter);

        social_aloneBtn = findViewById(R.id.editMood_aloneBtn);
        social_oneBtn = findViewById(R.id.editMood_oneAnotherBtn);
        social_twoMoreBtn = findViewById(R.id.editMood_twoMoreBtn);

        setSocialSituationBtns();
        setReasonText();

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (moodSelectAdapter.getSelectedMoodType() != null) {
                    moodEvent.setMoodType(moodSelectAdapter.getSelectedMoodType());
                    moodEvent.setSocialSituation(socialSituation);

                    if (reasonEditText.getText().toString().isEmpty()){
                        moodEvent.setReason(null);
                    }else{
                        moodEvent.setReason(reasonEditText.getText().toString());
                    }
                    communicator.updateMoodEvent(moodEvent);
                    finish();
                }else{
                    // prompt user to select a mood
                }
            }
        });

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setReasonText(){
        if (moodEvent.getReason() != null){
            reasonEditText.setText(moodEvent.getReason());
        }
    }

    private void setSocialSituationBtns(){
        if (moodEvent.getSocialSituation() != 0){
            socialSituation = moodEvent.getSocialSituation();
            switch (socialSituation){
                case 1:
                    social_aloneBtn.setCompatElevation(0f);
                    social_aloneBtn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.color_button_lightGrey_pressed)));
                    break;
                case 2:
                    social_oneBtn.setCompatElevation(0f);
                    social_oneBtn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.color_button_lightGrey_pressed)));
                    break;
                case 3:
                    social_twoMoreBtn.setCompatElevation(0f);
                    social_twoMoreBtn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.color_button_lightGrey_pressed)));
                    break;
            }
        }else{
            socialSituation = 0;
        }


        social_aloneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (socialSituation != 1) {
                    // press this button
                    socialSituation = 1;
                    social_aloneBtn.setCompatElevation(0f);
                    social_aloneBtn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.color_button_lightGrey_pressed)));

                    // unpress other button
                    social_oneBtn.setCompatElevation(12f);
                    social_oneBtn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.color_button_lightGrey)));
                    social_twoMoreBtn.setCompatElevation(12f);
                    social_twoMoreBtn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.color_button_lightGrey)));
                }else{
                    socialSituation = 0;
                    social_aloneBtn.setCompatElevation(12f);
                    social_aloneBtn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.color_button_lightGrey)));
                }
            }
        });

        social_oneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (socialSituation != 2) {
                    // press this button
                    socialSituation = 2;
                    social_oneBtn.setCompatElevation(0f);
                    social_oneBtn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.color_button_lightGrey_pressed)));

                    // unpress other button
                    social_aloneBtn.setCompatElevation(12f);
                    social_aloneBtn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.color_button_lightGrey)));
                    social_twoMoreBtn.setCompatElevation(12f);
                    social_twoMoreBtn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.color_button_lightGrey)));
                }else{
                    socialSituation = 0;
                    social_oneBtn.setCompatElevation(12f);
                    social_oneBtn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.color_button_lightGrey)));
                }
            }
        });

        social_twoMoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (socialSituation != 3) {
                    // press this button
                    socialSituation = 3;
                    social_twoMoreBtn.setCompatElevation(0f);
                    social_twoMoreBtn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.color_button_lightGrey_pressed)));

                    // unpress other button
                    social_oneBtn.setCompatElevation(12f);
                    social_oneBtn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.color_button_lightGrey)));
                    social_aloneBtn.setCompatElevation(12f);
                    social_aloneBtn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.color_button_lightGrey)));
                }else{
                    socialSituation = 0;
                    social_twoMoreBtn.setCompatElevation(12f);
                    social_twoMoreBtn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.color_button_lightGrey)));
                }
            }
        });
    }
}
