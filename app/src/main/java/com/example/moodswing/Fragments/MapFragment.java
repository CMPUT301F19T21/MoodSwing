package com.example.moodswing.Fragments;

import android.os.Bundle;
import android.util.Log;
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
import com.example.moodswing.customDataTypes.moodCluster;
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

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private static final int MOODHISTORY_MODE = 1;
    private static final int FOLLOWING_MODE = 2;

    private FirestoreUserDocCommunicator communicator;
    private Integer mode;

    // views
    private FloatingActionButton backBtn;
    private SupportMapFragment mapFrag;
    private GoogleMap map;


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

    private void setUpMarker(MoodEvent moodEvent, String username){
        LatLng latLng = null;
        Marker marker = null;
        switch (moodEvent.getMoodType()){
            case 1:
                latLng = new LatLng(moodEvent.getLatitude(), moodEvent.getLongitude());
                marker = map.addMarker(new MarkerOptions()
                        .position(latLng)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.mood1))
                        .title(username + "was HAPPY")
                        .snippet("Click to view Details"));
                break;
            case 2:
                latLng = new LatLng(moodEvent.getLatitude(), moodEvent.getLongitude());
                marker = map.addMarker(new MarkerOptions()
                        .position(latLng)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.mood2))
                        .title(username + "was SAD")
                        .snippet("Click to view Details"));
                break;
            case 3:
                latLng = new LatLng(moodEvent.getLatitude(), moodEvent.getLongitude());
                marker = map.addMarker(new MarkerOptions()
                        .position(latLng)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.mood3))
                        .title(username + "was ANGRY")
                        .snippet("Click to view Details"));
                break;
            case 4:
                latLng = new LatLng(moodEvent.getLatitude(), moodEvent.getLongitude());
                marker = map.addMarker(new MarkerOptions().position(latLng)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.mood4))
                        .title(username + "was EMOTIONAL")
                        .snippet("Click to view Details"));
                break;
        }
        markerIdMapping.put(marker, moodEvent.getUniqueID());
    }

    public void initElements() {
        // clear map
        Log.d("test init", "initElements: ");
        map.clear();
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
        }

        // set up camera using most recent mood, if mood not empty
        if (mostRecentMoodEvent != null){
            LatLng centerFocus = new LatLng(mostRecentMoodEvent.getLatitude(), mostRecentMoodEvent.getLongitude());
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(centerFocus, 14));
        }

        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                toDetailedView(markerIdMapping.get(marker));
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;
        map.setMapStyle(MapStyleOptions.loadRawResourceStyle(getContext(), R.raw.map_style_json));
        initElements();
        this.isMapReady = true;
    }

    public void toDetailedView(String ID) {
        getFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_fullScreenOverlay, new MapDetailAdapterFragment(this.mode, ID), "outerDetailView")
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commitAllowingStateLoss();
    }
}