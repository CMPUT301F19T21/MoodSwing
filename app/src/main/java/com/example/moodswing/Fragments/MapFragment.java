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

    private FloatingActionButton backBtn;
    private SupportMapFragment mapFrag;
    private GoogleMap map;
    private Integer mood, moodDetail, user;
    private ArrayList<MoodEvent> moodList, moodTypeList;
    private ClusterManager<moodCluster>clusterManager;
    private FirestoreUserDocCommunicator communicator;
    private UserJar userJar;
    private List<moodCluster>clusters = new ArrayList<>();
    private MapFragment mapFragment;
    private String followingUid, id;
    private HashMap<Marker, String> markerIdMapping = new HashMap<>();

    public MapFragment(){}

    public MapFragment(Integer mood){
        // should always call filter fragment with this constructor, the empty one should never be used
        // the ArrayList<Integer> is passed by reference, so any change to it inside this fragment will also be changed inside Activity
        this.mapFragment = this;
        switch (mood) {
            case 1:
                //displaying my moods on the map
                this.communicator = FirestoreUserDocCommunicator.getInstance();
                this.mood = mood;
                this.moodList = communicator.getMoodEvents();
                break;
            case 2:
                //displaying follwing's moods on the map
                this.communicator = FirestoreUserDocCommunicator.getInstance();
                this.mood = mood;
//                this.moodList = communicator.getUserJars();
                this.moodList = new ArrayList<>();
                ArrayList<UserJar> userJars = communicator.getUserJars();
                for (UserJar jar: userJars) {
                    moodList.add(jar.getMoodEvent());
                }
                break;
            default:
                break;
        }
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

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;
        map.setMapStyle(MapStyleOptions.loadRawResourceStyle(getContext(), R.raw.map_style_json));

        FirestoreUserDocCommunicator firebaseDoc = FirestoreUserDocCommunicator.getInstance();
        String uid = firebaseDoc.getUsername();
        for (MoodEvent moodEvent : moodList) {
            if (moodEvent.getLatitude() != null) {
                if (moodEvent.getMoodType() == 1) {
                    LatLng latlng = new LatLng(moodEvent.getLatitude(), moodEvent.getLongitude());
                    Marker marker = googleMap.addMarker(new MarkerOptions().position(latlng)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.happy_marker))
                            .title(uid + " Was Happy!")
                            .snippet("View Details"));
                    id = moodEvent.getUniqueID();
                    markerIdMapping.put(marker, id);
                } else if (moodEvent.getMoodType() == 2) {
                    LatLng latlng = new LatLng(moodEvent.getLatitude(), moodEvent.getLongitude());
                    Marker marker = googleMap.addMarker(new MarkerOptions().position(latlng)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.sad_marker))
                            .title(uid + " Was Sad!")
                            .snippet("View Details"));
                    id = moodEvent.getUniqueID();
                    markerIdMapping.put(marker, id);
                } else if (moodEvent.getMoodType() == 3) {
                    LatLng latlng = new LatLng(moodEvent.getLatitude(), moodEvent.getLongitude());
                    Marker marker = googleMap.addMarker(new MarkerOptions().position(latlng)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.angry_marker))
                            .title(uid + " Was Angry!")
                            .snippet("View Details"));
                    id = moodEvent.getUniqueID();
                    markerIdMapping.put(marker, id);
                } else {
                    LatLng latlng = new LatLng(moodEvent.getLatitude(), moodEvent.getLongitude());
                    Marker marker = googleMap.addMarker(new MarkerOptions().position(latlng)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.emotional_marker))
                            .title(uid + " Was Emotional!")
                            .snippet("View Details"));
                    id = moodEvent.getUniqueID();
                    markerIdMapping.put(marker, id);
                }
            }
        }
        LatLng UofA = new LatLng(53.523220, -113.526321);
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(UofA));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(UofA, 14));

        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            FirestoreUserDocCommunicator firebaseDoc = FirestoreUserDocCommunicator.getInstance();
            String uid = firebaseDoc.getUsername();

            @Override
            public void onInfoWindowClick(Marker marker) {
                String markerId = markerIdMapping.get(marker);
                Toast.makeText(getContext(), markerId, Toast.LENGTH_SHORT).show();
                toDetailedView(1);
            }
        });
    }

        public void toDetailedView(int moodPosition) {
            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_fullScreenOverlay, new MapDetailAdapterFragment(1), "outerDetailView")
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commitAllowingStateLoss();
        }
}