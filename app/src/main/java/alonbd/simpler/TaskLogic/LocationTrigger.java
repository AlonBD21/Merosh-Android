package alonbd.simpler.TaskLogic;

import android.content.Intent;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;


public class LocationTrigger extends Trigger implements Serializable {
    private final static String TAG = "ThugLocationTrigger";
    private double mLat;
    private double mLng;
    private double mRadius;

    public LocationTrigger(LatLng mLatLng) {
        super();
        mLat = mLatLng.latitude;
        mLng = mLatLng.longitude;
        mRadius = 50;
    }

    @Override
    public boolean matchIntent(Intent intent) {
        return false;
    }

    @Override
    public boolean matchLocation(Location location) {
        float[] results = new float[3];
        Location.distanceBetween(mLat,mLng,location.getLatitude(),location.getLongitude(),results);
        Log.d(TAG, "matchLocation: Results: Distance-"+results[0]+",BearingA-"+results[1]+",BearingB-"+results[2]);
        return results[0] <= mRadius;
    }
}
