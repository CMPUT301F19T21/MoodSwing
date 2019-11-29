package com.example.moodswing.Fragments;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.moodswing.NewMoodActivity;
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

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.moodswing.customDataTypes.MoodEventUtility.FOLLOWING_MODE;
import static com.example.moodswing.customDataTypes.MoodEventUtility.MOODHISTORY_MODE;

/**
 * This class is for the google API to set up the map
 */
public class MapSetUpFragment extends Fragment implements OnMapReadyCallback{

        // views
        private FloatingActionButton backBtn;
        private SupportMapFragment mapFrag;
        private GoogleMap map;

        private Location location;

        public MapSetUpFragment(){}

        public MapSetUpFragment(Location location){
            // should always call filter fragment with this constructor, the empty one should never be used
            // the ArrayList<Integer> is passed by reference, so any change to it inside this fragment will also be changed inside Activity
            this.location = location;
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

            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            map.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title("Drag to move your location")
                    .draggable(true));
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));

            map.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                @Override
                public void onMarkerDragStart(Marker marker) {

                }

                @Override
                public void onMarkerDrag(Marker marker) {

                }

                @Override
                public void onMarkerDragEnd(Marker marker) {
                    LatLng postPosition = marker.getPosition();
                    location.setLatitude(postPosition.latitude);
                    location.setLongitude(postPosition.longitude);
                }
            });
        }

    /**
     * method to close the fragment
     */
    private void closeFrag() {
            ((NewMoodActivity) getActivity()).MapSetUpFragmentCallBack();

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
