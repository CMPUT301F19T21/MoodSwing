package com.example.moodswing.customDataTypes;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

public class CustomIconRenderer extends DefaultClusterRenderer<moodCluster> {
    public CustomIconRenderer(Context context, GoogleMap map,
                              ClusterManager<moodCluster> clusterManager) {
        super(context, map, clusterManager);
    }

    @Override
    protected void onBeforeClusterItemRendered(moodCluster item, MarkerOptions markerOptions) {
        markerOptions.snippet(item.getSnippet());
        markerOptions.title(item.getTitle());
        super.onBeforeClusterItemRendered(item, markerOptions);
    }
}
