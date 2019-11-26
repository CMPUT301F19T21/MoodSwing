package com.example.moodswing.customDataTypes;

import android.graphics.drawable.Icon;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class moodCluster implements ClusterItem {
    private final String username;
    private final LatLng latLng;

    public moodCluster(String username, LatLng latLng) {
        this.username = username;
        this.latLng = latLng;
    }
    @Override
    public LatLng getPosition() {
        return latLng;
    }
    @Override
    public String getTitle() {
        return username;
    }
    @Override
    public String getSnippet() {
        return "";
    }
}
