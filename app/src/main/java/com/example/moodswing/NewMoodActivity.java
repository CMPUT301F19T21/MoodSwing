package com.example.moodswing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.location.Location;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import com.example.moodswing.customDataTypes.FirestoreUserDocCommunicator;
import com.example.moodswing.customDataTypes.MoodEvent;
import com.example.moodswing.customDataTypes.MoodEventUtility;
import com.example.moodswing.customDataTypes.SelectMoodAdapter;
import com.example.moodswing.customDataTypes.DateJar;
import com.example.moodswing.customDataTypes.SocialSituationItem;
import com.example.moodswing.customDataTypes.SpinnerAdapter;
import com.example.moodswing.customDataTypes.TimeJar;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class NewMoodActivity extends AppCompatActivity {
////    private ArrayList<SocialSituationItem> mSocialList;
////    private SpinnerAdapter spinnerAdapter;
////    private Spinner socialSituationSpinner;
////    private String socialSitToAdd;

    private FirebaseFirestore db;
    private FloatingActionButton confirmButton;
    private ImageView addNewImageButton;
    private EditText reasonEditText;
    private TextView dateTextView;
    private TextView timeTextView;
    private FloatingActionButton locationButton;
    private Location currentLocation;

    private RecyclerView moodSelectList;
    private RecyclerView.LayoutManager recyclerViewLayoutManager;
    private SelectMoodAdapter moodSelectAdapter;

    private FirestoreUserDocCommunicator communicator;
    private MoodEvent moodEvent;

    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;
    private double latitude, longitude;

    private boolean ifLocationEnabled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_mood);

        // find view
        confirmButton = findViewById(R.id.add_confirm);
        addNewImageButton = findViewById(R.id.add_newImage);
        reasonEditText = findViewById(R.id.reason_EditView);
        dateTextView = findViewById(R.id.add_date);
        timeTextView = findViewById(R.id.add_time);
        moodSelectList = findViewById(R.id.moodSelect_recycler);
        locationButton = findViewById(R.id.moodhistory_locationButton);

        // recyclerView
        recyclerViewLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        moodSelectAdapter = new SelectMoodAdapter();
        moodSelectList.setLayoutManager(recyclerViewLayoutManager);
        moodSelectList.setAdapter(moodSelectAdapter);

        // init communicator
        communicator = FirestoreUserDocCommunicator.getInstance();
        moodEvent = new MoodEvent();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        ifLocationEnabled = false;

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
        timeTextView.setText(MoodEventUtility.getTimeStr(time));

        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ifLocationEnabled){
                    // turn off
                    ifLocationEnabled = false;
                    locationButton.setCompatElevation(12f);
                    locationButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.color_button_lightGrey)));
                }else{
                    ifLocationEnabled = true;
                    locationButton.setCompatElevation(0f);
                    locationButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.color_button_lightGrey_pressed)));
                }
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (moodSelectAdapter.getSelectedMoodType() != null) {
                    // do upload
                    moodEvent.setUniqueID(communicator.generateMoodID());
                    moodEvent.setMoodType(moodSelectAdapter.getSelectedMoodType());

                    if (reasonEditText.getText().toString().isEmpty()){
                        moodEvent.setReason(null);
                    }else{
                        moodEvent.setReason(reasonEditText.getText().toString());
                    }
                    // hey jamie, i changed this part, cuz before, the condition your set up will never be true since the time user
                    // can interact with the UI, onCreate method is called. it should be inside the listener.
                    // also i changed back the UI design, and finished the button functionality.
                    if (ifLocationEnabled) {
                        fetchLastLocation();
                    }
                    communicator.addMoodEvent(moodEvent);
                    finish();
                }else{
                    // prompt user to select a mood
                }
            }
        });
        if (ifLocationEnabled == true) {
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
            fetchLastLocation();
        }
    }

    private String getDateStr (DateJar date) {
        String month = returnMonthStr(date.getMonth());
        return String.format(Locale.getDefault(), "%s %d, %d",month,date.getDay(),date.getYear());
    }

    private String getTimeStr (TimeJar time) {
        return String.format(Locale.getDefault(), "%02d:%02d",time.getHr(),time.getMin());
    }

    private String returnMonthStr(int monthInt){
        String monthStr = null;
        switch (monthInt){
            case 0:
                monthStr = "January";
                break;
            case 1:
                monthStr = "February";
                break;
            case 2:
                monthStr = "March";
                break;
            case 3:
                monthStr = "April";
                break;
            case 4:
                monthStr = "May";
                break;
            case 5:
                monthStr = "June";
                break;
            case 6:
                monthStr = "July";
                break;
            case 7:
                monthStr = "August";
                break;
            case 8:
                monthStr = "September";
                break;
            case 9:
                monthStr = "October";
                break;
            case 10:
                monthStr = "November";
                break;
            case 11:
                monthStr = "December";
                break;
        }
        return monthStr;
    }
    private void fetchLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
                    latitude = currentLocation.getLatitude();
                    longitude = currentLocation.getLongitude();
                    Toast.makeText(getApplicationContext(), latitude
                            +""+longitude,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchLastLocation();
                }
                break;
        }
    }
    public String Lat = Double.toString(latitude);
    public String Lng = Double.toString(longitude);
    public HashMap<String, String> addLatLng(String Latitude, String Longitude){
        HashMap<String, String> locationData = new HashMap<>();
        locationData.put("Latitude", Lat);
        locationData.put("Longitude", Lng);
        db.collection("LatLng").document("users")
                .collection("MoodEvents")
                .document(user.getUid())
                //.collection("MoodEvents")
                .document();
                //.getId();
        return locationData;
    }
}

////        ArrayList<Integer> viewColors = new ArrayList<>();
////        viewColors.add(1);
////        viewColors.add(2);
////        viewColors.add(3);
////        viewColors.add(4);
////
////        ArrayList<String> animalNames = new ArrayList<>();
////        animalNames.add("Happy");
////        animalNames.add("Angry");
////        animalNames.add("Emotional");
////        animalNames.add("Sad");
//
//
//        RecyclerView recyclerView = findViewById(R.id.addRecyclerView);
//        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(NewMoodActivity.this, LinearLayoutManager.HORIZONTAL, false);
//        recyclerView.setLayoutManager(horizontalLayoutManager);
////        adapter = new SelectMoodAdapter(this, viewColors, animalNames);
////        adapter.setClickListener(NewMoodActivity.this);
////        recyclerView.setAdapter(adapter);
//
//
//        //Setting the date
//        //month indexed 1 month behind for some reason
//        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
//        int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
//        int year = Calendar.getInstance().get(Calendar.YEAR);
//        Log.v("dateCheck", "year:" + year + " day:" + day + " month:" + month);
//        date = new DateJar(year, month, day);
//
//
//
//
//        mSocialList = new ArrayList<>();
//        mSocialList.add(new SocialSituationItem("Select Social Situation", 0));
//        mSocialList.add(new SocialSituationItem("Alone", R.drawable.aloneicon));
//        mSocialList.add(new SocialSituationItem("With One Person", R.drawable.onepersonicon));
//        mSocialList.add(new SocialSituationItem("With 2-7 People", R.drawable.twoplusicon));
//        mSocialList.add(new SocialSituationItem("With a Crowd", R.drawable.crowdicon));
//
//        socialSituationSpinner = findViewById(R.id.SituationSpinner);
//        spinnerAdapter = new SpinnerAdapter(this, mSocialList);
//        socialSituationSpinner.setAdapter(spinnerAdapter);
//
//        socialSituationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                SocialSituationItem current = (SocialSituationItem) adapterView.getItemAtPosition(i);
//                socialSitToAdd = current.getSituation();
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
//
//
//
//
//        confirmButton = (ImageButton) findViewById(R.id.confirmNewMood);
//        confirmButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                int hours = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
//                int minutes = Calendar.getInstance().get(Calendar.MINUTE);
//                time = new TimeJar(hours, minutes);
//                Log.v("SOMETHING", moodState + "");
//                Log.v("dateCheck", "hours: " + hours + "minutes:" + minutes);
//
//
//                reasonTextView = findViewById(R.id.reasonText);
//                reason = reasonTextView.getText().toString();
//                Log.v("dateCheck", reason);
//                Log.v("dateCheck", socialSitToAdd);
//
//                String[] temparray = reason.split(" ");
//                if (temparray.length <= 3) {
////
////
//////                        moodObj = new MoodEvent(moodState, date, time);
//////                        Log.v("SOMETHING", moodObj.getDate().toString());
//////                        returnIntent = new Intent();
//////                        returnIntent.putExtra("result", moodObj);
//////                        setResult(Activity.RESULT_OK, returnIntent);
//                        finish();
////
//                }
//                }
//            });
//        }
