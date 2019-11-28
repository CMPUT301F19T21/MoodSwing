package com.example.moodswing;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moodswing.Fragments.ImageFragment;
import com.example.moodswing.customDataTypes.DateJar;
import com.example.moodswing.customDataTypes.FirestoreUserDocCommunicator;
import com.example.moodswing.customDataTypes.MoodEvent;
import com.example.moodswing.customDataTypes.MoodEventUtility;
import com.example.moodswing.customDataTypes.SelectMoodAdapter;
import com.example.moodswing.customDataTypes.TimeJar;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

// The screen for adding a new mood, accessed from the Home screen.

/**
 * The screen for adding a new mood, accessed from the Home screen.
 */
public class NewMoodActivity extends AppCompatActivity {
    private static final int LOCATION_REQUEST_CODE = 1;
    private FloatingActionButton confirmButton;
    private ImageView addNewImageButton;
    private EditText reasonEditText;
    private TextView dateTextView;
    private TextView timeTextView;
    private TextView geoLocationText;
    private FloatingActionButton locationButton;
    private Location currentLocation;
    private FloatingActionButton social_aloneBtn;
    private FloatingActionButton social_oneBtn;
    private FloatingActionButton social_twoMoreBtn;

    private RecyclerView moodSelectList;
    private RecyclerView.LayoutManager recyclerViewLayoutManager;
    private SelectMoodAdapter moodSelectAdapter;

    private FirestoreUserDocCommunicator communicator;
    private MoodEvent moodEvent;

    private FusedLocationProviderClient fusedLocationProviderClient;

    private Double latitude, longitude;

    private boolean ifLocationEnabled;
    private boolean ifLocationGranted;
    private Integer socialSituation;

    private String currentPhotoPath;
    private Uri uploadImage;

    /**
     * All the fields for creating a new mood are created and the current date/time are generated.
     * All redirect button functionality is handled here too.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_mood);

        // find view
        confirmButton = findViewById(R.id.add_confirm);
        addNewImageButton = findViewById(R.id.add_newImage);
        reasonEditText = findViewById(R.id.reason_EditView);
        dateTextView = findViewById(R.id.add_date);
        timeTextView = findViewById(R.id.add_time);
        moodSelectList = findViewById(R.id.moodSelect_recycler);
        locationButton = findViewById(R.id.moodhistory_locationButton);
        geoLocationText = findViewById(R.id.add_geoLocation);

        social_aloneBtn = findViewById(R.id.addMood_aloneBtn);
        social_oneBtn = findViewById(R.id.addMood_oneAnotherBtn);
        social_twoMoreBtn = findViewById(R.id.addMood_twoMoreBtn);


        // recyclerView
        recyclerViewLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        moodSelectAdapter = new SelectMoodAdapter();
        moodSelectList.setLayoutManager(recyclerViewLayoutManager);
        moodSelectList.setAdapter(moodSelectAdapter);

        // init communicator
        communicator = FirestoreUserDocCommunicator.getInstance();
        moodEvent = new MoodEvent();
        // init location
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        ifLocationEnabled = false;
        geoLocationText.setText("Location Off");


        // Set up time and date
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // set up current date and time
                                Calendar calendar = Calendar.getInstance();

                                // set date and time
                                int day = calendar.get(Calendar.DAY_OF_MONTH);
                                int month = calendar.get(Calendar.MONTH);
                                int year = calendar.get(Calendar.YEAR);
                                int hr = calendar.get(Calendar.HOUR_OF_DAY);
                                int min = calendar.get(Calendar.MINUTE);
                                Long UTC = calendar.getTimeInMillis();


                                DateJar date = new DateJar(year,month,day);
                                TimeJar time = new TimeJar(hr,min);


                                moodEvent.setDate(date);
                                moodEvent.setTime(time);
                                moodEvent.setTimeStamp(UTC);

                                // set date and time for display
                                dateTextView.setText(MoodEventUtility.getDateStr(date));
                                timeTextView.setText(MoodEventUtility.getTimeStr(UTC));
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        thread.start();

        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ifLocationEnabled){
                    // turn off
                    locationBtnPop();
                }else{
                    locationBtnPress();
                    }
                }
        });

        setSocialSituationBtns();
        PickImage();
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (moodSelectAdapter.getSelectedMoodType() != null) {
                    // do upload
                    String moodId = communicator.generateMoodID();
                    moodEvent.setUniqueID(moodId);
                    moodEvent.setMoodType(moodSelectAdapter.getSelectedMoodType());
                    moodEvent.setSocialSituation(socialSituation);
                    if (reasonEditText.getText().toString().isEmpty()){
                        moodEvent.setReason(null);
                    }else{
                        if (reasonEditText.getText().toString().length() > 20) {
                            Toast.makeText(getApplicationContext(), "More than 20 characters!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        String[] splitStr = reasonEditText.getText().toString().trim().split("\\s+");
                        if (splitStr.length > 3) {
                            Toast.makeText(getApplicationContext(),"More than 3 words!",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        moodEvent.setReason(reasonEditText.getText().toString());
                    }
                    if (ifLocationEnabled) {
                        if (currentLocation != null){
                            moodEvent.setLatitude(currentLocation.getLatitude());
                            moodEvent.setLongitude(currentLocation.getLongitude());
                        }
                    }
                    else {
                        moodEvent.setLatitude(null);
                        moodEvent.setLongitude(null);
                    }
                    if (uploadImage != null){
                        communicator.addPhoto(moodEvent,uploadImage,null);
                    }
                    communicator.addMoodEvent(moodEvent);

                    finish();
                }else{
                    // prompt user to select a mood
                }
            }
        });
    }

    private void PickImage(){
        addNewImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageFragment imageFragment = new ImageFragment();
                Bundle args = new Bundle();
                args.putString("activity","New");
                imageFragment.setArguments(args);
                imageFragment.show(getSupportFragmentManager(),"image");
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
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case 0:
                    //show image comes form gallery
                    Uri selectedImage = data.getData();
                    if (selectedImage != null)
                        uploadImage = selectedImage;
                    addNewImageButton.setImageURI(selectedImage);
                    break;
                case 1:
                    // Showing the image from camera
                    addNewImageButton.setImageURI(uploadImage);

                    break;
            }
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

    private void setSocialSituationBtns(){
        socialSituation = 0;

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

    /**
     * This method gets the latitude and longitude that the google maps API found, and
     * assigns the value to our fields
     */
    private void fetchCurrentLocation(){
        fusedLocationProviderClient
                .getLastLocation()
                .addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()){
                            Location location = task.getResult();
                            if (location == null){
                                locationBtnPop();
                            }else{
                                currentLocation = location;
                                getAddress();
                            }
                        }else {
                            // display error msg
                            locationBtnPop();
                        }
                    }
                });
    }

    private void getAddress(){
        communicator.getAsynchronousTask()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        updateLocationStr();
                    }
                });
    }

    private void updateLocationStr(){
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        if (currentLocation != null){
            try {
                List<Address> firstAddressList = geocoder.getFromLocation(currentLocation.getLatitude(),currentLocation.getLongitude(),1);
                if (firstAddressList != null){
                    if (firstAddressList.isEmpty()){
                        // error
                    }else{
                        //
                        Address address = firstAddressList.get(0);
                        String thoroughfare = address.getThoroughfare();
                        if (thoroughfare == null){
                            geoLocationText.setText("nowhere!");
                        }else{
                            geoLocationText.setText(thoroughfare);
                        }
                    }
                }else {
                    // error
                }
            } catch (Exception e) {
                // display error msg
                e.printStackTrace();
            }
        }else{
            //
        }
    }

    private void locationBtnPop() {
        ifLocationEnabled = false;
        locationButton.setCompatElevation(12f);
        locationButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.color_button_lightGrey)));
        geoLocationText.setText("Location off");
    }


    private void locationBtnPress() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ifLocationEnabled = false;
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
        } else {
            // has location permission
            ifLocationEnabled = true;
            locationButton.setCompatElevation(0f);
            locationButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.color_button_lightGrey_pressed)));
            fetchCurrentLocation();
        }
    }

    /**
     *a utility method  for permission, see fetchLastLocation for functionality
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    ifLocationEnabled = true;
                    locationButton.setCompatElevation(0f);
                    locationButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.color_button_lightGrey_pressed)));
                    fetchCurrentLocation();
                }else{
                    // prompt user cannot fetch location
                    ifLocationEnabled = false;
                }
                break;
        }
    }

}