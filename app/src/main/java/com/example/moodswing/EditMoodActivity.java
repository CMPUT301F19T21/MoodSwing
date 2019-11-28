package com.example.moodswing;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moodswing.Fragments.ImageFragment;
import com.example.moodswing.customDataTypes.FirestoreUserDocCommunicator;
import com.example.moodswing.customDataTypes.MoodEvent;
import com.example.moodswing.customDataTypes.SelectMoodAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
    private ImageView editImage;

    private String currentPhotoPath;
    private String imageId;
    private Uri uploadImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_mood);
        communicator = FirestoreUserDocCommunicator.getInstance();
        Intent moodIntent = getIntent();
        int position = moodIntent.getIntExtra("position",-1);
        moodEvent = communicator.getMoodEvent(position);
        imageId = moodEvent.getImageId();
        // find view
        confirmButton = findViewById(R.id.editMood_add_confirm);
        closeBtn = findViewById(R.id.editMood_close);
        reasonEditText = findViewById(R.id.editMood_reason_EditView);
        editImage = findViewById(R.id.editMood_add_newImage);
        moodSelectList = findViewById(R.id.editMood_moodSelect_recycler);


        // recyclerView
        recyclerViewLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        moodSelectAdapter = new SelectMoodAdapter(moodEvent.getMoodType());
        moodSelectList.setLayoutManager(recyclerViewLayoutManager);
        moodSelectList.setAdapter(moodSelectAdapter);

        social_aloneBtn = findViewById(R.id.editMood_aloneBtn);
        social_oneBtn = findViewById(R.id.editMood_oneAnotherBtn);
        social_twoMoreBtn = findViewById(R.id.editMood_twoMoreBtn);

        communicator.getPhoto(imageId,editImage);

        setSocialSituationBtns();
        setReasonText();

        editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageFragment imageFragment = new ImageFragment();
                Bundle args = new Bundle();
                args.putString("activity","Edit");
                imageFragment.setArguments(args);
                imageFragment.show(getSupportFragmentManager(),"image");
            }
        });

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
                    if (uploadImage != null) {
                        communicator.addPhoto(moodEvent, uploadImage,imageId);
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

    private void setSocialSituationBtns() {
        if (moodEvent.getSocialSituation() != 0) {
            socialSituation = moodEvent.getSocialSituation();
            switch (socialSituation) {
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
        } else {
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
                } else {
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
                } else {
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
                } else {
                    socialSituation = 0;
                    social_twoMoreBtn.setCompatElevation(12f);
                    social_twoMoreBtn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.color_button_lightGrey)));
                }
            }
        });
    }

    public void pickFromGallery(){
        Intent intent=new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
        startActivityForResult(intent,0);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode){
                case 0:
                    //show image comes form gallery
                    Uri selectedImage = data.getData();
                    if (selectedImage != null)
                        uploadImage =  selectedImage;
                    editImage.setImageURI(selectedImage);
                    break;
                case 1:
                    // Showing the image from camera
                    editImage.setImageURI(uploadImage);

                    break;
            }
    }



    public void takeimage() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.d("error", "failed to create photo file");
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.moodswing.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, 1);
                uploadImage = photoURI;
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }
}
