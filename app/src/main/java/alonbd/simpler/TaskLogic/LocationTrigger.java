package alonbd.simpler.TaskLogic;

import android.content.Intent;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

public class LocationTrigger extends Trigger implements Serializable {
    private double lat;
    private double lng;

    public LocationTrigger(LatLng mLatLng) {
        super();
        lat = mLatLng.latitude;
        lng = mLatLng.longitude;
    }

    @Override
    public boolean matchIntent(Intent intent) {
        return false;
    }
}
