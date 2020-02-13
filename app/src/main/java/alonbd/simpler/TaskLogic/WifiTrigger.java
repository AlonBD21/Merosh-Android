package alonbd.simpler.TaskLogic;

import android.content.Intent;

import java.io.Serializable;

public class WifiTrigger extends Trigger implements Serializable {
    @Override
    public boolean matchIntent(Intent intent) {
        return false;
    }
}
