package alonbd.simpler.TaskLogic;

import android.content.Intent;
import android.location.Location;

import java.io.Serializable;

public class WifiTrigger extends Trigger implements Serializable {
    @Override
    public boolean matchIntent(Intent intent) {
        return false;
    }

    @Override
    public boolean matchLocation(Location location) {
        return false;
    }
}
