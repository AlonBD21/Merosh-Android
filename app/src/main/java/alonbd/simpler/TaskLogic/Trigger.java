package alonbd.simpler.TaskLogic;

import android.content.Intent;
import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

public abstract class Trigger implements Serializable {
    private boolean mUsed;

    public abstract boolean matchIntent(Intent intent);
    public abstract boolean matchLocation(Location location);
    public Trigger(){
        mUsed = false;
    }
    public boolean isUsed() {
        return mUsed;
    }
    public void setUsedTrue(){
        mUsed = true;
    }
}
