package com.example.moodswing.customDataTypes;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

public class LocationToAddressTask extends AsyncTask<Object, Object, Object> {
    private Location location;
    private TextView locationTextView;
    private Context context;

    public LocationToAddressTask(Context context, Location location, TextView locationTextView) {
        this.context = context;
        this.location = location;
        this.locationTextView = locationTextView;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        if (location != null){
            try {
                List<Address> firstAddressList = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                if (firstAddressList != null){
                    if (firstAddressList.isEmpty()){
                        // error
                    }else{
                        //
                        return firstAddressList.get(0);
                    }
                }else {
                    // error
                }
            } catch (Exception e) {
                // display error msg
                e.printStackTrace();
            }
        }else{
            //
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        if (o != null){
            if (locationTextView != null){
                Address address = (Address) o;
                String thoroughfare = address.getThoroughfare();
                if (thoroughfare == null){
                    locationTextView.setText("nowhere!");
                }else{
                    locationTextView.setText(thoroughfare);
                }
            }
        }
    }
}