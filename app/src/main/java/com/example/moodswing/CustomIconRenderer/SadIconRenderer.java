package com.example.moodswing.CustomIconRenderer;

import android.content.Context;

import com.example.moodswing.R;
import com.example.moodswing.customDataTypes.moodCluster;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

public class SadIconRenderer extends DefaultClusterRenderer<moodCluster> {
    public SadIconRenderer(Context context, GoogleMap map,
                             ClusterManager<moodCluster> clusterManager) {
        super(context, map, clusterManager);
    }

    @Override
    protected void onBeforeClusterItemRendered(moodCluster item, MarkerOptions markerOptions) {
        MarkerOptions sadIcon = markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.sad_marker));
    }
}
