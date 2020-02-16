package alonbd.simpler.TaskLogic;

import android.content.Intent;

import java.io.Serializable;

public abstract class Trigger implements Serializable {
    private boolean mUsed;

    public abstract boolean matchIntent(Intent intent);
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
