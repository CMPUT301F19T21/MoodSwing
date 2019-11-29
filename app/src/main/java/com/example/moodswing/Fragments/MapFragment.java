package com.example.moodswing.Fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.moodswing.R;
import com.example.moodswing.customDataTypes.FirestoreUserDocCommunicator;
import com.example.moodswing.customDataTypes.MoodEvent;
import com.example.moodswing.customDataTypes.UserJar;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.moodswing.customDataTypes.MoodEventUtility.FOLLOWING_MODE;
import static com.example.moodswing.customDataTypes.MoodEventUtility.MOODHISTORY_MODE;

/**
 * This class handles the map and setting the marker
 */

public class MapFragment extends Fragment implements OnMapReadyCallback {
    private FirestoreUserDocCommunicator communicator;
    private Integer mode;

    // views
    private FloatingActionButton backBtn;
    private SupportMapFragment mapFrag;
    private GoogleMap map;
    private String selectedMarker;


    private String id;
    private HashMap<Marker, String> markerIdMapping ;

    private Boolean isMapReady;

    public MapFragment(){}

    public MapFragment(Integer mode){
        // should always call filter fragment with this constructor, the empty one should never be used
        // the ArrayList<Integer> is passed by reference, so any change to it inside this fragment will also be changed inside Activity
        this.mode = mode;
        this.communicator = FirestoreUserDocCommunicator.getInstance();
        this.isMapReady = false;
        this.markerIdMapping = new HashMap<>();

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_maps, container, false);

        // find views
        backBtn = root.findViewById(R.id.map_backBtn);
//        mapFragHolder = root.findViewById(R.id.map_placeHolder)
        // set up map

        mapFrag = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);

        // set listeners
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeFrag();
            }
        });
        return root;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;
        map.setMapStyle(MapStyleOptions.loadRawResourceStyle(getContext(), R.raw.map_style_json));
        initElements();
        this.isMapReady = true;
    }

    public void initElements() {
        // clear map
        map.clear();
        selectedMarker = null;
        // update most recent mood LatLng
        MoodEvent mostRecentMoodEvent = null;
        switch (mode){
            case MOODHISTORY_MODE:
                ArrayList<MoodEvent> moodEvents = communicator.getMoodEvents();
                for (MoodEvent moodEvent : moodEvents){
                    if (moodEvent.getLatitude() != null){
                        setUpMarker(moodEvent, communicator.getUsername());
                        if (mostRecentMoodEvent == null){
                            mostRecentMoodEvent = moodEvent;
                        }
                    }
                }
                break;
            case FOLLOWING_MODE:
                ArrayList<UserJar> userJars = communicator.getUserJars();
                for (UserJar userJar : userJars){
                    if (userJar.getMoodEvent().getLatitude() != null){
                        setUpMarker(userJar.getMoodEvent(), userJar.getUsername());
                        if (mostRecentMoodEvent == null){
                            mostRecentMoodEvent = userJar.getMoodEvent();
                        }
                    }
                }
                break;
        }

        // set up camera using most recent mood, if mood not empty
        if (mostRecentMoodEvent != null){
            LatLng centerFocus = new LatLng(mostRecentMoodEvent.getLatitude(), mostRecentMoodEvent.getLongitude());
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(centerFocus, 11));
        }

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                String markerID = markerIdMapping.get(marker);
                if (selectedMarker != markerID){
                    marker.showInfoWindow();
                    selectedMarker = markerID;
                    return false;
                }else{
                    marker.hideInfoWindow();
                    toDetailedView(markerIdMapping.get(marker));
                    return true;
                }
            }
        });
    }

    /**
     * Sets up the map marker on the map
     * @param moodEvent the moodEvent associated with the location
     * @param username The users username that the marker is for
     */
    private void setUpMarker(MoodEvent moodEvent, String username){
        LatLng latLng = null;
        Marker marker = null;
        BitmapDrawable mapMarkerDrawable = null;
        Bitmap mapMarker = null;
        int MARKER_SIZE = 250;

        switch (moodEvent.getMoodType()){
            case 1:
                mapMarkerDrawable = (BitmapDrawable)getResources().getDrawable(R.drawable.moodm1);
                mapMarker = Bitmap.createScaledBitmap(mapMarkerDrawable.getBitmap(),MARKER_SIZE,MARKER_SIZE,false);
                        latLng = new LatLng(moodEvent.getLatitude(), moodEvent.getLongitude());
                marker = map.addMarker(new MarkerOptions()
                        .position(latLng)
                        .icon(BitmapDescriptorFactory.fromBitmap(mapMarker))
                        .title("HAPPY")
                        .snippet(username));
                break;
            case 2:
                mapMarkerDrawable = (BitmapDrawable)getResources().getDrawable(R.drawable.moodm2);
                mapMarker = Bitmap.createScaledBitmap(mapMarkerDrawable.getBitmap(),MARKER_SIZE,MARKER_SIZE,false);
                latLng = new LatLng(moodEvent.getLatitude(), moodEvent.getLongitude());
                marker = map.addMarker(new MarkerOptions()
                        .position(latLng)
                        .icon(BitmapDescriptorFactory.fromBitmap(mapMarker))
                        .title("SAD")
                        .snippet(username));
                break;
            case 3:
                mapMarkerDrawable = (BitmapDrawable)getResources().getDrawable(R.drawable.moodm3);
                mapMarker = Bitmap.createScaledBitmap(mapMarkerDrawable.getBitmap(),MARKER_SIZE,MARKER_SIZE,false);
                latLng = new LatLng(moodEvent.getLatitude(), moodEvent.getLongitude());
                marker = map.addMarker(new MarkerOptions()
                        .position(latLng)
                        .icon(BitmapDescriptorFactory.fromBitmap(mapMarker))
                        .title("ANGRY")
                        .snippet(username));
                break;
            case 4:
                mapMarkerDrawable = (BitmapDrawable)getResources().getDrawable(R.drawable.moodm4);
                mapMarker = Bitmap.createScaledBitmap(mapMarkerDrawable.getBitmap(),MARKER_SIZE,MARKER_SIZE,false);
                latLng = new LatLng(moodEvent.getLatitude(), moodEvent.getLongitude());
                marker = map.addMarker(new MarkerOptions().position(latLng)
                        .icon(BitmapDescriptorFactory.fromBitmap(mapMarker))
                        .title("EMOTIONAL")
                        .snippet(username));
                break;
            case 5:
                mapMarkerDrawable = (BitmapDrawable)getResources().getDrawable(R.drawable.moodm5);
                mapMarker = Bitmap.createScaledBitmap(mapMarkerDrawable.getBitmap(),MARKER_SIZE,MARKER_SIZE,false);
                latLng = new LatLng(moodEvent.getLatitude(), moodEvent.getLongitude());
                marker = map.addMarker(new MarkerOptions()
                        .position(latLng)
                        .icon(BitmapDescriptorFactory.fromBitmap(mapMarker))
                        .title("HEART BROKEN")
                        .snippet(username));
                break;
            case 6:
                mapMarkerDrawable = (BitmapDrawable)getResources().getDrawable(R.drawable.moodm6);
                mapMarker = Bitmap.createScaledBitmap(mapMarkerDrawable.getBitmap(),MARKER_SIZE,MARKER_SIZE,false);
                latLng = new LatLng(moodEvent.getLatitude(), moodEvent.getLongitude());
                marker = map.addMarker(new MarkerOptions().position(latLng)
                        .icon(BitmapDescriptorFactory.fromBitmap(mapMarker))
                        .title("IN LOVE")
                        .snippet(username));
                break;
            case 7:
                mapMarkerDrawable = (BitmapDrawable)getResources().getDrawable(R.drawable.moodm7);
                mapMarker = Bitmap.createScaledBitmap(mapMarkerDrawable.getBitmap(),MARKER_SIZE,MARKER_SIZE,false);
                latLng = new LatLng(moodEvent.getLatitude(), moodEvent.getLongitude());
                marker = map.addMarker(new MarkerOptions().position(latLng)
                        .icon(BitmapDescriptorFactory.fromBitmap(mapMarker))
                        .title("SCARED")
                        .snippet(username));
                break;
            case 8:
                mapMarkerDrawable = (BitmapDrawable)getResources().getDrawable(R.drawable.moodm8);
                mapMarker = Bitmap.createScaledBitmap(mapMarkerDrawable.getBitmap(),MARKER_SIZE,MARKER_SIZE,false);
                latLng = new LatLng(moodEvent.getLatitude(), moodEvent.getLongitude());
                marker = map.addMarker(new MarkerOptions().position(latLng)
                        .icon(BitmapDescriptorFactory.fromBitmap(mapMarker))
                        .title("SURPRISED")
                        .snippet(username));
                break;
        }
        markerIdMapping.put(marker, moodEvent.getUniqueID());
    }

    /**
     * the map can redirect to detailed view of each of the moods. this method handles that
     * @param ID the user ID
     */
    public void toDetailedView(String ID) {
        getFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_fullScreenOverlay, new MapDetailAdapterFragment(this.mode, ID), "outerDetailView")
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commitAllowingStateLoss();
    }

    /**
     * method to close the map fragment
     */
    private void closeFrag() {
        getChildFragmentManager()
                .beginTransaction()
                .remove(mapFrag)
                .commit();

        getFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                .remove(this)
                .commit();
    }
    @Override
    public void onResume() {
        super.onResume();

        if(getView() == null){
            return;
        }

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
                    closeFrag();
                    return true;
                }
                return false;
            }
        });
    }
}