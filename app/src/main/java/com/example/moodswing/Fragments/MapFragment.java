package com.example.moodswing.Fragments;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.moodswing.MainActivity;
import com.example.moodswing.R;
import com.example.moodswing.customDataTypes.FirestoreUserDocCommunicator;
import com.example.moodswing.customDataTypes.MoodEvent;
import com.example.moodswing.customDataTypes.MoodEventUtility;
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
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.moodswing.customDataTypes.MoodEventUtility.FOLLOWING_MODE;
import static com.example.moodswing.customDataTypes.MoodEventUtility.MOODHISTORY_MODE;

/**
 * This class handles the map and setting the marker
 */

public class MapFragment extends Fragment implements OnMapReadyCallback, MainActivity.controllableFragment {
    private FirestoreUserDocCommunicator communicator;
    private Integer mode;

    // views
    private SupportMapFragment mapFrag;
    private GoogleMap map;

    // marker
    private HashMap<Marker, String> markerIdMapping ;
    private String markerIDInSelect;

    public MapFragment(){}

    public MapFragment(Integer mode){
        // should always call filter fragment with this constructor, the empty one should never be used
        // the ArrayList<Integer> is passed by reference, so any change to it inside this fragment will also be changed inside Activity
        this.mode = mode;
        this.communicator = FirestoreUserDocCommunicator.getInstance();
        this.markerIdMapping = new HashMap<>();
        this.markerIDInSelect = null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_maps, container, false);

        // set up map
        mapFrag = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);

        return root;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        this.map = googleMap;
        map.setMapStyle(MapStyleOptions.loadRawResourceStyle(getContext(), R.raw.map_style_json));
        initElements();
    }

    public void initElements() {
        // clear map
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
                String markerId = markerIdMapping.get(marker);
                toDetailedView(markerId);
                return true;        // disable info window
            }
        });

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                closeDetailedBoard();
            }
        });
    }

    /**
     * Sets up the map marker on the map
     * @param moodEvent the moodEvent associated with the location
     * @param username The users username that the marker is for
     */
    // initially set all values to null so the map is automatically updated
    private void setUpMarker(MoodEvent moodEvent, String username){
        LatLng latLng = null;
        Marker marker = null;
        BitmapDrawable mapMarkerDrawable = null;
        Bitmap mapMarker = null;
        int MARKER_SIZE = 250;

        int moodType = moodEvent.getMoodType();

        mapMarkerDrawable = (BitmapDrawable)getResources().getDrawable(MoodEventUtility.getMoodDrawableMapInt(moodType));
        mapMarker = Bitmap.createScaledBitmap(mapMarkerDrawable.getBitmap(),MARKER_SIZE,MARKER_SIZE,false);
        latLng = new LatLng(moodEvent.getLatitude(), moodEvent.getLongitude());
        marker = map.addMarker(new MarkerOptions()
                .position(latLng)
                .icon(BitmapDescriptorFactory.fromBitmap(mapMarker))
                .title(MoodEventUtility.getMoodType(moodType))
                .snippet(username));

        markerIdMapping.put(marker, moodEvent.getUniqueID());
    }

    public void closeDetailedBoard() {
        if (markerIDInSelect != null) {
            markerIDInSelect = null;
            getFragmentManager()
                    .beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                    .remove(getFragmentManager().findFragmentByTag("detailMapBoardFrag"))
                    .commitAllowingStateLoss();
        }
    }

    /**
     * the map can redirect to detailed view of each of the moods. this method handles that
     * @param ID the user ID
     */
    public void toDetailedView(String ID) {

        if (markerIDInSelect == ID) {
            closeDetailedBoard();
        }else{
            markerIDInSelect = ID;
            switch (mode){
                case MOODHISTORY_MODE:
                    MoodEvent moodEvent = communicator.getMoodEvent(communicator.getMoodPosition(ID));
                    getFragmentManager()
                            .beginTransaction()
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .replace(R.id.detailedMoodDisplayBoard, new MoodDetailMapFragment(moodEvent), "detailMapBoardFrag")
                            .commitAllowingStateLoss();
                    break;
                case FOLLOWING_MODE:
                    UserJar userJar = communicator.getUserJar(communicator.getUserJarPosition(ID));
                    getFragmentManager()
                            .beginTransaction()
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .replace(R.id.detailedMoodDisplayBoard, new MoodDetailMapFragment(userJar), "detailMapBoardFrag")
                            .commitAllowingStateLoss();
                    break;
            }
        }


    }

    /**
     * method to close the map fragment
     */
    public void closeFrag() {
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
                    ((MainActivity) getActivity()).getCenterBtn().performClick();
                    return true;
                }
                return false;
            }
        });
    }
}