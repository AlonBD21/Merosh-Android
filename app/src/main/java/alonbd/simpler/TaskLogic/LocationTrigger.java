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
    private int mRadius;

    private boolean mJustActivated;

    public LocationTrigger(boolean mSingleUse, LatLng mLatLng, int mRadius) {
        super(mSingleUse);
        mLat = mLatLng.latitude;
        mLng = mLatLng.longitude;
        this.mRadius = mRadius;
        mJustActivated = false;
    }

    @Override
    public boolean matchCondition(Object object) {
        if(object instanceof Location) {
            Location location = (Location) object;
            float[] results = new float[3];
            Location.distanceBetween(mLat, mLng, location.getLatitude(), location.getLongitude(), results);
            Log.d(TAG, "matchLocation: Results: Distance-" + results[0] + ",BearingA-" + results[1] + ",BearingB-" + results[2]);
            float distance = results[0];

            if(distance <= mRadius) {//In activation range
                if(isInCooldown()) return false;
                else {
                    mJustActivated = true;
                    return true;
                }
            } else if(distance >= 2 * mRadius) {
                mJustActivated = false;
                return false;
            } else return false;

        }
        return false;
    }

    public boolean isInCooldown() {
        return mJustActivated && !isSingleUse();
    }

    @Override
    protected View getTypeDescriptiveView(Context context) {
        View view = View.inflate(context, R.layout.layout_view_location, null);
        ((TextView) view.findViewById(R.id.location_tv)).setText(LocationService.geoCode(context, mLat, mLng));
        ((TextView) view.findViewById(R.id.radius_tv)).setText(String.valueOf(mRadius));
        TextView cdTv = view.findViewById(R.id.cooldown_tv);
        cdTv.setText(getCooldownMessage(context));
        if(!isInCooldown()) cdTv.setVisibility(View.GONE);
        return view;
    }

    @Override
    public boolean isReady() {
        if(isInCooldown()) return false;
        return super.isReady();
    }

    public String getCooldownMessage(Context context) {
        return context.getString(R.string.cooldown_message, mRadius * 2);
    }
}
