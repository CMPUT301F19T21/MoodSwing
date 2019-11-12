package com.example.moodswing;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.moodswing.customDataTypes.FirestoreUserDocCommunicator;
import com.example.moodswing.customDataTypes.MoodEvent;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

/**
 * this class is the GoogleMapActivity. it handles the google API functionality
 */
public class GoogleMapActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap map;
    private Button backButton;
    private Location currentLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_map);

        /*
         * Creates a button that takes the user back to the previous page
         */
        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onBackPressed();
            }
        });

        /*
         * A public class that extends to Google Api that allows the users to access google map
         */
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fetchLastLocation();
    }

    /*
     * A method that gets the data of current location of the user to display on the map
     * If the user does not allow the app to access current location, the method returns nothing
     */
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
                    SupportMapFragment supportMapFragment = (SupportMapFragment)
                            getSupportFragmentManager().findFragmentById(R.id.map);
                    supportMapFragment.getMapAsync(GoogleMapActivity.this);
                }
            }
        });
    }

    /*
     * A method that stores latitude and longitude of the user's location
     * when a mood is added and GPS is on and stores null when off
     * Displays a marker on the map using the latitude and longitude from firestore
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        FirestoreUserDocCommunicator firebaseDoc = FirestoreUserDocCommunicator.getInstance();
        ArrayList<MoodEvent> moodEvents = firebaseDoc.getMoodEvents();
        String uid = firebaseDoc.getUsername();
        for (MoodEvent moodEvent : moodEvents) {
            if (moodEvent.getLatitude() != null) {
                LatLng latlng = new LatLng(moodEvent.getLatitude(), moodEvent.getLongitude());
                googleMap.addMarker(new MarkerOptions().position(latlng)
                        .title(uid));
            }
        }
        LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
    }

    /*
     * Asks the user to allow user's location
     */
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
}