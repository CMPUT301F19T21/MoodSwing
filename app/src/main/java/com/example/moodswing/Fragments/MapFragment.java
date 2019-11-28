package com.example.moodswing.Fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
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
import com.example.moodswing.customDataTypes.MyItem;
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

public class MapFragment extends Fragment implements OnMapReadyCallback {
    private FirestoreUserDocCommunicator communicator;
    private Integer mode;

    // views
    private FloatingActionButton backBtn;
    private SupportMapFragment mapFrag;
    private GoogleMap map;
    private ClusterManager<MyItem> clusterManager;
    private List<MyItem> items = new ArrayList<>();

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
        clusterManager = new ClusterManager<MyItem>(getContext(), map);
        map.setMapStyle(MapStyleOptions.loadRawResourceStyle(getContext(), R.raw.map_style_json));
        initElements();
        this.isMapReady = true;
    }

    public void initElements() {
        // clear map
        Log.d("test init", "initElements: ");
        map.clear();
        // update most recent mood LatLng
        MoodEvent mostRecentMoodEvent = null;
        map.setOnCameraIdleListener(clusterManager);
        map.setOnMarkerClickListener(clusterManager);
        switch (mode){
            case MOODHISTORY_MODE:
                items.clear();
                Log.d("size of items", Integer.toString(items.size()));
                ArrayList<MoodEvent> moodEvents = communicator.getMoodEvents();
                for (MoodEvent moodEvent : moodEvents){
                    if (moodEvent.getLatitude() != null){
                        setUpMarker(moodEvent, communicator.getUsername());
                        if (mostRecentMoodEvent == null){
                            mostRecentMoodEvent = moodEvent;
                        }
                        LatLng latlng = new LatLng (moodEvent.getLatitude(), moodEvent.getLongitude());
                        items.add(new MyItem(latlng));
                        clusterManager.addItems(items);
                        clusterManager.cluster();
                    }
                }
                break;
            case FOLLOWING_MODE:
                items.clear();
                ArrayList<UserJar> userJars = communicator.getUserJars();
                for (UserJar userJar : userJars){
                    if (userJar.getMoodEvent().getLatitude() != null){
                        setUpMarker(userJar.getMoodEvent(), userJar.getUsername());
                        if (mostRecentMoodEvent == null){
                            mostRecentMoodEvent = userJar.getMoodEvent();
                        }
                        LatLng latlng = new LatLng (userJar.getMoodEvent().getLatitude(), userJar.getMoodEvent().getLongitude());
                        items.add(new MyItem(latlng));
                        clusterManager.addItems(items);
                        clusterManager.cluster();
                    }
                }
                break;
        }

        // set up camera using most recent mood, if mood not empty
        if (mostRecentMoodEvent != null){
            LatLng centerFocus = new LatLng(mostRecentMoodEvent.getLatitude(), mostRecentMoodEvent.getLongitude());
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(centerFocus, 14));
        }

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (marker.isInfoWindowShown()){
                    toDetailedView(markerIdMapping.get(marker));
                    return true;
                }else{
                    marker.showInfoWindow();
                    return false;
                }
            }
        });
    }

    private void setUpMarker(MoodEvent moodEvent, String username){
        LatLng latLng = null;
        Marker marker = null;
        BitmapDrawable mapMarkerDrawable = null;
        Bitmap mapMarker = null;
        int MARKER_SIZE = 200;

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
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.moodm2))
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
        }
        markerIdMapping.put(marker, moodEvent.getUniqueID());
    }

    public void toDetailedView(String ID) {
        getFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_fullScreenOverlay, new MapDetailAdapterFragment(this.mode, ID), "outerDetailView")
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commitAllowingStateLoss();
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
}