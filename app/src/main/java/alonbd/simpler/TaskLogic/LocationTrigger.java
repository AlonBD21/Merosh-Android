package alonbd.simpler.TaskLogic;

import android.content.Context;
import android.location.Location;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

import alonbd.simpler.BackgroundAndroid.LocationService;
import alonbd.simpler.R;

public class LocationTrigger extends Trigger implements Serializable {
    private final static String TAG = "ThugLocationTrigger";
    private double mLat;
    private double mLng;
    private boolean mInCooldown;
    private int mRadius;

    public LocationTrigger(boolean mSingleUse, LatLng mLatLng, int mRadius) {
        super(mSingleUse);
        mLat = mLatLng.latitude;
        mLng = mLatLng.longitude;
        this.mRadius = mRadius;
        mInCooldown = false;
    }

    @Override
    public boolean matchCondition(Object object) {
        if(object instanceof Location) {
            Location location = (Location) object;
            float[] results = new float[3];
            Location.distanceBetween(mLat, mLng, location.getLatitude(), location.getLongitude(), results);
            Log.d(TAG, "matchLocation: Results: Distance-" + results[0] + ",BearingA-" + results[1] + ",BearingB-" + results[2]);
            if(mInCooldown) {
                if(results[0] >= 2 * mRadius) mInCooldown = false;
                return false;
            }
            boolean out = results[0] <= mRadius;
            if(out) mInCooldown = true;
            return out;
        }
        return false;
    }

    @Override
    protected View getTypeDescriptiveView(Context context) {
        View view = View.inflate(context, R.layout.layout_view_location, null);
        ((TextView) view.findViewById(R.id.location_tv)).setText(LocationService.geoCode(context, mLat, mLng));
        ((TextView) view.findViewById(R.id.radius_tv)).setText(String.valueOf(mRadius));
        return view;
    }

}
