package alonbd.simpler.TaskLogic;

import android.content.Intent;

import java.io.Serializable;

public abstract class Trigger implements Serializable {
    private boolean consumed;
    public Trigger(){
        consumed = false;
    }
    public void setConsumedTrue(){
        consumed = true;
    }
    public abstract boolean matchIntent(Intent intent);
}
